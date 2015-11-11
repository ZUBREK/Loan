package ifpr.campus.dao;

import ifpr.campus.Campus;
import ifpr.dao.Dao;

import java.util.List;

public interface CampusDao extends Dao<Campus> {
	
	public List<Campus> pesquisarPorCidade(String nome);
	
	public List<Campus> listarAlfabetica();
	
}
