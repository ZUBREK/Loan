package ifpr.competicao.partidaTimePlacar.dao;

import ifpr.competicao.partidaTimePlacar.PartidaTimePlacar;
import ifpr.dao.GenericDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "partidaTimePlacarDao")
@ApplicationScoped
public class PartidaTimePlacarDaoImpl extends GenericDao<PartidaTimePlacar> implements PartidaTimePlacarDao {

	private static final long serialVersionUID = 1L;

	public PartidaTimePlacarDaoImpl() {
		super(PartidaTimePlacar.class);
	}

}
