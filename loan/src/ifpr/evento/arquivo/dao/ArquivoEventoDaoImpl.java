package ifpr.evento.arquivo.dao;

import ifpr.dao.GenericDao;
import ifpr.evento.Evento;
import ifpr.evento.arquivo.ArquivoEvento;
import ifpr.evento.eventoPessoa.EventoPessoa;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;


@ManagedBean(name="arquivoEventoDao")
@SessionScoped
public class ArquivoEventoDaoImpl extends GenericDao<ArquivoEvento> implements ArquivoEventoDao {


	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ArquivoEvento> pesquisarPorEvento(Evento evento) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select ae from ArquivoEvento as ae inner join ae.eventoPessoa as ep where ep.evento = :evento");
		q.setParameter("evento", evento);
		return q.getResultList();
	}
	
	@Override
	public ArquivoEvento pesquisarPorEventoPessoa(EventoPessoa eventoPessoa) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select ae from ArquivoEvento as ae  where ae.eventoPessoa = :eventoPessoa");
		q.setParameter("eventoPessoa", eventoPessoa);
		return (ArquivoEvento) q.getSingleResult();
	}
	

}
