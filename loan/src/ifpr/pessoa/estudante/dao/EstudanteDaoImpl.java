package ifpr.pessoa.estudante.dao;

import ifpr.campus.Campus;
import ifpr.dao.GenericDao;
import ifpr.pessoa.estudante.Estudante;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name = "estudanteDao")
@ApplicationScoped
public class EstudanteDaoImpl extends GenericDao<Estudante> implements
		EstudanteDao {

	private static final long serialVersionUID = 1L;

	public EstudanteDaoImpl() {
		super(Estudante.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Estudante> pesquisarEstudanteNomeCampus(String nome,
			Campus campus) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from "
						+ classe.getSimpleName()
						+ " u where campus = :campus and lower(u.nome) like concat('%', :nome, '%') ");
		q.setParameter("campus", campus);
		q.setParameter("nome", nome);
		q.setMaxResults(10);
		return (List<Estudante>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Estudante> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from" + classe.getSimpleName()
				+ " u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

	public Estudante pesquisarPorCpf(String cpf) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.cpf = :cpf");
		query.setParameter("cpf", cpf);
		return (Estudante) query.getSingleResult();
	}

	public Estudante pesquisarPorCpf(String cpf, int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.cpf = :cpf and a.id != :id");
		query.setParameter("cpf", cpf);
		query.setParameter("id", id);
		return (Estudante) query.getSingleResult();
	}

	@Override
	public Estudante pesquisarPorMatricula(String matricula) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.matricula = :matricula");
		query.setParameter("matricula", matricula);
		return (Estudante) query.getSingleResult();
	}
	
	@Override
	public Estudante pesquisarPorMatricula(String matricula,int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.matricula = :matricula and a.id != :id");
		query.setParameter("matricula", matricula);
		query.setParameter("id", id);
		return (Estudante) query.getSingleResult();
		
	}
	
	
}
