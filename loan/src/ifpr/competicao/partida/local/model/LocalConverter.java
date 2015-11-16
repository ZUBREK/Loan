package ifpr.competicao.partida.local.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import ifpr.competicao.partida.local.Local;
import ifpr.competicao.partida.local.dao.LocalDao;

@ManagedBean(name = "localConverter")
@ApplicationScoped
public class LocalConverter implements Converter {

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um")) {
			return localDao.findByNome(value).get(0);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if (object instanceof Local)
			return ((Local) object).toString();
		return null;
	}

	public LocalDao getLocalDao() {
		return localDao;
	}

	public void setLocalDao(LocalDao localDao) {
		this.localDao = localDao;
	}

}