package ifpr.modalidade.dao;

import ifpr.dao.GenericDao;
import ifpr.modalidade.Modalidade;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="modalidadeDao")
@ApplicationScoped
public class ModalidadeDaoImpl  extends GenericDao<Modalidade> implements ModalidadeDao {
	
	private static final long serialVersionUID = 1L;
	
	public ModalidadeDaoImpl()
	{
		super(Modalidade.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Modalidade> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Modalidade u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Modalidade> listarAlfabetica() {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Modalidade u order by u.nome asc");
		q.setMaxResults(50);
		
		return q.getResultList();
	}

}
	