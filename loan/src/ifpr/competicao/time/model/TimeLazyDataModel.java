package ifpr.competicao.time.model;

import ifpr.competicao.time.Time;
import ifpr.competicao.time.dao.TimeDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "timeLazyDataModel")
@ViewScoped
public class TimeLazyDataModel extends LazyDataModel<Time> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;

	public TimeLazyDataModel() {

	}

	@Override
	public Time getRowData(String rowKey) {
		return timeDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Time time) {
		return time.getId();
	}

	@Override
	public List<Time> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Time> source = null;

		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = timeDao.findByNome(nomePesquisa);
		} else {
			source = timeDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyTimeSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(timeDao.getRowCount());

		return source;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
	}
	
	

}
