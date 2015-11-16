package ifpr.competicao.partida.local.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ifpr.competicao.partida.local.Local;
import ifpr.competicao.partida.local.dao.LocalDao;
import ifpr.competicao.partida.local.model.LocalLazyDataModel;

@ManagedBean(name = "localMB")
@ViewScoped
public class LocalMB {

	private Local local;

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	@ManagedProperty(value = "#{localLazyDataModel}")
	private LocalLazyDataModel localLazyDataModel;

	private List<Local> localFiltered;

	public LocalMB() {
		localFiltered = new ArrayList<Local>();
	}

	public void criar() {
		local = new Local();
	}

	public void remover() {
		localDao.remover(local);
	}

	public void cancelar() {
		local = null;

	}

	public void salvar() {
		if (local.getId() != null) {
			localDao.update(local);
		} else {
			localDao.salvar(local);
		}
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public LocalDao getLocalDao() {
		return localDao;
	}

	public void setLocalDao(LocalDao localDao) {
		this.localDao = localDao;
	}

	public LocalLazyDataModel getLocalLazyDataModel() {
		return localLazyDataModel;
	}

	public void setLocalLazyDataModel(LocalLazyDataModel localLazyDataModel) {
		this.localLazyDataModel = localLazyDataModel;
	}

	public List<Local> getLocalFiltered() {
		return localFiltered;
	}

	public void setLocalFiltered(List<Local> localFiltered) {
		this.localFiltered = localFiltered;
	}

}
