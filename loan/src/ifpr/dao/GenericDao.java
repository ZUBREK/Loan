package ifpr.dao;

import ifpr.campus.Campus;
import ifpr.modalidade.Modalidade;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

@ManagedBean(name = "genericDao")
public class GenericDao<T> implements Dao<T>, Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName = "loanPU")
	protected EntityManagerFactory emf;

	@Resource
	private UserTransaction utx;

	protected Class<T> classe;

	public GenericDao() {
	}

	public GenericDao(Class<T> classe) {
		this.classe = classe;
	}

	public void salvar(T obj) {
		EntityManager em = emf.createEntityManager();
		try {
			utx.begin();
			em.joinTransaction();
			boolean committed = false;
			try {
				em.persist(obj);
				utx.commit();
				committed = true;
			} finally {
				if (!committed)
					utx.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public T update(T obj) {
		EntityManager em = emf.createEntityManager();
		try {
			utx.begin();
			em.joinTransaction();
			boolean committed = false;
			try {
				obj = em.merge(obj);
				utx.commit();
				committed = true;
			} finally {
				if (!committed)
					utx.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return obj;
	}

	@Override
	public void remover(T obj) throws Exception {
		EntityManager em = emf.createEntityManager();
		try {
			utx.begin();
			em.joinTransaction();
			boolean committed = false;
			try {
				obj = em.merge(obj);
				em.refresh(obj);
				em.remove(obj);
				utx.commit();
				committed = true;
			} finally {
				if (!committed)
					utx.rollback();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public T findById(long id) {
		EntityManager em = emf.createEntityManager();
		return em.find(classe, id);
	}

	@Override
	public T findById(int id) {
		EntityManager em = emf.createEntityManager();
		return em.find(classe, id);
	}

	@Override
	public T findByIdInteger(Integer id) {
		EntityManager em = emf.createEntityManager();
		return em.find(classe, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listDesc() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a order by a.id DESC");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listAsc() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a order by a.nome ASC");
		query.setMaxResults(30);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where lower(a.nome) like concat('%', :nome, '%')");
		query.setParameter("nome", nome);
		query.setMaxResults(10);
		return query.getResultList();
	}

	@Override
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@SuppressWarnings("unchecked")
	public List<T> list(int first, int size) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a order by a.id desc");
		query.setFirstResult(first);
		query.setMaxResults(size);
		return query.getResultList();
	}

	public int getRowCount() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT count(a) from "
				+ classe.getSimpleName() + " a");
		return Integer.parseInt(query.getSingleResult().toString());
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNomeRole(String nome, String role) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Pessoa u where lower(u.nome) like concat('%', :nome, '%') and authority = :role");
		q.setParameter("nome", nome);
		q.setParameter("role", role);
		q.setMaxResults(10);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> listarPessoaByCampusEmAlfabetica(Campus campus) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from " + classe.getSimpleName()
				+ " u where campus = :campus order by u.nome");
		q.setParameter("campus", campus);
		q.setMaxResults(10);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listarPorCampusModalidade(Campus campus,
			Modalidade modalidade) {

		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select e from TimeEstudante as te inner join te.estudante as e "
						+ "where e.campus = :campus and te.time.modalidade = :modalidade order by e.nome");
		q.setParameter("campus", campus);
		q.setParameter("modalidade", modalidade);
		q.setMaxResults(100);

		return q.getResultList();
	}

	

}
