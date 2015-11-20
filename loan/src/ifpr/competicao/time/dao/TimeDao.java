package ifpr.competicao.time.dao;

import ifpr.competicao.time.Time;
import ifpr.dao.Dao;
import ifpr.modalidade.Modalidade;
import ifpr.pessoa.estudante.Estudante;

import java.util.List;

public interface TimeDao extends Dao<Time> {
	
	public List<Time> pesquisarPorNome(String nome);
	
	public List<Time> pesquisarPorModalidade(Modalidade modalidade);
	
	public List<Estudante> listarEstudantes(Time time);
	
}
