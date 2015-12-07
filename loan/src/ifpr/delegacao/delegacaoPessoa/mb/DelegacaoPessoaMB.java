package ifpr.delegacao.delegacaoPessoa.mb;

import ifpr.delegacao.Delegacao;
import ifpr.delegacao.delegacaoPessoa.DelegacaoPessoa;
import ifpr.delegacao.delegacaoPessoa.dao.DelegacaoPessoaDao;
import ifpr.pessoa.Pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "delegacaoPessoaMB")
@ViewScoped
public class DelegacaoPessoaMB {

	private DelegacaoPessoa delegacaoPessoa;

	@ManagedProperty(value = "#{delegacaoPessoaDao}")
	private DelegacaoPessoaDao delegacaoPessoaDao;

	private List<DelegacaoPessoa> delegacaoPessoaFiltered;

	private Pessoa pessoa;
	private Delegacao delegacao;
	private boolean chefe;
	
	public DelegacaoPessoaMB() {
		delegacaoPessoaFiltered = new ArrayList<DelegacaoPessoa>();
	}

	@PostConstruct
	public void poust() {
	}

	public void remover() {
		try {
			delegacaoPessoaDao.remover(delegacaoPessoa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cancelar() {
		delegacaoPessoa = null;
	}

	public void salvar() {
		if (delegacaoPessoa.getId() != null) {
			delegacaoPessoaDao.update(delegacaoPessoa);
		} else {
			delegacaoPessoa.setPessoa(pessoa);
			delegacaoPessoa.setDelegacao(delegacao);
			delegacaoPessoa.setChefe(chefe);
			delegacaoPessoaDao.salvar(delegacaoPessoa);
		}
	}
	
	public DelegacaoPessoa getDelegacaoPessoa() {
		return delegacaoPessoa;
	}

	public void setDelegacaoPessoa(DelegacaoPessoa delegacaoPessoa) {
		this.delegacaoPessoa = delegacaoPessoa;
	}

	public DelegacaoPessoaDao getDelegacaoPessoaDao() {
		return delegacaoPessoaDao;
	}

	public void setDelegacaoPessoaDao(DelegacaoPessoaDao delegacaoPessoaDao) {
		this.delegacaoPessoaDao = delegacaoPessoaDao;
	}

	public List<DelegacaoPessoa> getDelegacaoPessoaFiltered() {
		return delegacaoPessoaFiltered;
	}

	public void setDelegacaoPessoaFiltered(
			List<DelegacaoPessoa> timeEstudanteFiltered) {
		this.delegacaoPessoaFiltered = timeEstudanteFiltered;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Delegacao getDelegacao() {
		return delegacao;
	}

	public void setDelegacao(Delegacao delegacao) {
		this.delegacao = delegacao;
	}

	public boolean isChefe() {
		return chefe;
	}

	public void setChefe(boolean chefe) {
		this.chefe = chefe;
	}
	
	
}
