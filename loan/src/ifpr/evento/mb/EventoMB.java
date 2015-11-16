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

	@ManagedProperty(value = "#{pessoaDao}")
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

	private List<Pessoa> pessoas;

	private List<Estudante> estudantesSelecionados;

	private List<Pessoa> pessoasSelecionadas;

	private EventoPessoa eventoPessoa;

	private TipoPessoa tipoPessoa;

	private TipoEvento tipoEvento;

	private boolean isUpdate;

	private Pessoa pessoaLogada;

	private boolean isTecAdm;

	private boolean isAdm;

	private boolean disableTipoPessoa;

	private boolean isAcesso;

	public EventoMB() {
		eventoFiltered = new ArrayList<Evento>();
		isAdm = false;
		isUpdate = true;
		tipoEvento = TipoEvento.REFEICAO;
		tipoPessoa = TipoPessoa.ROLE_ADMIN;
		estudantesSelecionados = new ArrayList<Estudante>();
		pessoasSelecionadas = new ArrayList<Pessoa>();
		pessoas = new ArrayList<Pessoa>();
	}

	public void criar() {
		evento = new Evento();
		isUpdate = false;
		evento.setEventoPessoas(new ArrayList<EventoPessoa>());

	}

	@PostConstruct
	public void poust() {

		listaCampus = campusDao.listarAlfabetica();
		listaModalidade = modalidadeDao.listarAlfabetica();
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(
				context, "#{loginControllerMB}", LoginControllerMB.class);
		pessoaLogada = loginController.getPessoaLogada();
		if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ADM))
			isTecAdm = true;
		else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN))
			isAdm = true;
		disableTipoPessoa = true;
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
	}

	public void salvarTreino() {

		if (evento.getId() != null) {
			eventoDao.update(evento);
		} else {
			evento.setTipo(TipoEvento.TREINO);
			evento.setResponsavel(pessoaLogada);
			eventoDao.salvar(evento);

		}

	}

	public void salvarEventoAdm() {

		evento.setResponsavel(pessoaLogada);
		evento.setTipo(tipoEvento);
		eventoDao.salvar(evento);
		List<Pessoa> pessoas = pessoaDao.findByRole(tipoPessoa);
		EventoPessoa evp;
		for (int i = 0; i < pessoas.size(); ++i) {
			evp = new EventoPessoa();
			evp.setPessoa(pessoas.get(i));
			evp.setEvento(evento);
			eventoPessoaDao.salvar(evp);
			evento.getEventoPessoas().add(evp);
		}
		eventoDao.update(evento);

		disableTipoPessoa = false;
		tipoEvento = TipoEvento.REFEICAO;
		tipoPessoa = TipoPessoa.ROLE_ESTUDANTE;
	}

	public void adicionarEstudante() {
		evento.setResponsavel(pessoaLogada);
		evento.setTipo(TipoEvento.TREINO);
		eventoDao.salvar(evento);
		EventoPessoa evp;
		for (int i = 0; i < estudantesSelecionados.size(); ++i) {
			evp = new EventoPessoa();
			evp.setPessoa(estudantesSelecionados.get(i));
			evp.setEvento(evento);
			evp.setWasPresente(false);
			eventoPessoaDao.salvar(evp);
			evento.getEventoPessoas().add(evp);
		}
		
	}

	public void adicionarPessoas() {

		evento.setResponsavel(pessoaLogada);
		evento.setTipo(tipoEvento);
		eventoDao.salvar(evento);
		EventoPessoa evp;
		for (int i = 0; i < pessoasSelecionadas.size(); ++i) {
			evp = new EventoPessoa();
			evp.setPessoa(pessoasSelecionadas.get(i));
			evp.setEvento(evento);
			eventoPessoaDao.salvar(evp);
			evento.getEventoPessoas().add(evp);
		}

	}

	public void removerPessoa() {
		eventoPessoaDao.remover(eventoPessoa);
		evento.getEventoPessoas().remove(eventoPessoa);
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

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public TipoPessoa[] getTiposPessoa() {
		TipoPessoa[] lista = null;
		if (tipoEvento.equals(TipoEvento.MAPAMODALIDADE)) {
			lista = new TipoPessoa[] { TipoPessoa.ROLE_TEC_ADM };
		} else {
			lista = new TipoPessoa[] { TipoPessoa.ROLE_ESTUDANTE };
			disableTipoPessoa = true;
		}

		return lista;
	}

	public TipoEvento[] getTiposEvento() {

		return TipoEvento.values();
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = TipoEvento.valueOf(tipoEvento);
	}

	public boolean isTecAdm() {
		return isTecAdm;
	}

	public void setTecAdm(boolean isTecAdm) {
		this.isTecAdm = isTecAdm;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public boolean isDisableTipoPessoa() {
		return disableTipoPessoa;
	}

	public void setDisableTipoPessoa(boolean disableTipoPessoa) {
		this.disableTipoPessoa = disableTipoPessoa;
	}

	public boolean isAdm() {
		return isAdm;
	}

	public void setAdm(boolean isAdm) {
		this.isAdm = isAdm;
	}

	public List<Pessoa> getPessoasSelecionadas() {
		return pessoasSelecionadas;
	}

	public void setPessoasSelecionadas(List<Pessoa> pessoasSelecionadas) {
		this.pessoasSelecionadas = pessoasSelecionadas;
	}

	public List<Pessoa> getPessoas() {
		return pessoaDao.findByRole(tipoPessoa);
	}

	public boolean isAcesso() {
		return isAcesso;
	}

	public void setAcesso(boolean isAcesso) {
		this.isAcesso = isAcesso;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

}