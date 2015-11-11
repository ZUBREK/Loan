package ifpr.campus.model;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name="campusLazyDataModel")
@ViewScoped
public class CampusLazyDataModel extends LazyDataModel<Campus> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;
	
	public CampusLazyDataModel() {
        
    }
     
    @Override
    public Campus getRowData(String rowKey) {
        return campusDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(Campus campus) {
        return campus.getId();
    }
 
    @Override
    public List<Campus> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<Campus> source = null;
          
       source = campusDao.list(first, pageSize);

 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazyCampusSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(campusDao.getRowCount());
 
        return source;
    }

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}



}
