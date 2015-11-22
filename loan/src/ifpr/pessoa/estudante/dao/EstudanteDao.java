package ifpr.pessoa.estudante.dao;

import ifpr.campus.Campus;
import ifpr.dao.Dao;
import ifpr.pessoa.estudante.Estudante;

import java.util.List;

public interface EstudanteDao extends Dao<Estudante> {
	public List<Estudante> pesquisarEstudanteNomeCampus(String nome, Campus campus);

	public List<Estudante> pesquisarPorNome(String nome);
	
	
	public Estudante pesquisarPorCpf(String cpf);
	
	public Estudante pesquisarPorCpf(String cpf, int id);
	
	public Estudante pesquisarPorMatricula(String matricula);
	
	public Estudante pesquisarPorMatricula(String matricula, int id);

	
}
