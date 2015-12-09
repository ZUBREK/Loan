package ifpr.competicao.jogos.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ifpr.competicao.jogos.Jogos;
import ifpr.dao.GenericDao;

@ManagedBean(name = "jogosDao")
@ApplicationScoped
public class JogosDaoImpl extends GenericDao<Jogos> implements JogosDao {

	private static final long serialVersionUID = 1L;

	public JogosDaoImpl() {
		super(Jogos.class);
	}

}
