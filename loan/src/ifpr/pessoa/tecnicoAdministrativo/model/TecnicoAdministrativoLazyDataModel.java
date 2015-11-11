package ifpr.pessoa.tecnicoAdministrativo.model;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;
import ifpr.pessoa.tecnicoAdministrativo.dao.TecnicoAdministrativoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name="tecnicoAdmLazyDataModel")
@ViewScoped
public class TecnicoAdministrativoLazyDataModel extends LazyDataModel<TecnicoAdministrativo> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{tecnicoAdmDao}")
	private TecnicoAdministrativoDao tecnicoAdmDao;
	
	public TecnicoAdministrativoLazyDataModel() {
        
    }
     
    @Override
    public TecnicoAdministrativo getRowData(String rowKey) {
        return tecnicoAdmDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(TecnicoAdministrativo tecnicoAdm) {
        return tecnicoAdm.getId();
    }
 
    @Override
    public List<TecnicoAdministrativo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<TecnicoAdministrativo> source = null;
        
        if (filters.containsKey("nome") )
        {
        	String nomePesquisa = filters.get("nome").toString();
        	source = tecnicoAdmDao.findByNomeRole(nomePesquisa,TipoPessoa.ROLE_TEC_ADM.toString());
        }
        else
        {
        	source = tecnicoAdmDao.list(first, pageSize);
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazyTecnicoAdministrativoSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(tecnicoAdmDao.getRowCount());
 
        return source;
    }

	public TecnicoAdministrativoDao getTecnicoAdmDao() {
		return tecnicoAdmDao;
	}

	public void setTecnicoAdmDao(TecnicoAdministrativoDao tecnicoAdmDao) {
		this.tecnicoAdmDao = tecnicoAdmDao;
	}


}
