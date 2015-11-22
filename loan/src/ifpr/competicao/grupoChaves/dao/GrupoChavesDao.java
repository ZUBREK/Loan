package ifpr.competicao.grupoChaves.dao;

import java.util.List;

import ifpr.competicao.grupoChaves.GrupoChaves;
import ifpr.dao.Dao;
import ifpr.modalidade.Modalidade;

public interface GrupoChavesDao extends Dao<GrupoChaves> {

	public List<GrupoChaves> pesquisarPorNome(String nome);

	public boolean hasChave(Modalidade modalidade);
}
