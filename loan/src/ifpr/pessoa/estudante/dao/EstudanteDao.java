package ifpr.pessoa.estudante.dao;

import ifpr.campus.Campus;
import ifpr.dao.Dao;
import ifpr.pessoa.estudante.Estudante;

import java.util.List;

public interface EstudanteDao extends Dao<Estudante> {
	public List<Estudante> pesquisarEstudanteNomeCampus(String nome, Campus campus);

	List<Estudante> pesquisarPorNome(String nome);
}
