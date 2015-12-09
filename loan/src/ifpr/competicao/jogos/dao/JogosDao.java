package ifpr.competicao.jogos.dao;

import ifpr.competicao.jogos.Jogos;
import ifpr.dao.Dao;

public interface JogosDao extends Dao<Jogos> {
	public boolean existeJogosAno(int ano);
	
	public Jogos pegarJogosAno(int ano);
}
