package ifpr.pessoa.dao;

import java.util.List;

import ifpr.dao.Dao;
import ifpr.delegacao.Delegacao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;

public interface PessoaDao extends Dao<Pessoa> {
	
	public List<Pessoa> pesquisarPorNome(String nome);
		
	public Pessoa findByLogin(String login);
	
	public List<Pessoa> findByRole(TipoPessoa tipo);

	public List<Pessoa> pesquisarPorNomeParaDelegacao(String nome, Delegacao delegacao);
	
	
}
