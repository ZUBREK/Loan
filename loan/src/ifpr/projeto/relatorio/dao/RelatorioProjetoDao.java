package ifpr.projeto.relatorio.dao;

import ifpr.dao.Dao;
import ifpr.projeto.Projeto;
import ifpr.projeto.relatorio.RelatorioProjeto;

import java.util.List;

public interface RelatorioProjetoDao extends Dao<RelatorioProjeto>{

	
	public List<RelatorioProjeto> pesquisarPorProjeto(Projeto projeto);
}
