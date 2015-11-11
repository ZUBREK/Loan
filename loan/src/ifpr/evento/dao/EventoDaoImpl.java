package ifpr.evento.dao;

import ifpr.dao.GenericDao;
import ifpr.evento.Evento;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="eventoDao")
@ApplicationScoped
public class EventoDaoImpl  extends GenericDao<Evento> implements EventoDao {
	
	private static final long serialVersionUID = 1L;
	
	public EventoDaoImpl()
	{
		super(Evento.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> pesquisarPorNome(String nome)
	{
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from Evento u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		
		return q.getResultList();
	}
	

}
	