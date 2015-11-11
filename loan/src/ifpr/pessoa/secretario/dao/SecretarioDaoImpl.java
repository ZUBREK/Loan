package ifpr.pessoa.secretario.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.secretario.Secretario;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="secretarioDao")
@ApplicationScoped
public class SecretarioDaoImpl  extends GenericDao<Secretario> implements SecretarioDao {
	
	private static final long serialVersionUID = 1L;
	
	public SecretarioDaoImpl()
	{
		super(Secretario.class);
	}
	
	
	
}
	