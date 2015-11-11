package ifpr.competicao.time.estudante.dao;

import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.dao.GenericDao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="timeEstudanteDao")
@ApplicationScoped
public class TimeEstudanteDaoImpl  extends GenericDao<TimeEstudante> implements TimeEstudanteDao {
	
	private static final long serialVersionUID = 1L;
	
	public TimeEstudanteDaoImpl()
	{
		super(TimeEstudante.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TimeEstudante> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from TimeEstudante u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	

}
	