package ifpr.competicao.grupoChaves.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ifpr.competicao.grupoChaves.GrupoChaves;
import ifpr.competicao.grupoChaves.dao.GrupoChavesDao;

@ManagedBean(name = "grupoChavesLazyDataModel")
@ViewScoped
public class GrupoChavesLazyDataModel extends LazyDataModel<GrupoChaves> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{grupoChavesDao}")
	private GrupoChavesDao grupoChavesDao;

	public GrupoChavesLazyDataModel() {

	}

	@Override
	public GrupoChaves getRowData(String rowKey) {
		return grupoChavesDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(GrupoChaves campus) {
		return campus.getId();
	}

	@Override
	public List<GrupoChaves> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<GrupoChaves> source = null;

		source = grupoChavesDao.list(first, pageSize);

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyGrupoChavesSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(grupoChavesDao.getRowCount());

		return source;
	}

	public GrupoChavesDao getGrupoChavesDao() {
		return grupoChavesDao;
	}

	public void setGrupoChavesDao(GrupoChavesDao grupoChavesDao) {
		this.grupoChavesDao = grupoChavesDao;
	}

}
