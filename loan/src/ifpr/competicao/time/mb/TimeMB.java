package ifpr.competicao.time.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.competicao.time.Time;
import ifpr.competicao.time.dao.TimeDao;
import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.competicao.time.estudante.dao.TimeEstudanteDao;
import ifpr.competicao.time.model.TimeLazyDataModel;
import ifpr.competicao.time.pontos.dao.PontosTimeDao;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.pessoa.tecnicoEsportivo.dao.TecnicoEsportivoDao;

@ManagedBean(name = "timeMB")
@ViewScoped
public class TimeMB {

	private Time time;

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	@ManagedProperty(value = "#{pontosTimeDao}")
	private PontosTimeDao pontosTimeDao;

	@ManagedProperty(value = "#{timeLazyDataModel}")
	private TimeLazyDataModel timeLazyDataModel;

	private List<Time> timeFiltered;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	@ManagedProperty(value = "#{tecnicoEsportivoDao}")
	private TecnicoEsportivoDao tecEspDao;

	@ManagedProperty(value = "#{coordenadorDao}")
	private CoordenadorDao coordDao;

	private List<Campus> listaCampus;

	private Campus campus;

	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;

	private List<Modalidade> listaModalidade;

	private Modalidade modalidade;

	private List<Pessoa> listaTecEsportivo;

	private Pessoa tecEsportivo;

	private List<Estudante> listaEstudante;

	private Estudante estudante;

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	@ManagedProperty(value = "#{timeEstudanteDao}")
	private TimeEstudanteDao timeEstudanteDao;

	private TimeEstudante timeEstudante;

	private Time novoTime;

	private Integer anoOld;

	public TimeMB() {

		timeFiltered = new ArrayList<Time>();
	}

	public void criar() {
		time = new Time();
		campus = null;
		modalidade = null;
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
		listaModalidade = modalidadeDao.listarAlfabetica();
	}

	public void remover() {

		try {
			timeDao.remover(time);
		} catch (Exception ex) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"Time em competição não pode ser apagado!");
			context.addMessage("Atenção", message);

		}
		time = null;
	}

	public void cancelar() {
		removerRegistrosRelacionamentos();
		time = null;
		campus = null;
		modalidade = null;
	}

	private void removerRegistrosRelacionamentos() {
		try {
			for (TimeEstudante timeEstudante : time.getTimeEstudante()) {
				if (timeEstudante.getTime() == null) {
					timeEstudanteDao.remover(timeEstudante);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cancelarEstudante() {
		timeEstudante = null;
	}

	public void salvar() {
		if (time.getId() != null) {
			if (anoOld != time.getAno()) {
				novoTime = new Time();
				novoTime.setAno(time.getAno());
				novoTime.setCampus(time.getCampus());
				novoTime.setModalidade(time.getModalidade());
				novoTime.setNome(time.getNome());
				novoTime.setTecnico(time.getTecnico());
				resalvarEstudantesTime(time.getTimeEstudante());
				timeDao.salvar(time);
			} else {
				timeDao.update(time);
			}
		} else {
			time.setCampus(campus);
			time.setModalidade(modalidade);
			time.setTecnico(tecEsportivo);
			timeDao.salvar(time);
		}

	}

	private void resalvarEstudantesTime(List<TimeEstudante> timeEstudantes) {
		for (TimeEstudante timeEstudante : timeEstudantes) {
			TimeEstudante novoTimeEstud = new TimeEstudante();
			novoTimeEstud.setEstudante(timeEstudante.getEstudante());
			timeEstudanteDao.salvar(novoTimeEstud);
			novoTime.getTimeEstudante().add(novoTimeEstud);
		}
	}

	public void adicionarEstudante() {
		if (estudante != null && estudante.getId() != null) {
			TimeEstudante timeEstd = new TimeEstudante();
			timeEstd.setEstudante(estudante);
			timeEstudanteDao.salvar(timeEstd);
			time.getTimeEstudante().add(timeEstd);
			estudante = new Estudante();
		}
	}

	public void removerEstudante() {
		try {
			timeEstudanteDao.remover(timeEstudante);
		} catch (Exception e) {

			e.printStackTrace();
		}
		time.getTimeEstudante().remove(timeEstudante);
		timeEstudante = null;
	}

	public void preencherListaTec() {
		pegarListas(campus);
	}

	public void pegarListas(Campus campus) {
		listaTecEsportivo = tecEspDao.pesquisarPorCampus(campus);
		listaTecEsportivo.addAll(coordDao.pesquisarTecCoordPorCampus(campus));

	}

	public String confirmaExclusao() {
		RequestContext.getCurrentInstance().execute("PF('timeEstConfirmDialog').show()");
		return "";
	}

	public List<Estudante> pesquisarEstudanteNome(String nome) {
		if (time.getId() != null) {
			listaEstudante = estudanteDao.pesquisarEstudanteNomeCampusTime(nome, campus, time);
		} else {
			listaEstudante = estudanteDao.pesquisarEstudanteNomeCampus(nome, campus);
		}
		return listaEstudante;
	}

	public void onItemSelect(SelectEvent event) {
		Object item = event.getObject();
		estudante = (Estudante) item;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		pegarListas(time.getCampus());
		campus = time.getCampus();
		modalidade = time.getModalidade();
		tecEsportivo = time.getTecnico();
		anoOld = time.getAno();
		this.time = time;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
	}

	public TimeLazyDataModel getTimeLazyDataModel() {
		return timeLazyDataModel;
	}

	public void setTimeLazyDataModel(TimeLazyDataModel timeLazyDataModel) {
		this.timeLazyDataModel = timeLazyDataModel;
	}

	public List<Time> getTimeFiltered() {
		return timeFiltered;
	}

	public void setTimeFiltered(List<Time> timeFiltered) {
		this.timeFiltered = timeFiltered;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
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

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	public List<Modalidade> getListaModalidade() {
		return listaModalidade;
	}

	public List<Pessoa> getListaTecEsportivo() {
		return listaTecEsportivo;
	}

	public void setListaTecEsportivo(List<Pessoa> listaTecEsportivo) {
		this.listaTecEsportivo = listaTecEsportivo;
	}

	public void setListaModalidade(List<Modalidade> listaModalidade) {
		this.listaModalidade = listaModalidade;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}

	public List<Estudante> getListaEstudante() {
		return listaEstudante;
	}

	public void setListaEstudante(List<Estudante> listaEstudante) {
		this.listaEstudante = listaEstudante;
	}

	public TimeEstudanteDao getTimeEstudanteDao() {
		return timeEstudanteDao;
	}

	public void setTimeEstudanteDao(TimeEstudanteDao timeEstudanteDao) {
		this.timeEstudanteDao = timeEstudanteDao;
	}

	public TimeEstudante getTimeEstudante() {
		return timeEstudante;
	}

	public void setTimeEstudante(TimeEstudante timeEstudante) {
		this.timeEstudante = timeEstudante;
	}

	public Pessoa getTecEsportivo() {
		return tecEsportivo;
	}

	public void setTecEsportivo(Pessoa tecEsportivo) {
		this.tecEsportivo = tecEsportivo;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public PontosTimeDao getPontosTimeDao() {
		return pontosTimeDao;
	}

	public void setPontosTimeDao(PontosTimeDao pontosTimeDao) {
		this.pontosTimeDao = pontosTimeDao;
	}

	public int getAnoOld() {
		return anoOld;
	}

	public void setAnoOld(int anoOld) {
		this.anoOld = anoOld;
	}

	public TecnicoEsportivoDao getTecEspDao() {
		return tecEspDao;
	}

	public void setTecEspDao(TecnicoEsportivoDao tecEspDao) {
		this.tecEspDao = tecEspDao;
	}

	public CoordenadorDao getCoordDao() {
		return coordDao;
	}

	public void setCoordDao(CoordenadorDao coordDao) {
		this.coordDao = coordDao;
	}

}
