package ifpr.evento.mb;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.evento.Evento;
import ifpr.evento.TipoEvento;
import ifpr.evento.dao.EventoDao;
import ifpr.evento.eventoPessoa.EventoPessoa;
import ifpr.evento.eventoPessoa.dao.EventoPessoaDao;
import ifpr.evento.model.EventoLazyDataModel;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;
import ifpr.model.LoginControllerMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name = "eventoMB")
@ViewScoped
public class EventoMB {
	

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	private Evento evento;

	private List<Evento> eventoList;

	@ManagedProperty(value = "#{eventoDao}")
	private EventoDao eventoDao;
	
	@ManagedProperty(value="#{pessoaDao}")
	private PessoaDao pessoaDao;

	private List<Evento> eventoFiltered;

	@ManagedProperty(value = "#{eventoLazyDataModel}")
	private EventoLazyDataModel eventoLazyDataModel;
	
	private LoginControllerMB loginController;

	@ManagedProperty(value = "#{eventoPessoaDao}")
	private EventoPessoaDao eventoPessoaDao;
	
	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;
	
	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	private List<Campus> listaCampus;

	private Campus campus;

	private List<Modalidade> listaModalidade;

	private Modalidade modalidade;

	private List<Estudante> estudantes;

	private List<Estudante> estudantesSelecionados;
	
	private EventoPessoa eventoPessoa;
	
	private TipoPessoa role;
	
	private TipoEvento tipoEvento;

	private boolean isUpdate;
	
	private boolean isPrimeiraTab;
	
	private Pessoa pessoaLogada;
	
	private int index;
	
	private boolean isAdm;
	
	
	public EventoMB() {
		eventoFiltered = new ArrayList<Evento>();
		isPrimeiraTab = true;
		isUpdate = true;
		index = 0;
	}

	public void criar() {
		evento = new Evento();
		isUpdate = false;
		evento.setEventoPessoas(new ArrayList<EventoPessoa>());
		index = 0;
		isPrimeiraTab = true;
	}
	
	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
		listaModalidade = modalidadeDao.listarAlfabetica();
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(
				context, "#{loginControllerMB}", LoginControllerMB.class);
		pessoaLogada = loginController.getPessoaLogada();
		estudantesSelecionados = new ArrayList<Estudante>();
		role = TipoPessoa.ROLE_ADMIN;
		tipoEvento = TipoEvento.MAPAMODALIDADE;
		if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN))
			isAdm = true;
		else
			isAdm = false;
	}




	public void cancelar() {
		if (isUpdate == true) {
			evento = null;
			modalidade = null;
			campus = null;
		} else {
			removerPessoa();
			modalidade = null;
			campus = null;
			isUpdate = true;
		}
		isPrimeiraTab = true;
		index = 0;
	}


	public void salvar() {
		isPrimeiraTab = false;
		if (evento.getId() != null) {
			eventoDao.update(evento);
		} else {
			if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP) || pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_COORD)){
				evento.setTipo(TipoEvento.TREINO);
				evento.setResponsavel(pessoaLogada);
				eventoDao.salvar(evento);
			}
			else if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN)){
				evento.setTipo(tipoEvento);
				evento.setResponsavel(pessoaLogada);
				eventoDao.salvar(evento);
			}
		}
		index = 1;
	}
	
	private void salvarEventoAdm(){
		evento.setResponsavel(pessoaLogada);
		evento.setTipo(tipoEvento);
		eventoDao.salvar(evento);
		List<Pessoa> pessoas = pessoaDao.findByRole(role);
		EventoPessoa evp;
		for(int i = 0; i < pessoas.size(); ++i){
			evp = new EventoPessoa();
			evp.setPessoa(pessoas.get(i));
			evp.setEvento(evento);
			eventoPessoaDao.salvar(evp);
			evento.getEventoPessoas().add(evp);
		}
		eventoDao.update(evento);
	}

	public void adicionarEstudante( ) {
		EventoPessoa evp;
		for(int i = 0; i < estudantesSelecionados.size(); ++i){
			evp = new EventoPessoa();
			evp.setPessoa(estudantesSelecionados.get(i));
			evp.setEvento(evento);
			evp.setWasPresente(false);
			eventoPessoaDao.salvar(evp);
			evento.getEventoPessoas().add(evp);
		}
		

	}
	
	public void removerPessoa() {
		
		if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP) || pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_COORD)){
			eventoPessoaDao.remover(eventoPessoa);
			evento.getEventoPessoas().remove(eventoPessoa);
		}
		else if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN)){
			List<EventoPessoa> lista = eventoPessoaDao.pesquisarPorEvento(evento);
			for(int i = 0; i < lista.size(); ++i){
				eventoPessoaDao.remover(lista.get(i));;
				evento.getEventoPessoas().remove(lista.get(i));
			}
			
		}
		eventoDao.remover(evento);
	}
	

	

	public EventoPessoa getEventoPessoa() {
		return eventoPessoa;
	}

	public void setEventoPessoa(EventoPessoa eventoPessoa) {
		this.eventoPessoa = eventoPessoa;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public List<Evento> getEventoList() {
		return eventoList;
	}

	public void setEventoList(List<Evento> eventoList) {
		this.eventoList = eventoList;
	}

	public EventoDao getEventoDao() {
		return eventoDao;
	}

	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}

	public List<Evento> getEventoFiltered() {
		return eventoFiltered;
	}

	public void setEventoFiltered(List<Evento> eventoFiltered) {
		this.eventoFiltered = eventoFiltered;
	}

	public EventoLazyDataModel getEventoLazyDataModel() {
		return eventoLazyDataModel;
	}

	public void setEventoLazyDataModel(EventoLazyDataModel eventoLazyDataModel) {
		this.eventoLazyDataModel = eventoLazyDataModel;
	}

	public List<Campus> getListaCampus() {
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public List<Modalidade> getListaModalidade() {
		return listaModalidade;
	}

	public void setListaModalidade(List<Modalidade> listaModalidade) {
		this.listaModalidade = listaModalidade;
	}

	public List<Estudante> getEstudantes() {
		estudantes = estudanteDao.listarPorCampusModalidade(campus, modalidade);
		return estudantes;
	}

	public void setEstudantes(List<Estudante> estudantes) {
		this.estudantes = estudantes;
	}

	public List<Estudante> getEstudantesSelecionados() {
		return estudantesSelecionados;
	}

	public void setEstudantesSelecionados(List<Estudante> estudantesSelecionados) {
		this.estudantesSelecionados = estudantesSelecionados;
	}

	public EventoPessoaDao getEventoPessoaDao() {
		return eventoPessoaDao;
	}

	public void setEventoPessoaDao(EventoPessoaDao eventoPessoaDao) {
		this.eventoPessoaDao = eventoPessoaDao;
	}

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	
	
	public boolean isisPrimeiraTab() {
		return isPrimeiraTab;
	}

	public void setisPrimeiraTab(boolean isPrimeiraTab) {
		this.isPrimeiraTab = isPrimeiraTab;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}

	public LoginControllerMB getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginControllerMB loginController) {
		this.loginController = loginController;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isAdm() {
		return isAdm;
	}

	public void setAdm(boolean isAdm) {
		this.isAdm = isAdm;
	}
	
	public TipoPessoa getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = TipoPessoa.valueOf(role);
	}
	

	public TipoEvento getTipo() {
		return tipoEvento;
	}

	public void setTipo(String tipo) {
		
		this.tipoEvento = TipoEvento.valueOf(tipo);
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}
	
	public TipoPessoa[] getTiposPessoa() {
		return TipoPessoa.values();
	}
	

	public TipoEvento[] getTiposEvento() {
		return TipoEvento.values();
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public void setRole(TipoPessoa role) {
		this.role = role;
	}
	
	
	
	
	
}