package ifpr.evento.mb;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.evento.Evento;
import ifpr.evento.TipoEvento;
import ifpr.evento.arquivo.ArquivoEvento;
import ifpr.evento.arquivo.dao.ArquivoEventoDao;
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
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

	@ManagedProperty(value = "#{arquivoEventoDao}")
	private ArquivoEventoDao arquivoEventoDao;

	private StreamedContent arqStreamed;

	private List<ArquivoEvento> arquivos;

	private ArquivoEvento arquivoEvento;

	private List<Campus> listaCampus;

	private Campus campus;

	private List<Modalidade> listaModalidade;

	private Modalidade modalidade;

	private List<Estudante> estudantes;

	private List<Estudante> estudantesSelecionados;

	private List<Pessoa> pessoasSelecionadas;

	private EventoPessoa eventoPessoa;

	private TipoPessoa tipoPessoaSelecionado;

	private TipoEvento tipoEventoSelecionado;

	private boolean isUpdate;

	private Pessoa pessoaLogada;

	private boolean isTecAdm;

	private boolean isAdm;

	private boolean isTecEsp;

	private final String CAMINHO_ARQUIVO_EVENTO = "C:/home/loan_docs/evento";

	public EventoMB() {
		eventoFiltered = new ArrayList<Evento>();
		isAdm = false;
		isUpdate = true;
		isTecAdm = false;
		isTecEsp = false;
		tipoEventoSelecionado = TipoEvento.REFEICAO;
		tipoPessoaSelecionado = TipoPessoa.ROLE_ADMIN;
		estudantesSelecionados = new ArrayList<Estudante>();
		pessoasSelecionadas = new ArrayList<Pessoa>();
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
		else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)
				|| pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_COORD))
			isTecEsp = true;
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

	public void cancelarEventoAdm() {
		if (isUpdate == true) {
			evento = null;
		} else {
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
		isUpdate = true;

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

	public void salvarEventoRef() {

		if (evento.getId() != null) {
			eventoDao.update(evento);
		} else {
			evento.setTipo(TipoEvento.REFEICAO);
			evento.setResponsavel(pessoaLogada);
			eventoDao.salvar(evento);
		}
	}

	public void salvarEvtArquivo() {
		if (evento.getId() != null) {
			eventoDao.update(evento);
		} else {
			evento.setTipo(TipoEvento.MAPAMODALIDADE);
			evento.setDataHoraInicio(new Date());
			evento.setResponsavel(pessoaLogada);
			eventoDao.salvar(evento);
		}
	}

	public void removerPessoa() {
		eventoPessoaDao.remover(eventoPessoa);
		evento.getEventoPessoas().remove(eventoPessoa);
		eventoDao.remover(evento);
	}

	public String popularArquivos() {
		arquivos = arquivoEventoDao.pesquisarPorEvento(evento);
		return "";
	}

	public String procurarArquivo() {
		try {
			eventoPessoa = eventoPessoaDao.pesquisarPorEventoPessoa(evento,
					pessoaLogada);
			arquivoEvento = arquivoEventoDao
					.pesquisarPorEventoPessoa(eventoPessoa);
		} catch (Exception nr) {

		}
		return "";
	}

	public void handleFileUpload(FileUploadEvent event) {
		if (eventoPessoa == null) {
			eventoPessoa = new EventoPessoa();
			eventoPessoa.setEvento(evento);
			eventoPessoa.setPessoa(pessoaLogada);
			eventoPessoa.setDataHora(new Date());
			eventoPessoaDao.salvar(eventoPessoa);

		}
		if (arquivoEvento == null) {
			arquivoEvento = new ArquivoEvento();
		}
		try {
			TecnicoAdministrativo tecAdm = (TecnicoAdministrativo) pessoaLogada;
			String nomeArquivoStreamed = tecAdm.getCampus().toString() + "-"
					+ event.getFile().getFileName();
			byte[] arquivoByte = event.getFile().getContents();
			String caminho = CAMINHO_ARQUIVO_EVENTO
					+ "/"
					+ pessoaLogada.getId()
					+ nomeArquivoStreamed.substring(
							nomeArquivoStreamed.lastIndexOf('.'),
							nomeArquivoStreamed.length());
			criarArquivoDisco(arquivoByte, caminho);
			arquivoEvento.setEventoPessoa(eventoPessoa);
			arquivoEvento.setUploader(pessoaLogada);
			arquivoEvento.setCaminho(caminho);
			arquivoEvento.setNome(nomeArquivoStreamed);
			if (arquivoEvento.getId() != null) {
				arquivoEventoDao.update(arquivoEvento);
			} else {
				arquivoEventoDao.salvar(arquivoEvento);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return;
	}

	public void apagarArquivo() {
		if (arquivoEvento != null) {
			File file = new File(arquivoEvento.getCaminho());
			file.delete();

			arquivoEventoDao.remover(arquivoEvento);
			arquivoEvento = null;
		}
		return;
	}

	private void criarArquivoDisco(byte[] bytes, String arquivoPath)
			throws IOException {
		File file = new File(CAMINHO_ARQUIVO_EVENTO);
		file.mkdirs();
		FileOutputStream fos;
		fos = new FileOutputStream(arquivoPath);
		fos.write(bytes);
		fos.close();
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(arquivoEvento.getCaminho());
			arqStreamed = new DefaultStreamedContent(stream, null,
					arquivoEvento.getNome());
		} catch (FileNotFoundException e) {
			System.out.println("Erro no download do arquivo");
		}

		return arqStreamed;
	}

	public void abrirDialog() {
		if (evento != null) {
			if (evento.getTipo().equals(TipoEvento.MAPAMODALIDADE)){
				popularArquivos();
				RequestContext.getCurrentInstance().execute(
						"PF('visEventoMapaDialog').show()");
			}
			else
				RequestContext.getCurrentInstance().execute(
						"PF('visEventoDialog').show()");
		}
	}
	
	public void abrirEditDialog(){
		
		RequestContext.getCurrentInstance().execute(
				"PF('eventoAdmDialog').show()");
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
		if (tipoEventoSelecionado.equals(TipoEvento.MAPAMODALIDADE)) {
			lista = new TipoPessoa[] { TipoPessoa.ROLE_TEC_ADM };
		} else {
			lista = new TipoPessoa[] { TipoPessoa.ROLE_ESTUDANTE };
		}

		return lista;
	}

	public TipoEvento[] getTiposEvento() {

		return TipoEvento.values();
	}

	public TipoEvento getTipoEventoSelecionado() {
		return tipoEventoSelecionado;
	}

	public void setTipoEventoSelecionado(String tipoEvento) {
		this.tipoEventoSelecionado = TipoEvento.valueOf(tipoEvento);
	}

	public boolean isTecAdm() {
		return isTecAdm;
	}

	public void setTecAdm(boolean isTecAdm) {
		this.isTecAdm = isTecAdm;
	}

	public TipoPessoa getTipoPessoaSelecionado() {
		return tipoPessoaSelecionado;
	}

	public void setTipoPessoaSelecionado(TipoPessoa tipoPessoa) {
		this.tipoPessoaSelecionado = tipoPessoa;
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
		return pessoaDao.findByRole(tipoPessoaSelecionado);
	}

	public boolean isTecEsp() {
		return isTecEsp;
	}

	public void setTecEsp(boolean isTecEsp) {
		this.isTecEsp = isTecEsp;
	}

	public void setTipoEventoSelecionado(TipoEvento tipoEventoSelecionado) {
		this.tipoEventoSelecionado = tipoEventoSelecionado;
	}

	public ArquivoEventoDao getArquivoEventoDao() {
		return arquivoEventoDao;
	}

	public void setArquivoEventoDao(ArquivoEventoDao arquivoEventoDao) {
		this.arquivoEventoDao = arquivoEventoDao;
	}

	public ArquivoEvento getArquivoEvento() {
		return arquivoEvento;
	}

	public void setArquivoEvento(ArquivoEvento arquivoEvento) {
		this.arquivoEvento = arquivoEvento;
	}

	public List<ArquivoEvento> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<ArquivoEvento> arquivos) {
		this.arquivos = arquivos;
	}

}