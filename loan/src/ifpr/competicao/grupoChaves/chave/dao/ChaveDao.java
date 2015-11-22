package ifpr.competicao.grupoChaves.chave.dao;

import java.util.List;

import ifpr.competicao.grupoChaves.chave.Chave;
import ifpr.dao.Dao;

public interface ChaveDao extends Dao<Chave> {

	public List<Chave> pesquisarPorNome(String nome);

}
