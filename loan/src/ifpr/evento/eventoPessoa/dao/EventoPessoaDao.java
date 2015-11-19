package ifpr.evento.eventoPessoa.dao;

import ifpr.dao.Dao;
import ifpr.evento.Evento;
import ifpr.evento.eventoPessoa.EventoPessoa;
import ifpr.pessoa.Pessoa;

import java.util.List;

public interface EventoPessoaDao extends Dao<EventoPessoa> {
	
	
	public List<EventoPessoa> pesquisarPorEvento(Evento evento);

	public EventoPessoa pesquisarPorEventoPessoa(Evento evento,
			Pessoa pessoa);
	
	
	
}
