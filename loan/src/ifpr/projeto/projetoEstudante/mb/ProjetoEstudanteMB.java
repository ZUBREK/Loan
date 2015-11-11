package ifpr.projeto.projetoEstudante.mb;

import ifpr.pessoa.estudante.Estudante;
import ifpr.projeto.Projeto;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;
import ifpr.projeto.projetoEstudante.dao.ProjetoEstudanteDao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "projetoEstudanteMB")
@ViewScoped
public class ProjetoEstudanteMB {

	private ProjetoEstudante projetoEstudante;

	@ManagedProperty(value = "#{projetoEstudanteDao}")
	private ProjetoEstudanteDao projetoEstudanteDao;

	private List<ProjetoEstudante> projetoEstudanteFiltered;

	private Estudante estudante;
	private Projeto projeto;

	public ProjetoEstudanteMB() {

		projetoEstudanteFiltered = new ArrayList<ProjetoEstudante>();
	}

	@PostConstruct
	public void poust() {
	}

	public void remover() {
		projetoEstudanteDao.remover(projetoEstudante);
	}

	public void cancelar() {
		projetoEstudante = null;
	}

	public void salvar() {
		if (projetoEstudante.getId() != null) {
			projetoEstudanteDao.update(projetoEstudante);
		} else {
			projetoEstudante.setEstudante(estudante);
			projetoEstudante.setProjeto(projeto);
			projetoEstudanteDao.salvar(projetoEstudante);
		}
	}
	
	public ProjetoEstudante getProjetoEstudante() {
		return projetoEstudante;
	}

	public void setProjetoEstudante(ProjetoEstudante projetoEstudante) {
		this.projetoEstudante = projetoEstudante;
	}

	public ProjetoEstudanteDao getProjetoEstudanteDao() {
		return projetoEstudanteDao;
	}

	public void setProjetoEstudanteDao(ProjetoEstudanteDao projetoEstudanteDao) {
		this.projetoEstudanteDao = projetoEstudanteDao;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<ProjetoEstudante> getProjetoEstudanteFiltered() {
		return projetoEstudanteFiltered;
	}

	public void setProjetoEstudanteFiltered(
			List<ProjetoEstudante> timeEstudanteFiltered) {
		this.projetoEstudanteFiltered = timeEstudanteFiltered;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

}
