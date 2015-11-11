package ifpr.pessoa.coordenadorPea.model;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name="coordLazyDataModel")
@ViewScoped
public class CoordenadorLazyDataModel extends LazyDataModel<CoordenadorPea> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{coordenadorDao}")
	private CoordenadorDao coordenadorDao;
	
	public CoordenadorLazyDataModel() {
        
    }
     
    @Override
    public CoordenadorPea getRowData(String rowKey) {
        return coordenadorDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(CoordenadorPea coordenador) {
        return coordenador.getId();
    }
 
    @Override
    public List<CoordenadorPea> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<CoordenadorPea> source = null;
        
        if (filters.containsKey("nome") )
        {
        	String nomePesquisa = filters.get("nome").toString();
        	source = coordenadorDao.findByNomeRole(nomePesquisa,TipoPessoa.ROLE_COORDENADOR.toString());
        }
        else
        {
        	source = coordenadorDao.list(first, pageSize);
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazyCoordenadorSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(coordenadorDao.getRowCount());
 
        return source;
    }

	public CoordenadorDao getCoordenadorDao() {
		return coordenadorDao;
	}

	public void setCoordenadorDao(CoordenadorDao coordenadorDao) {
		this.coordenadorDao = coordenadorDao;
	}


}
