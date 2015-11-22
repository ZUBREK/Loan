package ifpr.competicao.grupoChaves.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import ifpr.competicao.grupoChaves.GrupoChaves;
import ifpr.competicao.grupoChaves.dao.GrupoChavesDao;

@ManagedBean(name = "grupoChavesConverter")
@ApplicationScoped
public class GrupoChavesConverter implements Converter {

	@ManagedProperty(value = "#{gruposChaveDao}")
	private GrupoChavesDao grupoChavesDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if ( value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um") ){
			return grupoChavesDao.pesquisarPorNome(value).get(0);
			}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if ( object instanceof GrupoChaves )
			return ((GrupoChaves)object).toString();
		return null;
	}

	public GrupoChavesDao getGrupoChavesDao() {
		return grupoChavesDao;
	}

	public void setGrupoChavesDao(GrupoChavesDao grupoChavesDao) {
		this.grupoChavesDao = grupoChavesDao;
	}

}