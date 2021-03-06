package ifpr.pessoa.tecnicoEsportivo.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;
import ifpr.pessoa.tecnicoEsportivo.dao.TecnicoEsportivoDao;

@ManagedBean(name = "tecnicoEsportivoConverter")
@ApplicationScoped
public class TecnicoEsportivoConverter implements Converter {

	@ManagedProperty(value = "#{tecnicoEsportivoDao}")
	private TecnicoEsportivoDao tecnicoEsportivoDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		TecnicoEsportivo pessoa;
		if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um")) {
			try {
				pessoa = tecnicoEsportivoDao.findByNomeRole(value, TipoPessoa.ROLE_TEC_ESP.toString()).get(0);
			} catch (Exception e) {
				pessoa = tecnicoEsportivoDao.findByNomeRole(value, TipoPessoa.ROLE_TEC_COORD.toString()).get(0);
			}
			return pessoa;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if (object instanceof TecnicoEsportivo)
			return ((TecnicoEsportivo) object).toString();
		return null;
	}

	public TecnicoEsportivoDao getTecnicoEsportivoDao() {
		return tecnicoEsportivoDao;
	}

	public void setTecnicoEsportivoDao(TecnicoEsportivoDao tecnicoEsportivoDao) {
		this.tecnicoEsportivoDao = tecnicoEsportivoDao;
	}

}