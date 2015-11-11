package ifpr.pessoa.model;

import ifpr.pessoa.Pessoa;
import ifpr.pessoa.dao.PessoaDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean(name = "pessoaConverter")
@ApplicationScoped
public class PessoaConverter implements Converter {

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		Pessoa pessoa = null;
		if ( value != null && !value.isEmpty() && !value.equalsIgnoreCase("Selecione um") )
			pessoa = pessoaDao.pesquisarPorNome(value).get(0);
			System.out.println(pessoa.getNome());
			return pessoaDao.pesquisarPorNome(value).get(0);
		//return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if ( object instanceof Pessoa )
			return ((Pessoa)object).toString();
		return null;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}
}