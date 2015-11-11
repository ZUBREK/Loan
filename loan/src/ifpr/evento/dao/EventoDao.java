package ifpr.evento.dao;

import ifpr.dao.Dao;
import ifpr.evento.Evento;

import java.util.List;

public interface EventoDao extends Dao<Evento> {
	
	public List<Evento> pesquisarPorNome(String nome);
	
	
	
}
