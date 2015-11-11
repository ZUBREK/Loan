package ifpr.projeto.dao;

import ifpr.dao.Dao;
import ifpr.projeto.Projeto;

import java.util.List;

public interface ProjetoDao extends Dao<Projeto> {
	
	public List<Projeto> pesquisarPorNome(String nome);
	
	
	
}
