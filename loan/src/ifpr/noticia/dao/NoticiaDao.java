package ifpr.noticia.dao;

import java.util.List;

import ifpr.dao.Dao;
import ifpr.noticia.Noticia;

public interface NoticiaDao extends Dao<Noticia> {
	public 	List<Noticia> findByTitulo(String titulo);
}
