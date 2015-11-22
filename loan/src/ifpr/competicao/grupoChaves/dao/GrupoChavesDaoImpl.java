package ifpr.competicao.grupoChaves.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ifpr.competicao.grupoChaves.GrupoChaves;
import ifpr.dao.GenericDao;
import ifpr.modalidade.Modalidade;

@ManagedBean(name = "grupoChavesDao")
@ApplicationScoped
public class GrupoChavesDaoImpl extends GenericDao<GrupoChaves> implements GrupoChavesDao {

	private static final long serialVersionUID = 1L;

	public GrupoChavesDaoImpl() {
		super(GrupoChaves.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoChaves> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("select u from GrupoChaves u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);

		return q.getResultList();
	}
	
	@Override
	public boolean hasChave(Modalidade modalidade) {
		try{
			EntityManager em = emf.createEntityManager();
			Query q = em
					.createQuery("select u from GrupoChaves u where modalidade = :modalidade");
			q.setParameter("modalidade", modalidade);
			if(q.getResultList().isEmpty()){
				return false;
			} else{
				return true;
			}
		}
		catch(NoResultException ex){
			return false;
		}
	}

}
