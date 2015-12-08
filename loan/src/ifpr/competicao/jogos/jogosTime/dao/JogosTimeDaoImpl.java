package ifpr.competicao.jogos.jogosTime.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ifpr.competicao.jogos.jogosTime.JogosTime;
import ifpr.dao.GenericDao;

@ManagedBean(name = "jogosTimeDao")
@ApplicationScoped
public class JogosTimeDaoImpl extends GenericDao<JogosTime> implements JogosTimeDao {

	private static final long serialVersionUID = 1L;

	public JogosTimeDaoImpl() {
		super(JogosTime.class);
	}

}
