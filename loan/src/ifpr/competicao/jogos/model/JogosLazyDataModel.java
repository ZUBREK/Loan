package ifpr.competicao.jogos.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ifpr.competicao.jogos.Jogos;
import ifpr.competicao.jogos.dao.JogosDao;

@ManagedBean(name = "jogosLazyDataModel")
@ViewScoped
public class JogosLazyDataModel extends LazyDataModel<Jogos> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{jogosDao}")
	private JogosDao jogosDao;

	public JogosLazyDataModel() {

	}

	@Override
	public Jogos getRowData(String rowKey) {
		return jogosDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Jogos campus) {
		return campus.getId();
	}

	@Override
	public List<Jogos> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Jogos> source = null;

		source = jogosDao.list(first, pageSize);

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyJogosSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(jogosDao.getRowCount());

		return source;
	}

	public JogosDao getJogosDao() {
		return jogosDao;
	}

	public void setJogosDao(JogosDao jogosDao) {
		this.jogosDao = jogosDao;
	}

}
