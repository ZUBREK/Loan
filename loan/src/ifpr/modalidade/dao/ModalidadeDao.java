package ifpr.modalidade.dao;

import ifpr.dao.Dao;
import ifpr.modalidade.Modalidade;

import java.util.List;

public interface ModalidadeDao extends Dao<Modalidade> {
	
	public List<Modalidade> pesquisarPorNome(String nome);
	
	public List<Modalidade> listarAlfabetica();
	
}
