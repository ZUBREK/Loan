package ifpr.pessoa.tecnicoAdministrativo.model;

import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;
import ifpr.pessoa.tecnicoAdministrativo.dao.TecnicoAdministrativoDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean(name = "tecnicoAdmConverter")
@ApplicationScoped
public class TecnicoAdministrativoConverter implements Converter {

	@ManagedProperty(value = "#{tecnicoAdmDao}")
	private TecnicoAdministrativoDao tecnicoAdmDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if ( value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um") )
			return tecnicoAdmDao.findByNome(value).get(0);
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if ( object instanceof TecnicoAdministrativo )
			return ((TecnicoAdministrativo)object).toString();
		return null;
	}

	public TecnicoAdministrativoDao getTecnicoAdministrativoDao()
	{
		return tecnicoAdmDao;
	}

	public void setTecnicoAdministrativoDao(TecnicoAdministrativoDao tecnicoAdmDao)
	{
		this.tecnicoAdmDao = tecnicoAdmDao;
	}
}