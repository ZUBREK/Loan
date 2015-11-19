package ifpr.competicao.time.pontos.dao;

import ifpr.competicao.time.pontos.PontosTime;
import ifpr.dao.GenericDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "pontosTimeDao")
@ApplicationScoped
public class PontosTimeDaoImpl extends GenericDao<PontosTime> implements PontosTimeDao {
	private static final long serialVersionUID = 1L;

	public PontosTimeDaoImpl() {
		super(PontosTime.class);
	}

}
