package ifpr.evento.model;

import ifpr.evento.Evento;
import ifpr.evento.dao.EventoDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name="eventoLazyDataModel")
@ViewScoped
public class EventoLazyDataModel extends LazyDataModel<Evento> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{eventoDao}")
	private EventoDao eventoDao;
	
	public EventoLazyDataModel() {
        
    }
     
    @Override
    public Evento getRowData(String rowKey) {
        return eventoDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(Evento campus) {
        return campus.getId();
    }
 
    @Override
    public List<Evento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<Evento> source = null;
          
       source = eventoDao.list(first, pageSize);

 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazyEventoSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(eventoDao.getRowCount());
 
        return source;
    }

	public EventoDao getEventoDao() {
		return eventoDao;
	}

	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}



	


}
