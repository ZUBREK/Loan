package ifpr.pessoa.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@ManagedBean(name="pessoaDao")
@ApplicationScoped
public class PessoaDaoImpl  extends GenericDao<Pessoa> implements PessoaDao {
	
	private static final long serialVersionUID = 1L;
	
	public PessoaDaoImpl()
	{
		super(Pessoa.class);
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Pessoa u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	
	public Pessoa findByLogin(String login)  throws NoResultException
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Pessoa u where lower(u.login) = :login ");
		q.setParameter("login", login);
		
		return (Pessoa) q.getSingleResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findByRole(TipoPessoa tipo) {
		
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Pessoa u where authority = :role");
		q.setParameter("role", tipo.toString());

		return q.getResultList();
	}

	
	

}
	