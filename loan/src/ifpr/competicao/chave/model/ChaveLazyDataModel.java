package ifpr.competicao.chave.model;

import ifpr.competicao.chave.Chave;
import ifpr.competicao.chave.dao.ChaveDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "chaveLazyDataModel")
@ViewScoped
public class ChaveLazyDataModel extends LazyDataModel<Chave> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{chaveDao}")
	private ChaveDao chaveDao;

	public ChaveLazyDataModel() {

	}

	@Override
	public Chave getRowData(String rowKey) {
		return chaveDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Chave campus) {
		return campus.getId();
	}

	@Override
	public List<Chave> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Chave> source = null;

		source = chaveDao.list(first, pageSize);

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyChaveSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(chaveDao.getRowCount());

		return source;
	}

	public ChaveDao getChaveDao() {
		return chaveDao;
	}

	public void setChaveDao(ChaveDao chaveDao) {
		this.chaveDao = chaveDao;
	}

}
