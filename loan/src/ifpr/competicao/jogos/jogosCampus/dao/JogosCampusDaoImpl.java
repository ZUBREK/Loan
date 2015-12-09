package ifpr.competicao.jogos.jogosCampus.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ifpr.competicao.jogos.jogosCampus.JogosCampus;
import ifpr.dao.GenericDao;

@ManagedBean(name = "jogosCampusDao")
@ApplicationScoped
public class JogosCampusDaoImpl extends GenericDao<JogosCampus> implements JogosCampusDao {

	private static final long serialVersionUID = 1L;

	public JogosCampusDaoImpl() {
		super(JogosCampus.class);
	}

}
