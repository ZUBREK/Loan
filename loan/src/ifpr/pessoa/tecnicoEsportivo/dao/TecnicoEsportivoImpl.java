package ifpr.pessoa.tecnicoEsportivo.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="tecnicoEsportivoDao")
@ApplicationScoped
public class TecnicoEsportivoImpl  extends GenericDao<TecnicoEsportivo> implements TecnicoEsportivoDao {
	
	private static final long serialVersionUID = 1L;
	
	public TecnicoEsportivoImpl()
	{
		super(TecnicoEsportivo.class);
	}
	
	@Override
	public TecnicoEsportivo pesquisarPorSiape(String siape) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.siape = :siape");
		query.setParameter("siape", siape);

		return (TecnicoEsportivo) query.getSingleResult();
	}

	@Override
	public TecnicoEsportivo pesquisarPorSiape(String siape, int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.siape = :siape and a.id != :id");
		query.setParameter("siape", siape);
		query.setParameter("id", id);
		return (TecnicoEsportivo) query.getSingleResult();
	}
}
	