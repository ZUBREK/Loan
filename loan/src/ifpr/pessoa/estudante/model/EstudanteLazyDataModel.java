package ifpr.pessoa.estudante.model;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "estudanteLazyDataModel")
@ViewScoped
public class EstudanteLazyDataModel extends LazyDataModel<Estudante> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	public EstudanteLazyDataModel() {

	}

	@Override
	public Estudante getRowData(String rowKey) {
		return estudanteDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Estudante tecnicoAdm) {
		return tecnicoAdm.getId();
	}

	@Override
	public List<Estudante> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Estudante> source = null;

		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = estudanteDao.findByNomeRole(nomePesquisa,
					TipoPessoa.ROLE_ESTUDANTE.toString());
		} else {
			source = estudanteDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyEstudanteSorter(sortField,
					sortOrder));
		}

		// rowCount
		this.setRowCount(estudanteDao.getRowCount());

		return source;
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}
}
