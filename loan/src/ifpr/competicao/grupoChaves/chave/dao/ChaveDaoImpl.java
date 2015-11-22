package ifpr.competicao.grupoChaves.chave.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ifpr.competicao.grupoChaves.chave.Chave;
import ifpr.dao.GenericDao;

@ManagedBean(name = "chaveDao")
@ApplicationScoped
public class ChaveDaoImpl extends GenericDao<Chave> implements ChaveDao {

	private static final long serialVersionUID = 1L;

	public ChaveDaoImpl() {
		super(Chave.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chave> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Chave u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

}
