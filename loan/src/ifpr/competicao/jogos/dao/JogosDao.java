package ifpr.competicao.jogos.dao;

import ifpr.competicao.jogos.Jogos;
import ifpr.dao.Dao;

import java.util.List;

public interface JogosDao extends Dao<Jogos> {
	public boolean existeJogosAno(int ano);
	
	public Jogos pegarJogosAno(int ano);
	
	public List<Jogos> listarJogosMenu();
}
