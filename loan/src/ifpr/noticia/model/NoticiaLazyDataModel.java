package ifpr.noticia.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ifpr.noticia.Noticia;
import ifpr.noticia.dao.NoticiaDao;

@ManagedBean(name="noticiaLazyDataModel")
@ViewScoped
public class NoticiaLazyDataModel extends LazyDataModel<Noticia> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{noticiaDao}")
	private NoticiaDao noticiaDao;
	
	public NoticiaLazyDataModel() {
        
    }
     
    @Override
    public Noticia getRowData(String rowKey) {
        return noticiaDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(Noticia noticia) {
        return noticia.getId();
    }
 
    @Override
    public List<Noticia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<Noticia> source = null;
        
        if (filters.containsKey("titulo") )
        {
        	String nomePesquisa = filters.get("titulo").toString();
        	source = noticiaDao.findByTitulo(nomePesquisa);
        }
        else
        {
        	source = noticiaDao.list(first, pageSize);
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazyNoticiaSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(noticiaDao.getRowCount());
 
        return source;
    }

	public NoticiaDao getNoticiaDao() {
		return noticiaDao;
	}

	public void setNoticiaDao(NoticiaDao noticiaDao) {
		this.noticiaDao = noticiaDao;
	}
    
    




}
