package ifpr.delegacao.dao;

import ifpr.dao.Dao;
import ifpr.delegacao.Delegacao;
import ifpr.pessoa.Pessoa;

import java.util.List;

public interface DelegacaoDao extends Dao<Delegacao> {

	public List<Delegacao> pesquisarPorNome(String nome);

	public List<Pessoa> listarPessoas(Delegacao delegacao);

}
