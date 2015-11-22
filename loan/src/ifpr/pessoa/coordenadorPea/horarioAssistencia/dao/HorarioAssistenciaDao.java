package ifpr.pessoa.coordenadorPea.horarioAssistencia.dao;

import java.util.List;

import ifpr.dao.Dao;
import ifpr.pessoa.coordenadorPea.horarioAssistencia.HorarioAssistencia;

public interface HorarioAssistenciaDao extends Dao<HorarioAssistencia> {
	
	public List<HorarioAssistencia> pesquisarPorNome(String nome);
	
	public 	List<HorarioAssistencia> findByMateria(String materia);
}
