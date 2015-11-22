package ifpr.pessoa.secretario.dao;

import ifpr.dao.Dao;
import ifpr.pessoa.secretario.Secretario;

public interface SecretarioDao extends Dao<Secretario> {
	
	
	public Secretario pesquisarPorCpf(String cpf);
		

	public Secretario pesquisarPorCpf(String cpf, int id);

}
