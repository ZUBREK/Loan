package ifpr.arquivo.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ifpr.arquivo.Arquivo;
import ifpr.dao.GenericDao;
import ifpr.pessoa.Pessoa;

@ManagedBean(name = "arquivoDao")
@ApplicationScoped
public class ArquivoDaoImpl extends GenericDao<Arquivo> implements ArquivoDao {

	private static final long serialVersionUID = 1L;

	public ArquivoDaoImpl() {
		super(Arquivo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Arquivo> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Arquivo u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

	@Override
	public Arquivo pesquisarFotoPerfil(Pessoa pessoa) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Arquivo u where uploader = :pessoa and isFotoPerfil = '1'");
		q.setParameter("pessoa", pessoa);
		try {
			return (Arquivo) q.getSingleResult();
		} catch (NoResultException ex) {
			return (new Arquivo());
		}

	}

}
