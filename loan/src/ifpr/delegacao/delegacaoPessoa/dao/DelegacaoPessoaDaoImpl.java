package ifpr.delegacao.delegacaoPessoa.dao;

import ifpr.dao.GenericDao;
import ifpr.delegacao.delegacaoPessoa.DelegacaoPessoa;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "delegacaoPessoaDao")
@ApplicationScoped
public class DelegacaoPessoaDaoImpl extends GenericDao<DelegacaoPessoa> implements DelegacaoPessoaDao {

	private static final long serialVersionUID = 1L;

	public DelegacaoPessoaDaoImpl() {
		super(DelegacaoPessoa.class);
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<Chave> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Chave u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}*/

}
