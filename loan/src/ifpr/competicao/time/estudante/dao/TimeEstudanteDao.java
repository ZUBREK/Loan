package ifpr.competicao.time.estudante.dao;

import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.dao.Dao;

import java.util.List;

public interface TimeEstudanteDao extends Dao<TimeEstudante> {
	
	public List<TimeEstudante> pesquisarPorNome(String nome);
	
	
	
}
