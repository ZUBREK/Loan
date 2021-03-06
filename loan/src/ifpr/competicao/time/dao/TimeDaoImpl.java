package ifpr.competicao.time.dao;

import ifpr.campus.Campus;
import ifpr.competicao.time.Time;
import ifpr.dao.GenericDao;
import ifpr.modalidade.Modalidade;
import ifpr.pessoa.estudante.Estudante;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ManagedBean(name = "timeDao")
@ApplicationScoped
public class TimeDaoImpl extends GenericDao<Time> implements TimeDao {

	private static final long serialVersionUID = 1L;

	public TimeDaoImpl() {
		super(Time.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Time> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Time u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Time> pesquisarPorModalidade(Modalidade modalidade) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Time u where modalidade = :modalidade");
		q.setParameter("modalidade", modalidade);
		q.setMaxResults(10);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Estudante> listarEstudantes(Time time) {

		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select t from TimeEstudante as te inner join te.estudante"
						+ " as t where te.time = :time ");
		q.setParameter("time", time);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Time> listarTimesPorCampusModalidade(Campus campus,
			Modalidade modalidade) {

		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select e from Time e where e.campus = :campus and e.modalidade = :modalidade order by e.nome");
		q.setParameter("campus", campus);
		q.setParameter("modalidade", modalidade);
		q.setMaxResults(100);

		return q.getResultList();
	}

}
