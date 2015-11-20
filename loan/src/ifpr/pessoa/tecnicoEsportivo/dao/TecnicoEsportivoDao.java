package ifpr.pessoa.tecnicoEsportivo.dao;

import ifpr.dao.Dao;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

public interface TecnicoEsportivoDao extends Dao<TecnicoEsportivo> {

	public TecnicoEsportivo pesquisarPorSiape(String siape);

	public TecnicoEsportivo pesquisarPorSiape(String siape, int id);
}
