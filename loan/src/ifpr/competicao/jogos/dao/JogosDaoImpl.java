package ifpr.competicao.jogos.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ifpr.competicao.jogos.Jogos;
import ifpr.dao.GenericDao;

@ManagedBean(name = "jogosDao")
@ApplicationScoped
public class JogosDaoImpl extends GenericDao<Jogos> implements JogosDao {

	private static final long serialVersionUID = 1L;

	public JogosDaoImpl() {
		super(Jogos.class);
	}

	@Override
	public boolean existeJogosAno(int ano) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from Jogos a where a.ano = :ano");
		query.setParameter("ano", ano);
		try {
			query.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

}
