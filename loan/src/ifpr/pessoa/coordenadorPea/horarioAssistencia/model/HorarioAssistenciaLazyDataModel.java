package ifpr.pessoa.coordenadorPea.horarioAssistencia.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ifpr.pessoa.coordenadorPea.horarioAssistencia.HorarioAssistencia;
import ifpr.pessoa.coordenadorPea.horarioAssistencia.dao.HorarioAssistenciaDao;

@ManagedBean(name = "horarioAssistenciaLazyDataModel")
@ViewScoped
public class HorarioAssistenciaLazyDataModel extends LazyDataModel<HorarioAssistencia> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{horarioAssistenciaDao}")
	private HorarioAssistenciaDao horarioAssistenciaDao;

	public HorarioAssistenciaLazyDataModel() {

	}

	@Override
	public HorarioAssistencia getRowData(String rowKey) {
		return horarioAssistenciaDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(HorarioAssistencia horarioAssistencia) {
		return horarioAssistencia.getId();
	}

	@Override
	public List<HorarioAssistencia> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<HorarioAssistencia> source = null;

		if (filters.containsKey("materia")) {
			String nomePesquisa = filters.get("materia").toString();
			source = horarioAssistenciaDao.findByMateria(nomePesquisa);
		} else {
			source = horarioAssistenciaDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyHorarioAssistenciaSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(horarioAssistenciaDao.getRowCount());

		return source;
	}

	public HorarioAssistenciaDao getHorarioAssistenciaDao() {
		return horarioAssistenciaDao;
	}

	public void setHorarioAssistenciaDao(HorarioAssistenciaDao horarioAssistenciaDao) {
		this.horarioAssistenciaDao = horarioAssistenciaDao;
	}

}
