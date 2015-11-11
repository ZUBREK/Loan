package ifpr.competicao.chave.dao;

import ifpr.competicao.chave.Chave;
import ifpr.dao.Dao;

import java.util.List;

public interface ChaveDao extends Dao<Chave> {

	public List<Chave> pesquisarPorNome(String nome);

}
