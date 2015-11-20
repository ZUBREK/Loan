package ifpr.pessoa.dao;

import ifpr.dao.Dao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;

import java.util.List;

public interface PessoaDao extends Dao<Pessoa> {
	
	public List<Pessoa> pesquisarPorNome(String nome);
		
	public Pessoa findByLogin(String login);
	
	public List<Pessoa> findByRole(TipoPessoa tipo);
	
	
}
