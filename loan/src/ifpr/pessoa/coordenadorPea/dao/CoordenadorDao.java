package ifpr.pessoa.coordenadorPea.dao;

import java.util.List;

import ifpr.dao.Dao;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;

public interface CoordenadorDao extends Dao<CoordenadorPea> {

	public CoordenadorPea pesquisarPorSiape(String siape);

	public CoordenadorPea pesquisarPorSiape(String siape, int id);

	public List<CoordenadorPea> findByNomeRole(String nome);

}
