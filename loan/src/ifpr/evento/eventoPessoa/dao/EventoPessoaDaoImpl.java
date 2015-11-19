package ifpr.evento.eventoPessoa.dao;

import java.util.List;

import ifpr.dao.GenericDao;
import ifpr.evento.Evento;
import ifpr.evento.eventoPessoa.EventoPessoa;
import ifpr.pessoa.Pessoa;

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
		return q.getResultList();
	}

	@Override
	public EventoPessoa pesquisarPorEventoPessoa(Evento evento,
			Pessoa pessoa) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select ep from EventoPessoa as ep where ep.evento = :evento and ep.pessoa = :pessoa");
		q.setParameter("evento", evento);
		q.setParameter("pessoa", pessoa);
		return (EventoPessoa) q.getSingleResult();
	}
	
	
}
	