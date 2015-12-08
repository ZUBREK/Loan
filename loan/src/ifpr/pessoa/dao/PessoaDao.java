package ifpr.pessoa.dao;

import ifpr.campus.Campus;
import ifpr.dao.Dao;
import ifpr.delegacao.Delegacao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;

import java.util.List;

public interface PessoaDao extends Dao<Pessoa> {
	
	public List<Pessoa> pesquisarPorNome(String nome);
		
	public Pessoa findByLogin(String login);
	
	public List<Pessoa> findByRole(TipoPessoa tipo);

	public List<Pessoa> pesquisarPorNomeParaDelegacao(String nome, Delegacao delegacao);

	List<Pessoa> listarTecEspByCampusEmAlfabetica(Campus campus);
	
	List<Pessoa> listarCoordByCampusEmAlfabetica(Campus campus);
	
	
}
