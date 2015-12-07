package ifpr.dao;

import ifpr.campus.Campus;
import ifpr.modalidade.Modalidade;

import java.util.List;

import javax.persistence.EntityManagerFactory;

public interface Dao<T> {
	public void salvar(T obj);

	public T update(T obj);

	public void remover(T obj) throws Exception;

	public T findById(int id);

	public T findByIdInteger(Integer id);

	public T findById(long id);

	public List<T> listDesc();

	public List<T> listAsc();

	public List<T> findByNome(String nome);

	public List<T> findByNomeRole(String nome, String role);

	public List<T> listarPessoaByCampusEmAlfabetica(Campus campus);

	public void setEntityManagerFactory(EntityManagerFactory emf);

	public List<T> list(int first, int size);

	public int getRowCount();

	public List<T> listarPorCampusModalidade(Campus campus,
			Modalidade modalidade);

}