package ifpr.competicao.partida.local.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ifpr.competicao.partida.local.Local;
import ifpr.competicao.partida.local.dao.LocalDao;

@ManagedBean(name = "localLazyDataModel")
@ViewScoped
public class LocalLazyDataModel extends LazyDataModel<Local> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	public LocalLazyDataModel() {

	}

	@Override
	public Local getRowData(String rowKey) {
		return localDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Local campus) {
		return campus.getId();
	}

	@Override
	public List<Local> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<Local> source = null;

		source = localDao.list(first, pageSize);

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyLocalSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(localDao.getRowCount());

		return source;
	}

	public LocalDao getLocalDao() {
		return localDao;
	}

	public void setLocalDao(LocalDao localDao) {
		this.localDao = localDao;
	}
}
