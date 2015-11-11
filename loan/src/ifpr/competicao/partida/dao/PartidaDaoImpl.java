package ifpr.competicao.partida.dao;

import ifpr.competicao.partida.Partida;
import ifpr.dao.GenericDao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "partidaDao")
@ApplicationScoped
public class PartidaDaoImpl extends GenericDao<Partida> implements PartidaDao {

	private static final long serialVersionUID = 1L;

	public PartidaDaoImpl() {
		super(Partida.class);
	}

	

}
