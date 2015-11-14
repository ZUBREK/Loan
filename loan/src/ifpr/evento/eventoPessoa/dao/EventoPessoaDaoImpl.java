package ifpr.evento.eventoPessoa.dao;

import java.util.List;

import ifpr.dao.GenericDao;
import ifpr.evento.Evento;
import ifpr.evento.eventoPessoa.EventoPessoa;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name="eventoPessoaDao")
@ApplicationScoped
public class EventoPessoaDaoImpl  extends GenericDao<EventoPessoa> implements EventoPessoaDao {
	
	private static final long serialVersionUID = 1L;
	
	public EventoPessoaDaoImpl()
	{
		super(EventoPessoa.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventoPessoa> pesquisarPorEvento(Evento evento) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select ep from EventoPessoa as ep where ep.evento = :evento");
		q.setParameter("evento", evento);
		List<EventoPessoa> lista = q.getResultList();
		return lista;
	}
	
	
}
	