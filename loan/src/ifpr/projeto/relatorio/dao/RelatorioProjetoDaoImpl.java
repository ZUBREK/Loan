package ifpr.projeto.relatorio.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ifpr.dao.GenericDao;
import ifpr.projeto.relatorio.RelatorioProjeto;

@ManagedBean(name = "relatorioProjetoDao")
@ApplicationScoped
public class RelatorioProjetoDaoImpl extends GenericDao<RelatorioProjeto> implements RelatorioProjetoDao {

	private static final long serialVersionUID = 1L;

	public RelatorioProjetoDaoImpl() {
		super(RelatorioProjeto.class);
	}

}
