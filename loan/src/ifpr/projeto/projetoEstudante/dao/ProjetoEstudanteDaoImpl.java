package ifpr.projeto.projetoEstudante.dao;

import ifpr.dao.GenericDao;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="projetoEstudanteDao")
@ApplicationScoped
public class ProjetoEstudanteDaoImpl  extends GenericDao<ProjetoEstudante> implements ProjetoEstudanteDao {
	
	private static final long serialVersionUID = 1L;
	
	public ProjetoEstudanteDaoImpl()
	{
		super(ProjetoEstudante.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjetoEstudante> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from " + classe.getSimpleName() + " u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	

}
	