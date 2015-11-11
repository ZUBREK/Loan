package ifpr.competicao.time.dao;

import ifpr.competicao.time.Time;
import ifpr.dao.Dao;

import java.util.List;

public interface TimeDao extends Dao<Time> {
	
	public List<Time> pesquisarPorNome(String nome);
	
	
	
}
