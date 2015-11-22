package ifpr.pessoa.coordenadorPea.model;

import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean(name = "coordenadorConverter")
@ApplicationScoped
public class CoordenadorConverter implements Converter {

	@ManagedProperty(value = "#{coordenadorDao}")
	private CoordenadorDao coordenadorDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		CoordenadorPea pessoa;
		if (value != null && !value.isEmpty()
				&& !value.equalsIgnoreCase("Selecione um")) {
			try {
				 pessoa = coordenadorDao.findByNomeRole(value,
						TipoPessoa.ROLE_COORDENADOR.toString()).get(0);
			} catch (Exception e) {
				pessoa = coordenadorDao.findByNomeRole(value,
						TipoPessoa.ROLE_TEC_COORD.toString()).get(0);
			}

			return pessoa;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) throws ConverterException {
		if (object instanceof CoordenadorPea)
			return ((CoordenadorPea) object).toString();
		return null;
	}

	public CoordenadorDao getCoordenadorDao() {
		return coordenadorDao;
	}

	public void setCoordenadorDao(CoordenadorDao coordenadorDao) {
		this.coordenadorDao = coordenadorDao;
	}

}