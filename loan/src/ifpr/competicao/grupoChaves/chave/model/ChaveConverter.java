package ifpr.competicao.grupoChaves.chave.model;

import ifpr.competicao.grupoChaves.chave.Chave;
import ifpr.competicao.grupoChaves.chave.dao.ChaveDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean(name = "chaveConverter")
@ApplicationScoped
public class ChaveConverter implements Converter {

	@ManagedProperty(value = "#{chaveDao}")
	private ChaveDao chaveDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if ( value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um") ){
			return chaveDao.pesquisarPorNome(value).get(0);
			}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if ( object instanceof Chave )
			return ((Chave)object).toString();
		return null;
	}

	public ChaveDao getChaveDao() {
		return chaveDao;
	}

	public void setChaveDao(ChaveDao chaveDao) {
		this.chaveDao = chaveDao;
	}
}