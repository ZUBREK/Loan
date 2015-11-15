package ifpr.competicao.time.dao;

import java.util.List;

import ifpr.competicao.time.Time;
import ifpr.dao.Dao;
import ifpr.modalidade.Modalidade;

public interface TimeDao extends Dao<Time> {
	
	public List<Time> pesquisarPorNome(String nome);
	public List<Time> pesquisarPorModalidade(Modalidade modalidade);
	
	
}
