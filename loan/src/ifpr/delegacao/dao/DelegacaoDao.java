package ifpr.delegacao.dao;

import ifpr.dao.Dao;
import ifpr.delegacao.Delegacao;

import java.util.List;

public interface DelegacaoDao extends Dao<Delegacao> {
	
	public List<Delegacao> pesquisarPorNome(String nome);
	
	
	
}
