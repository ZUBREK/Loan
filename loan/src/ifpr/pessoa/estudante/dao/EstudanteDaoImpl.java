package ifpr.pessoa.estudante.dao;

import ifpr.campus.Campus;
import ifpr.dao.GenericDao;
import ifpr.pessoa.estudante.Estudante;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="estudanteDao")
@ApplicationScoped
public class EstudanteDaoImpl  extends GenericDao<Estudante> implements EstudanteDao {
	
	private static final long serialVersionUID = 1L;
	
	public EstudanteDaoImpl()
	{
		super(Estudante.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Estudante> pesquisarEstudanteNomeCampus(String nome, Campus campus) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Estudante u where campus = :campus and lower(u.nome) like concat('%', :nome, '%') ");
		q.setParameter("campus", campus);
		q.setParameter("nome", nome);
		q.setMaxResults(10);
		return (List<Estudante>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Estudante> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Estudante u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
}
	