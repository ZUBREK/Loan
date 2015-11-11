package ifpr.competicao.placar.dao;

import ifpr.competicao.placar.Placar;
import ifpr.dao.GenericDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "placarDao")
@ApplicationScoped
public class PlacarDaoImpl extends GenericDao<Placar> implements PlacarDao {

	private static final long serialVersionUID = 1L;

	public PlacarDaoImpl() {
		super(Placar.class);
	}

	

}
