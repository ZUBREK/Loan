package ifpr.pessoa.secretario.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.secretario.Secretario;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name = "secretarioDao")
@ApplicationScoped
public class SecretarioDaoImpl extends GenericDao<Secretario> implements
		SecretarioDao {

	private static final long serialVersionUID = 1L;

	public SecretarioDaoImpl() {
		super(Secretario.class);
	}

	public Secretario pesquisarPorCpf(String cpf) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.cpf = :cpf");
		query.setParameter("cpf", cpf);
		return (Secretario) query.getSingleResult();
	}

	public Secretario pesquisarPorCpf(String cpf, int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.cpf = :cpf and a.id != :id");
		query.setParameter("cpf", cpf);
		query.setParameter("id", id);
		return (Secretario) query.getSingleResult();
	}

}
