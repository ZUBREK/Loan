package ifpr.competicao.time.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import ifpr.competicao.time.Time;
import ifpr.competicao.time.dao.TimeDao;

@ManagedBean(name = "timeConverter")
@ApplicationScoped
public class TimeConverter implements Converter {

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um")) {
			return timeDao.pesquisarPorNome(value).get(0);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if (object instanceof Time)
			return ((Time) object).toString();
		return null;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
	}

}