package ifpr.pessoa.coordenadorPea.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="coordenadorDao")
@ApplicationScoped
public class CoordenadorDaoImpl  extends GenericDao<CoordenadorPea> implements CoordenadorDao {
	
	private static final long serialVersionUID = 1L;
	
	public CoordenadorDaoImpl()
	{
		super(CoordenadorPea.class);
	}
	
	
	
}
	