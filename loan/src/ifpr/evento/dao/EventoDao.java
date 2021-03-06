package ifpr.evento.dao;

import ifpr.dao.Dao;
import ifpr.evento.Evento;
import ifpr.pessoa.Pessoa;

import java.util.List;

public interface EventoDao extends Dao<Evento> {
	
	public List<Evento> pesquisarPorNome(String nome);
	
	public List<Evento> listByTecnico(int first,int size,Pessoa pessoa);

	public int getRowCountTecnico(Pessoa pessoa);
	
	public List<Evento> listByTecnicoAdm(int first,int size);

	public int getRowCountTecnicoAdm();
	
	public List<Evento> listByNomeEvPessoa(String nome,Pessoa pessoa);
}
