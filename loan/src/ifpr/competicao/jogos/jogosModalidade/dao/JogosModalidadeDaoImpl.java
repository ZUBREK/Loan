package ifpr.competicao.jogos.jogosModalidade.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ifpr.competicao.jogos.jogosModalidade.JogosModalidade;
import ifpr.dao.GenericDao;

@ManagedBean(name = "jogosModalidadeDao")
@ApplicationScoped
public class JogosModalidadeDaoImpl extends GenericDao<JogosModalidade> implements JogosModalidadeDao {

	private static final long serialVersionUID = 1L;

	public JogosModalidadeDaoImpl() {
		super(JogosModalidade.class);
	}

}
