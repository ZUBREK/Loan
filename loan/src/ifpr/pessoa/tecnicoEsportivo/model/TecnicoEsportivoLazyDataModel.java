package ifpr.pessoa.tecnicoEsportivo.model;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;
import ifpr.pessoa.tecnicoEsportivo.dao.TecnicoEsportivoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "tecnicoEspLazyDataModel")
@ViewScoped
public class TecnicoEsportivoLazyDataModel extends
		LazyDataModel<TecnicoEsportivo> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{tecnicoEsportivoDao}")
	private TecnicoEsportivoDao tecnicoEspDao;

	public TecnicoEsportivoLazyDataModel() {

	}

	@Override
	public TecnicoEsportivo getRowData(String rowKey) {
		return tecnicoEspDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(TecnicoEsportivo tecnicoEsp) {
		return tecnicoEsp.getId();
	}

	@Override
	public List<TecnicoEsportivo> load(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<TecnicoEsportivo> source = null;

		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = tecnicoEspDao.findByNomeRole(nomePesquisa, TipoPessoa.ROLE_TEC_ESP.toString());
		} else {
			source = tecnicoEspDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyTecnicoEsportivoSorter(sortField,
					sortOrder));
		}

		// rowCount
		this.setRowCount(tecnicoEspDao.getRowCount());

		return source;
	}

	public TecnicoEsportivoDao getTecnicoEspDao() {
		return tecnicoEspDao;
	}

	public void setTecnicoEspDao(TecnicoEsportivoDao tecnicoEspDao) {
		this.tecnicoEspDao = tecnicoEspDao;
	}

}
