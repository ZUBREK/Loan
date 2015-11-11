package ifpr.competicao.chave.mb;

import ifpr.competicao.chave.Chave;
import ifpr.competicao.chave.dao.ChaveDao;
import ifpr.competicao.chave.model.ChaveLazyDataModel;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.ConstraintViolationException;

@ManagedBean(name = "chaveMB")
@ViewScoped
public class ChaveMB {

	private Chave chave;

	private List<Chave> chaveList;

	@ManagedProperty(value = "#{chaveDao}")
	private ChaveDao chaveDao;

	@ManagedProperty(value = "#{chaveLazyDataModel}")
	private ChaveLazyDataModel chaveLazyDataModel;

	public void criar() {
		chave = new Chave();

	}

	public void remover() {
		try {
			chaveDao.remover(chave);
		} catch (ConstraintViolationException e) {
			// facesmessage bagaça
		}

	}

	public void cancelar() {
		chave = null;
	}

	public void salvar() {

		if (chave.getId() != null) {

			chaveDao.update(chave);
		} else {
			chaveDao.salvar(chave);
		}
	}

	public Chave getChave() {
		return chave;
	}

	public void setChave(Chave chave) {
		this.chave = chave;
	}

	public List<Chave> getChaveList() {
		return chaveList;
	}

	public void setChaveList(List<Chave> chaveList) {
		this.chaveList = chaveList;
	}

	public ChaveDao getChaveDao() {
		return chaveDao;
	}

	public void setChaveDao(ChaveDao chaveDao) {
		this.chaveDao = chaveDao;
	}

	public ChaveLazyDataModel getChaveLazyDataModel() {
		return chaveLazyDataModel;
	}

	public void setChaveLazyDataModel(ChaveLazyDataModel chaveLazyDataModel) {
		this.chaveLazyDataModel = chaveLazyDataModel;
	}
	
}
