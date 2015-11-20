package ifpr.pessoa.tecnicoAdministrativo.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="tecnicoAdmDao")
@ApplicationScoped
public class TecnicoAdministrativoImpl  extends GenericDao<TecnicoAdministrativo> implements TecnicoAdministrativoDao {
	
	private static final long serialVersionUID = 1L;
	
	public TecnicoAdministrativoImpl()
	{
		super(TecnicoAdministrativo.class);
	}
	
	@Override
	public TecnicoAdministrativo pesquisarPorSiape(String siape) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from" + classe.getSimpleName()
				+ " a where a.siape = :siape");
		query.setParameter("siape", siape);

		return (TecnicoAdministrativo) query.getSingleResult();
	}

	@Override
	public TecnicoAdministrativo pesquisarPorSiape(String siape, int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from" + classe.getSimpleName()
				+ " a where a.siape = :siape and a.id != :id");
		query.setParameter("siape", siape);
		query.setParameter("id", id);
		return (TecnicoAdministrativo) query.getSingleResult();
	}
	
	
	
}
	