package ifpr.competicao.partidaTime.dao;

import ifpr.competicao.partidaTime.PartidaTime;
import ifpr.dao.GenericDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "partidaTimeDao")
@ApplicationScoped
public class PartidaTimeDaoImpl extends GenericDao<PartidaTime> implements PartidaTimeDao {

	private static final long serialVersionUID = 1L;

	public PartidaTimeDaoImpl() {
		super(PartidaTime.class);
	}

}
