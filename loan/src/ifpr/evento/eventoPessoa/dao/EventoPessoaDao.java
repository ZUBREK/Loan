package ifpr.evento.eventoPessoa.dao;

import ifpr.dao.Dao;
import ifpr.evento.Evento;
import ifpr.evento.eventoPessoa.EventoPessoa;

import java.util.List;

public interface EventoPessoaDao extends Dao<EventoPessoa> {
	
	
	public List<EventoPessoa> pesquisarPorEvento(Evento evento);
	
	
	
}
