package ifpr.projeto.projetoEstudante.dao;

import ifpr.dao.Dao;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;

import java.util.List;

public interface ProjetoEstudanteDao extends Dao<ProjetoEstudante> {
	
	public List<ProjetoEstudante> pesquisarPorNome(String nome);
	
	
	
}
