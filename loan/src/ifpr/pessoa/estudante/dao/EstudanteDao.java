package ifpr.pessoa.estudante.dao;

import ifpr.campus.Campus;
import ifpr.competicao.time.Time;
import ifpr.dao.Dao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.projeto.Projeto;

import java.util.List;

public interface EstudanteDao extends Dao<Estudante> {
	
	

	public List<Estudante> pesquisarPorNome(String nome);
	
	
	public Estudante pesquisarPorCpf(String cpf);
	
	public Estudante pesquisarPorCpf(String cpf, int id);
	
	public Estudante pesquisarPorMatricula(String matricula);
	
	public Estudante pesquisarPorMatricula(String matricula, int id);

	public List<Estudante> pesquisarEstudanteNomeCampusTime(String nome,
			Campus campus, Time time);

	public List<Estudante> pesquisarEstudanteNomeCampusProjeto(String nome,
			Campus campus, Projeto projeto);


	List<Estudante> pesquisarEstudanteNomeCampus(String nome, Campus campus);

	
}
