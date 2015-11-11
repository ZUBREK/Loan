package ifpr.competicao.partida.local.dao;

import ifpr.competicao.partida.local.Local;
import ifpr.dao.Dao;

import java.util.List;

public interface LocalDao extends Dao<Local> {

	public List<Local> pesquisarPorNome(String nome);

}
