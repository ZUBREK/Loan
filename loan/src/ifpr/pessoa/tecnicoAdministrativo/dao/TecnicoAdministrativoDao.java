package ifpr.pessoa.tecnicoAdministrativo.dao;

import ifpr.dao.Dao;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;

public interface TecnicoAdministrativoDao extends Dao<TecnicoAdministrativo> {

	public TecnicoAdministrativo pesquisarPorSiape(String siape);

	public TecnicoAdministrativo pesquisarPorSiape(String siape, int id);

}
