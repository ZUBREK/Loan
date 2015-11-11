package ifpr.pessoa.dao;

import ifpr.dao.Dao;
import ifpr.pessoa.Pessoa;

import java.util.List;

public interface PessoaDao extends Dao<Pessoa> {
	
	public List<Pessoa> pesquisarPorNome(String nome);
		
	public Pessoa findByLogin(String login);
}
