package ifpr.model;

import ifpr.pessoa.Pessoa;
import ifpr.pessoa.dao.PessoaDao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ManagedBean(name = "loginControllerMB")
@RequestScoped
public class LoginControllerMB {

	private Pessoa pessoaLogada;
	
	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;
	
	public Pessoa getPessoaLogada() {
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		pessoaLogada = pessoaDao.findByLogin(user.getUsername());
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public boolean isLogado() {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
			return false;
		}
		return true;
	}
}
