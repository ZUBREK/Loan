package ifpr.noticia.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ifpr.dao.GenericDao;
import ifpr.noticia.Noticia;

@ManagedBean(name="noticiaDao")
@ApplicationScoped
public class NoticiaDaoImpl  extends GenericDao<Noticia> implements NoticiaDao {
	
	private static final long serialVersionUID = 1L;
	
	public NoticiaDaoImpl()
	{
		super(Noticia.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Noticia> findByTitulo(String titulo) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT a from Noticia a where lower(a.titulo) like concat('%', :titulo, '%')");
		query.setParameter("titulo", titulo);
		query.setMaxResults(10);
		return query.getResultList();
	}

}
	