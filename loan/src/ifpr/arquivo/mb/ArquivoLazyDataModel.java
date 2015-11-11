package ifpr.arquivo.mb;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "arquivoLazyDataModel")
@ViewScoped
public class ArquivoLazyDataModel extends LazyDataModel<Arquivo> {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	public ArquivoLazyDataModel() {

	}

	

	@Override
	public Arquivo getRowData(String rowKey) {
		return arquivoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Arquivo arquivo) {
		return arquivo.getId();
	}

	@Override
	public List<Arquivo> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Arquivo> source = null;

		source = arquivoDao.list(first, pageSize);

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyArquivoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(arquivoDao.getRowCount());

		return source;
	}
	
	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
