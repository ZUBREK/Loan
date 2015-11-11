package ifpr.campus.dao;

import ifpr.campus.Campus;
import ifpr.dao.GenericDao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="campusDao")
@ApplicationScoped
public class CampusDaoImpl  extends GenericDao<Campus> implements CampusDao {
	
	private static final long serialVersionUID = 1L;
	
	public CampusDaoImpl()
	{
		super(Campus.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Campus> pesquisarPorCidade(String cidade)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Campus u where lower(u.cidade) like :cidade");
		q.setParameter("cidade", cidade);
		q.setMaxResults(10);
		
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Campus> listarAlfabetica() {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Campus u order by u.cidade asc");
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	

}
	