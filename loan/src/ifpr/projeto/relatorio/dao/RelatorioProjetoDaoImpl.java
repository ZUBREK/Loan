package ifpr.projeto.relatorio.dao;

import ifpr.dao.GenericDao;
import ifpr.projeto.Projeto;
import ifpr.projeto.relatorio.RelatorioProjeto;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;


@ManagedBean(name="relatorioProjetoDao")
@ApplicationScoped
public class RelatorioProjetoDaoImpl extends GenericDao<RelatorioProjeto>
		implements RelatorioProjetoDao {

	private static final long serialVersionUID = 1L;

	
	public RelatorioProjetoDaoImpl() {
		super(RelatorioProjeto.class);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RelatorioProjeto> pesquisarPorProjeto(Projeto projeto) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from " + classe.getSimpleName()
				+ " a where a.projeto = :projeto");
		query.setParameter("projeto", projeto);

		return query.getResultList();
	}

}
