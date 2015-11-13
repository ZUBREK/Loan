package ifpr.modalidade.model;

import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name="modalidadeLazyDataModel")
@ViewScoped
public class ModalidadeLazyDataModel extends LazyDataModel<Modalidade> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;
	
	public ModalidadeLazyDataModel() {
        
    }
     
    @Override
    public Modalidade getRowData(String rowKey) {
        return modalidadeDao.findById(Integer.parseInt(rowKey));
    }
 
    @Override
    public Object getRowKey(Modalidade campus) {
        return campus.getId();
    }
 
    @Override
    public List<Modalidade> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
    {
        List<Modalidade> source = null;
          
       source = modalidadeDao.list(first, pageSize);

 
        //sort
        if(sortField != null) {
            Collections.sort(source, new LazyModalidadeSorter(sortField, sortOrder));
        }
 
        //rowCount
        this.setRowCount(modalidadeDao.getRowCount());
 
        return source;
    }

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}



}
