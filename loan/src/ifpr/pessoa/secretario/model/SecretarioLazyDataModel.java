package ifpr.pessoa.secretario.model;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.secretario.Secretario;
import ifpr.pessoa.secretario.dao.SecretarioDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name="secretarioLazyDataModel")
@ViewScoped
public class SecretarioLazyDataModel extends LazyDataModel<Secretario> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{secretarioDao}")
	private SecretarioDao secretarioDao;
	
	public SecretarioLazyDataModel() {
        
    }
     
    @Override
    public Secretario getRowData(String rowKey) {
        return secretarioDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(Secretario secretario) {
        return secretario.getId();
    }
 
    @Override
    public List<Secretario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<Secretario> source = null;
        
        if (filters.containsKey("nome") )
        {
        	String nomePesquisa = filters.get("nome").toString();
        	source = secretarioDao.findByNomeRole(nomePesquisa,TipoPessoa.ROLE_SECRETARIO.toString());
        }
        else
        {
        	source = secretarioDao.list(first, pageSize);
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazySecretarioSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(secretarioDao.getRowCount());
 
        return source;
    }

	public SecretarioDao getSecretarioDao() {
		return secretarioDao;
	}

	public void setSecretarioDao(SecretarioDao secretarioDao) {
		this.secretarioDao = secretarioDao;
	}
    
    




}
