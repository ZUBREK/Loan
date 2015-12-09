package ifpr.competicao.jogos.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import ifpr.competicao.jogos.Jogos;
import ifpr.competicao.jogos.dao.JogosDao;

@ManagedBean(name = "jogosConverter")
@ApplicationScoped
public class JogosConverter implements Converter {

	@ManagedProperty(value = "#{jogosDao}")
	private JogosDao jogosDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um")) {
			return jogosDao.pegarJogosAno(Integer.parseInt(value));
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if (object instanceof Jogos)
			return ((Jogos) object).toString();
		return null;
	}

	public JogosDao getJogosDao() {
		return jogosDao;
	}

	public void setJogosDao(JogosDao jogosDao) {
		this.jogosDao = jogosDao;
	}

}