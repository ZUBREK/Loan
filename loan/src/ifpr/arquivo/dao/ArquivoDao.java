package ifpr.arquivo.dao;

import ifpr.arquivo.Arquivo;
import ifpr.dao.Dao;
import ifpr.pessoa.Pessoa;

import java.util.List;

public interface ArquivoDao extends Dao<Arquivo> {
	
	public List<Arquivo> pesquisarPorNome(String nome);
	
	public Arquivo pesquisarFotoPerfil(Pessoa pessoa);
	
}
