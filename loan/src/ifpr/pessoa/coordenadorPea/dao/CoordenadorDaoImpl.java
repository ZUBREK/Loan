package ifpr.pessoa.coordenadorPea.dao;

import java.util.List;

import ifpr.campus.Campus;
import ifpr.dao.GenericDao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name = "coordenadorDao")
@ApplicationScoped
public class CoordenadorDaoImpl extends GenericDao<CoordenadorPea> implements CoordenadorDao {

	private static final long serialVersionUID = 1L;

	public CoordenadorDaoImpl() {
		super(CoordenadorPea.class);
	}

	@Override
	public CoordenadorPea pesquisarPorSiape(String siape) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName() + " a where a.siape = :siape");
		query.setParameter("siape", siape);

		return (CoordenadorPea) query.getSingleResult();
	}

	@Override
	public CoordenadorPea pesquisarPorSiape(String siape, int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("SELECT a from " + classe.getSimpleName() + " a where a.siape = :siape and a.id != :id");
		query.setParameter("siape", siape);
		query.setParameter("id", id);
		return (CoordenadorPea) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<CoordenadorPea> findByNomeRole(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(
				"select u from Pessoa u where lower(u.nome) like concat('%', :nome, '%') and authority = :role1 or authority = :role2");
		q.setParameter("nome", nome);
		q.setParameter("role1", TipoPessoa.ROLE_COORDENADOR.toString());
		q.setParameter("role2", TipoPessoa.ROLE_TEC_COORD.toString());
		q.setMaxResults(10);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> pesquisarTecCoordPorCampus(Campus campus) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from " + classe.getSimpleName()
				+ " u where campus = :campus and authority = :role1");
		q.setParameter("campus", campus);
		q.setParameter("role1", TipoPessoa.ROLE_TEC_COORD.toString());
		q.setMaxResults(20);
		return (List<Pessoa>) q.getResultList();
	}

}
