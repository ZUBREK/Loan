package ifpr.delegacao.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ifpr.dao.GenericDao;
import ifpr.delegacao.Delegacao;
import ifpr.pessoa.Pessoa;

@ManagedBean(name = "delegacaoDao")
@ApplicationScoped
public class DelegacaoDaoImpl extends GenericDao<Delegacao> implements DelegacaoDao {

	private static final long serialVersionUID = 1L;

	public DelegacaoDaoImpl() {
		super(Delegacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Delegacao> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Delegacao u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Pessoa> listarPessoas(Delegacao delegacao) {

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(
				"select p from DelegacaoPessoa as dp inner join dp.pessoa" + " as p where dp.delegacao = :delegacao ");
		q.setParameter("delegacao", delegacao);
		return q.getResultList();
	}
}
