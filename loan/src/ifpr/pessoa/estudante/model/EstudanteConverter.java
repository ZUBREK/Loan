package ifpr.pessoa.estudante.model;

import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean(name = "estudanteConverter")
@ApplicationScoped
public class EstudanteConverter implements Converter {

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if ( value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um") )
			return estudanteDao.pesquisarPorNome(value).get(0);
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if ( object instanceof Estudante )
			return ((Estudante)object).toString();
		return null;
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}
	
	
}