package ifpr.pessoa.coordenadorPea.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="coordenadorDao")
@ApplicationScoped
public class CoordenadorDaoImpl  extends GenericDao<CoordenadorPea> implements CoordenadorDao {
	
	private static final long serialVersionUID = 1L;
	
	public CoordenadorDaoImpl()
	{
		super(CoordenadorPea.class);
	}

	@Override
	public CoordenadorPea pesquisarPorSiape(String siape) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.siape = :siape");
		query.setParameter("siape", siape);

		return (CoordenadorPea) query.getSingleResult();
	}

	@Override
	public CoordenadorPea pesquisarPorSiape(String siape, int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.siape = :siape and a.id != :id");
		query.setParameter("siape", siape);
		query.setParameter("id", id);
		return (CoordenadorPea) query.getSingleResult();
	}
	
	
	
}
	