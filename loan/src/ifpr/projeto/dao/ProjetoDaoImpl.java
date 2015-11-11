package ifpr.projeto.dao;

import ifpr.dao.GenericDao;
import ifpr.projeto.Projeto;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="projetoDao")
@ApplicationScoped
public class ProjetoDaoImpl  extends GenericDao<Projeto> implements ProjetoDao {
	
	private static final long serialVersionUID = 1L;
	
	public ProjetoDaoImpl()
	{
		super(Projeto.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Projeto> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Projeto u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	

}
	