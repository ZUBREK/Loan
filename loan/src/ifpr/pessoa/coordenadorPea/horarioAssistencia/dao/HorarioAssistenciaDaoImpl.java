package ifpr.pessoa.coordenadorPea.horarioAssistencia.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ifpr.dao.GenericDao;
import ifpr.pessoa.coordenadorPea.horarioAssistencia.HorarioAssistencia;

@ManagedBean(name = "horarioAssistenciaDao")
@ApplicationScoped
public class HorarioAssistenciaDaoImpl extends GenericDao<HorarioAssistencia> implements HorarioAssistenciaDao {

	private static final long serialVersionUID = 1L;

	public HorarioAssistenciaDaoImpl() {
		super(HorarioAssistencia.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HorarioAssistencia> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from HorarioAssistencia u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HorarioAssistencia> findByMateria(String materia) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from HorarioAssistencia a where lower(a.materia) like concat('%', :materia, '%')");
		query.setParameter("materia", materia);
		query.setMaxResults(10);
		return query.getResultList();
	}
}
