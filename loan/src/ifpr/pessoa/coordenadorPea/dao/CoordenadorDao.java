package ifpr.pessoa.coordenadorPea.dao;

import ifpr.dao.Dao;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;

public interface CoordenadorDao extends Dao<CoordenadorPea> {
	
	
	public CoordenadorPea pesquisarPorSiape(String siape);
	
	public CoordenadorPea pesquisarPorSiape(String siape, int id);

}
