package ifpr.projeto.model;

import ifpr.projeto.Projeto;
import ifpr.projeto.dao.ProjetoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "projetoLazyDataModel")
@ViewScoped
public class ProjetoLazyDataModel extends LazyDataModel<Projeto> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{projetoDao}")
	private ProjetoDao projetoDao;

	public ProjetoLazyDataModel() {

	}

	@Override
	public Projeto getRowData(String rowKey) {
		return projetoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Projeto projeto) {
		return projeto.getId();
	}

	@Override
	public List<Projeto> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Projeto> source = null;

		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = projetoDao.findByNome(nomePesquisa);
		} else {
			source = projetoDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyProjetoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(projetoDao.getRowCount());

		return source;
	}

	public ProjetoDao getProjetoDao() {
		return projetoDao;
	}

	public void setProjetoDao(ProjetoDao projetoDao) {
		this.projetoDao = projetoDao;
	}
	
	

}
