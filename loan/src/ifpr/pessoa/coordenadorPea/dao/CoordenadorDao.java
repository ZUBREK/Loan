package ifpr.pessoa.coordenadorPea.dao;

import ifpr.campus.Campus;
import ifpr.dao.Dao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;

import java.util.List;

public interface CoordenadorDao extends Dao<CoordenadorPea> {

	public CoordenadorPea pesquisarPorSiape(String siape);

	public CoordenadorPea pesquisarPorSiape(String siape, int id);

	public List<CoordenadorPea> findByNomeRole(String nome);
	
	public List<Pessoa> pesquisarTecCoordPorCampus(Campus campus);

}
