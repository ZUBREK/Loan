package ifpr.noticia.dao;

import ifpr.dao.GenericDao;
import ifpr.noticia.Noticia;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="noticiaDao")
@ApplicationScoped
public class NoticiaDaoImpl  extends GenericDao<Noticia> implements NoticiaDao {
	
	private static final long serialVersionUID = 1L;
	
	public NoticiaDaoImpl()
	{
		super(Noticia.class);
	}

}
	