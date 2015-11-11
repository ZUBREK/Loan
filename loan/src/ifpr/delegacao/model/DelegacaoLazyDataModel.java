package ifpr.delegacao.model;

import ifpr.delegacao.Delegacao;
import ifpr.delegacao.dao.DelegacaoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "delegacaoLazyDataModel")
@ViewScoped
public class DelegacaoLazyDataModel extends LazyDataModel<Delegacao> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{delegacaoDao}")
	private DelegacaoDao delegacaoDao;

	public DelegacaoLazyDataModel() {

	}

	@Override
	public Delegacao getRowData(String rowKey) {
		return delegacaoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Delegacao delegacao) {
		return delegacao.getId();
	}

	@Override
	public List<Delegacao> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Delegacao> source = null;

		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = delegacaoDao.findByNome(nomePesquisa);
		} else {
			source = delegacaoDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyDelegacaoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(delegacaoDao.getRowCount());

		return source;
	}

	public DelegacaoDao getDelegacaoDao() {
		return delegacaoDao;
	}

	public void setDelegacaoDao(DelegacaoDao delegacaoDao) {
		this.delegacaoDao = delegacaoDao;
	}
	
	

}
