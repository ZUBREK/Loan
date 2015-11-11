package ifpr.campus.model;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean(name = "campusConverter")
@ApplicationScoped
public class CampusConverter implements Converter {

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if ( value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um") ){
			return campusDao.pesquisarPorCidade(value).get(0);
			}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if ( object instanceof Campus )
			return ((Campus)object).toString();
		return null;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}


}