package ifpr.competicao.time.estudante.mb;

import ifpr.competicao.time.Time;
import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.competicao.time.estudante.dao.TimeEstudanteDao;
import ifpr.modalidade.Modalidade;
import ifpr.pessoa.estudante.Estudante;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "timeEstudanteMB")
@ViewScoped
public class TimeEstudanteMB {

	private TimeEstudante timeEstudante;

	@ManagedProperty(value = "#{timeEstudanteDao}")
	private TimeEstudanteDao timeEstudanteDao;

	private List<TimeEstudante> timeEstudanteFiltered;

	private Estudante estudante;
	private Time time;
	private Modalidade modalidade;

	public TimeEstudanteMB() {

		timeEstudanteFiltered = new ArrayList<TimeEstudante>();
	}

	public void criar() {
		//timeEstudante = new TimeEstudante();

	}

	@PostConstruct
	public void poust() {
	}

	public void remover() {
		timeEstudanteDao.remover(timeEstudante);
	}

	public void cancelar() {
		timeEstudante = null;
	}

	public void salvar() {
		if (timeEstudante.getId() != null) {
			timeEstudanteDao.update(timeEstudante);
		} else {
			timeEstudante.setEstudante(estudante);
			timeEstudante.setTime(time);
			timeEstudanteDao.salvar(timeEstudante);
		}
	}

	public TimeEstudante getTimeEstudante() {
		return timeEstudante;
	}

	public void setTimeEstudante(TimeEstudante timeEstudante) {
		this.timeEstudante = timeEstudante;
	}

	public TimeEstudanteDao getTimeEstudanteDao() {
		return timeEstudanteDao;
	}

	public void setTimeEstudanteDao(TimeEstudanteDao timeEstudanteDao) {
		this.timeEstudanteDao = timeEstudanteDao;
	}

	public List<TimeEstudante> getTimeEstudanteFiltered() {
		return timeEstudanteFiltered;
	}

	public void setTimeEstudanteFiltered(
			List<TimeEstudante> timeEstudanteFiltered) {
		this.timeEstudanteFiltered = timeEstudanteFiltered;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

}
