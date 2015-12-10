package ifpr.pessoa.tecnicoEsportivo.dao;

import ifpr.campus.Campus;
import ifpr.dao.Dao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

import java.util.List;

public interface TecnicoEsportivoDao extends Dao<TecnicoEsportivo> {

	public TecnicoEsportivo pesquisarPorSiape(String siape);

	public TecnicoEsportivo pesquisarPorSiape(String siape, int id);
	
	public List<Pessoa> pesquisarPorCampus(Campus campus);
}
