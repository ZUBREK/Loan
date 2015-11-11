package ifpr.competicao.partida.local.dao;

import ifpr.competicao.partida.local.Local;
import ifpr.dao.GenericDao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name = "localDao")
@ApplicationScoped
public class LocalDaoImpl extends GenericDao<Local> implements LocalDao {

	private static final long serialVersionUID = 1L;

	public LocalDaoImpl() {
		super(Local.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Local> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Local u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

}
