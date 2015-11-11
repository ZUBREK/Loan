package ifpr.pessoa.tecnicoAdministrativo.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="tecnicoAdmDao")
@ApplicationScoped
public class TecnicoAdministrativoImpl  extends GenericDao<TecnicoAdministrativo> implements TecnicoAdministrativoDao {
	
	private static final long serialVersionUID = 1L;
	
	public TecnicoAdministrativoImpl()
	{
		super(TecnicoAdministrativo.class);
	}
	
	
	
}
	