package ifpr.competicao.jogos.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.competicao.jogos.Jogos;
import ifpr.competicao.jogos.dao.JogosDao;
import ifpr.competicao.jogos.jogosCampus.JogosCampus;
import ifpr.competicao.jogos.jogosCampus.dao.JogosCampusDao;
import ifpr.competicao.jogos.jogosModalidade.JogosModalidade;
import ifpr.competicao.jogos.jogosModalidade.dao.JogosModalidadeDao;
import ifpr.competicao.jogos.jogosTime.JogosTime;
import ifpr.competicao.jogos.jogosTime.dao.JogosTimeDao;
import ifpr.competicao.jogos.model.JogosLazyDataModel;
import ifpr.competicao.time.Time;
import ifpr.competicao.time.dao.TimeDao;
import ifpr.geradorPdf.RelatorioFinal;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;

@ManagedBean(name = "jogosMB")
@ViewScoped
public class JogosMB {

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;

	@ManagedProperty(value = "#{jogosTimeDao}")
	private JogosTimeDao jogosTimeDao;

	@ManagedProperty(value = "#{jogosModalidadeDao}")
	private JogosModalidadeDao jogosModalidadeDao;

	@ManagedProperty(value = "#{jogosCampusDao}")
	private JogosCampusDao jogosCampusDao;

	private Jogos jogos;

	private List<Jogos> jogosList;

	@ManagedProperty(value = "#{jogosDao}")
	private JogosDao jogosDao;

	private List<Jogos> jogosFiltered;

	@ManagedProperty(value = "#{jogosLazyDataModel}")
	private JogosLazyDataModel jogosLazyDataModel;

	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	private List<Campus> listaCampus;

	private List<Modalidade> listaModalidade;

	private List<Time> times;

	private List<Time> timesSelecionados;

	private List<Modalidade> modalidadesSelecionadas;

	private List<Campus> campusSelecionados;

	private JogosCampus jogosCampus;

	private JogosModalidade jogosModalidade;

	private JogosTime jogosTime;
	
	@ManagedProperty(value = "#{relatorioFinal}")
	public RelatorioFinal relatorioFinal;

	public JogosMB() {
		jogosFiltered = new ArrayList<Jogos>();
		timesSelecionados = new ArrayList<>();
		campusSelecionados = new ArrayList<>();
		modalidadesSelecionadas = new ArrayList<>();
	}

	public void criar() {
		jogos = new Jogos();
	}

	@PostConstruct
	public void poust() {

		listaCampus = campusDao.listarAlfabetica();
		listaModalidade = modalidadeDao.listarAlfabetica();
	}

	public void cancelar() {

		jogos = null;

	}

	public void adicionarTime() {

		JogosTime jogosTime;
		for (Time time : timesSelecionados) {
			jogosTime = new JogosTime();
			jogosTime.setTime(time);
			jogosTimeDao.salvar(jogosTime);
			jogos.getJogosTimes().add(jogosTime);
		}

	}

	public void adicionarCampus() {

		JogosCampus jogosCampus;
		for (Campus campus : campusSelecionados) {
			jogosCampus = new JogosCampus();
			jogosCampus.setCampus(campus);
			jogosCampusDao.salvar(jogosCampus);
			jogos.getJogosCampus().add(jogosCampus);
		}

	}

	public void adicionarModalidade() {

		JogosModalidade jogosModalidade;
		for (Modalidade modalidade : modalidadesSelecionadas) {
			jogosModalidade = new JogosModalidade();
			jogosModalidade.setModalidade(modalidade);
			jogosModalidadeDao.salvar(jogosModalidade);
			jogos.getJogosModalidades().add(jogosModalidade);
		}

	}

	public void salvarJogos() {

		if (jogos.getId() != null) {
			jogosDao.update(jogos);
			RequestContext.getCurrentInstance().execute("PF('jogosDialog').hide()");
		} else {
			if (!jogosDao.existeJogosAno(jogos.getAno())) {
				jogosDao.salvar(jogos);
				RequestContext.getCurrentInstance().execute("PF('jogosDialog').hide()");
			} else {
				mensagemErroFaces("Erro!", "Já existe um jogo com esse ano!");
			}

		}

	}

	private void mensagemErroFaces(String titulo, String messagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, messagem);

		context.addMessage("Atenção", message);
		context.validationFailed();
	}

	public void removerJogos() {

		try {
			jogosDao.remover(jogos);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Impossível remover Jogos!");
		}

	}

	public void removerCampus() {

		try {

			jogosCampusDao.remover(jogosCampus);
			jogos.getJogosCampus().remove(jogosCampus);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Impossível remover Campus!");
		}

	}

	public void removerModalidade() {

		try {

			jogosModalidadeDao.remover(jogosModalidade);
			jogos.getJogosModalidades().remove(jogosModalidade);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Impossível remover Modalidade!");
		}

	}

	public void removerTime() {

		try {
			jogosTimeDao.remover(jogosTime);
			jogos.getJogosTimes().remove(jogosTime);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Impossível remover Time!");
		}

	}

	public void mensagemAvisoFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, message));
	}
	
	public void gerarRelatorio(Jogos jogos) {
		relatorioFinal.gerarRelatorio(jogos);
	}

	public List<Campus> getListaCampus() {
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public List<Modalidade> getListaModalidade() {
		return listaModalidade;
	}

	public void setListaModalidade(List<Modalidade> listaModalidade) {
		this.listaModalidade = listaModalidade;
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

	public Jogos getJogos() {
		return jogos;
	}

	public void setJogos(Jogos jogos) {
		modalidadesSelecionadas.clear();
		for (JogosModalidade evPess : jogos.getJogosModalidades()) {
			modalidadesSelecionadas.add((Modalidade) evPess.getModalidade());
		}
		campusSelecionados.clear();
		for (JogosCampus evPess : jogos.getJogosCampus()) {
			campusSelecionados.add((Campus) evPess.getCampus());
		}
		timesSelecionados.clear();
		for (JogosTime evPess : jogos.getJogosTimes()) {
			timesSelecionados.add((Time) evPess.getTime());
		}
		this.jogos = jogos;
	}

	public JogosDao getJogosDao() {
		return jogosDao;
	}

	public void setJogosDao(JogosDao jogosDao) {
		this.jogosDao = jogosDao;
	}

	public JogosLazyDataModel getJogosLazyDataModel() {
		return jogosLazyDataModel;
	}

	public void setJogosLazyDataModel(JogosLazyDataModel jogosLazyDataModel) {
		this.jogosLazyDataModel = jogosLazyDataModel;
	}

	public List<Jogos> getJogosList() {
		return jogosList;
	}

	public void setJogosList(List<Jogos> jogosList) {
		this.jogosList = jogosList;
	}

	public List<Jogos> getJogosFiltered() {
		return jogosFiltered;
	}

	public void setJogosFiltered(List<Jogos> jogosFiltered) {
		this.jogosFiltered = jogosFiltered;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
	}

	public List<Time> getTimes() {
		if (getIsSelecionavel()) {
			if (times == null) {
				times = new ArrayList<>();
			}
			times.clear();
			for (JogosModalidade modalidade : jogos.getJogosModalidades()) {
				for (JogosCampus campus2 : jogos.getJogosCampus()) {
					times.addAll(timeDao.listarTimesPorCampusModalidade(campus2.getCampus(), modalidade.getModalidade()));
				}
			}
		}
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public List<Time> getTimesSelecionados() {
		return timesSelecionados;
	}

	public void setTimesSelecionados(List<Time> timesSelecionados) {
		this.timesSelecionados = timesSelecionados;
	}

	public JogosTimeDao getJogosTimeDao() {
		return jogosTimeDao;
	}

	public void setJogosTimeDao(JogosTimeDao jogosTimeDao) {
		this.jogosTimeDao = jogosTimeDao;
	}

	public JogosModalidadeDao getJogosModalidadeDao() {
		return jogosModalidadeDao;
	}

	public void setJogosModalidadeDao(JogosModalidadeDao jogosModalidadeDao) {
		this.jogosModalidadeDao = jogosModalidadeDao;
	}

	public JogosCampusDao getJogosCampusDao() {
		return jogosCampusDao;
	}

	public void setJogosCampusDao(JogosCampusDao jogosCampusDao) {
		this.jogosCampusDao = jogosCampusDao;
	}

	public boolean getIsSelecionavel() {
		if (jogos != null && (!jogos.getJogosModalidades().isEmpty() && !jogos.getJogosCampus().isEmpty())) {
			return true;
		}
		return false;
	}

	public List<Modalidade> getModalidadesSelecionadas() {
		return modalidadesSelecionadas;
	}

	public void setModalidadesSelecionadas(List<Modalidade> modalidadesSelecionadas) {
		this.modalidadesSelecionadas = modalidadesSelecionadas;
	}

	public List<Campus> getCampusSelecionados() {
		return campusSelecionados;
	}

	public void setCampusSelecionados(List<Campus> campusSelecionados) {
		this.campusSelecionados = campusSelecionados;
	}

	public JogosCampus getJogosCampus() {
		return jogosCampus;
	}

	public void setJogosCampus(JogosCampus jogosCampus) {
		this.jogosCampus = jogosCampus;
	}

	public JogosModalidade getJogosModalidade() {
		return jogosModalidade;
	}

	public void setJogosModalidade(JogosModalidade jogosModalidade) {
		this.jogosModalidade = jogosModalidade;
	}

	public JogosTime getJogosTime() {
		return jogosTime;
	}

	public void setJogosTime(JogosTime jogosTime) {
		this.jogosTime = jogosTime;
	}

	public RelatorioFinal getRelatorioFinal() {
		return relatorioFinal;
	}

	public void setRelatorioFinal(RelatorioFinal relatorioFinal) {
		this.relatorioFinal = relatorioFinal;
	}
	
}