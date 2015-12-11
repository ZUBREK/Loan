package ifpr.evento.dao;

import ifpr.dao.GenericDao;
import ifpr.evento.Evento;
import ifpr.evento.TipoEvento;
import ifpr.pessoa.Pessoa;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name = "eventoDao")
@ApplicationScoped
public class EventoDaoImpl extends GenericDao<Evento> implements EventoDao {

	private static final long serialVersionUID = 1L;

	public EventoDaoImpl() {
		super(Evento.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(
				"select u from " + classe.getSimpleName() + " u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> listByTecnico(int first, int size, Pessoa pessoa) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery(
				"SELECT a from " + classe.getSimpleName() + " a where a.responsavel = :pessoa  order by a.id desc");
		query.setParameter("pessoa", pessoa);
		query.setFirstResult(first);
		query.setMaxResults(size);
		return query.getResultList();
	}

	public int getRowCountTecnico(Pessoa pessoa) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("SELECT count(a) from " + classe.getSimpleName() + " a where a.responsavel = :pessoa");
		query.setParameter("pessoa", pessoa);
		return Integer.parseInt(query.getSingleResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> listByTecnicoAdm(int first, int size) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("SELECT a from " + classe.getSimpleName() + " a where a.tipo = :tipo  order by a.id desc");
		query.setParameter("tipo", TipoEvento.MAPAMODALIDADE);
		query.setFirstResult(first);
		query.setMaxResults(size);
		return query.getResultList();
	}

	@Override
	public int getRowCountTecnicoAdm() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT count(a) from " + classe.getSimpleName() + " a where a.tipo = :tipo");
		query.setParameter("tipo", TipoEvento.MAPAMODALIDADE);
		return Integer.parseInt(query.getSingleResult().toString());
	}

	@SuppressWarnings("unchecked")
	public List<Evento> listByNomeEvPessoa(String nome, Pessoa pessoa) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from " + classe.getSimpleName()
				+ " u where u.responsavel = :pessoa and lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setParameter("pessoa", pessoa);
		q.setMaxResults(50);

		return q.getResultList();
	}

}
