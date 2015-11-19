package ifpr.evento.arquivo.dao;

import ifpr.dao.Dao;
import ifpr.evento.Evento;
import ifpr.evento.arquivo.ArquivoEvento;
import ifpr.evento.eventoPessoa.EventoPessoa;

import java.util.List;

public interface ArquivoEventoDao extends Dao<ArquivoEvento>{
	
	public List<ArquivoEvento> pesquisarPorEvento(Evento evento);

	public ArquivoEvento pesquisarPorEventoPessoa(EventoPessoa eventoPessoa);
	
}
