package ifpr.pessoa.tecnicoEsportivo.dao;

import ifpr.dao.GenericDao;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="tecnicoEsportivoDao")
@ApplicationScoped
public class TecnicoEsportivoImpl  extends GenericDao<TecnicoEsportivo> implements TecnicoEsportivoDao {
	
	private static final long serialVersionUID = 1L;
	
	public TecnicoEsportivoImpl()
	{
		super(TecnicoEsportivo.class);
	}
}
	