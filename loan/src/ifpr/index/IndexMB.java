package ifpr.index;

import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.criptografia.Conversion;
import ifpr.model.LoginControllerMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.utils.Paths;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean(name = "indexMB")
@ViewScoped
public class IndexMB {

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	private String email;

	private String senhaAtual;

	private String senhaNova;

	private LoginControllerMB loginController;

	@ManagedProperty(value = "#{cadastroValidator}")
	private CadastroUsuarioValidator validator;

	public void mudarSenha() {
		try {
			Pessoa pessoa = pessoaDao.findByLogin(email);
			pessoa.setSenha(gerarSenha());

			enviarEmail(pessoa);
			pessoa.setSenha(criptografar(pessoa.getSenha()));

			pessoaDao.update(pessoa);
		} catch (NoResultException ex) {

		}
		email = null;
	}

	public void trocarSenha() {
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(
				context, "#{loginControllerMB}", LoginControllerMB.class);
		Pessoa pessoa = loginController.getPessoaLogada();

		if (senhaNova.length() < 7) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Senha muito curta, Minímo de 7 caracteres!");
			context.addMessage("Atenção", message);
			context.validationFailed();

			return;
		}
		senhaAtual = criptografar(senhaAtual);
		if (!pessoa.getSenha().equals(senhaAtual)) {

			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Senha atual incorreta!");
			context.addMessage("Atenção", message);
			context.validationFailed();

			return;
		}

		pessoa.setSenha(criptografar(senhaNova));
		pessoaDao.update(pessoa);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Sucesso!", "Senha alterada com sucesso!");
		context.addMessage("Sucesso!", message);

		senhaAtual = null;
		senhaNova = null;
		return;

	}

	public void mudarEmail() {

		if (validator.validarEmail(email)) {
			FacesContext context = FacesContext.getCurrentInstance();
			loginController = context.getApplication().evaluateExpressionGet(
					context, "#{loginControllerMB}", LoginControllerMB.class);
			Pessoa pessoa = loginController.getPessoaLogada();

			pessoa.setLogin(email);
			pessoaDao.update(pessoa);

			SecurityContextHolder.clearContext();
		}
		email = null;
		return;

	}

	private void enviarEmail(Pessoa pessoa) {
		try {

			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(454);
			email.setAuthenticator(new DefaultAuthenticator(
					"notificador.loan@gmail.com", "sysloan123"));
			email.setSSLOnConnect(true);
			// [ADMINISTRADOR LOAN]
			email.setFrom("notificador.loan@gmail.com");

			email.setSubject("[LOAN] - Nova Senha");
			// gerar senha
			email.setMsg("Olá " + pessoa.getNome() + ", \n\n Sua nova senha é:"
					+ pessoa.getSenha()
					+ "\n\n Para acessar o site clique aqui: "
					+ Paths.SISTEMA_LOAN + " \n\n Atenciosamente \n Loan Team");

			email.addTo(pessoa.getLogin());

			email.send();
		} catch (EmailException e) {
		}
	}

	private String criptografar(String senha) {
		String retorno = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(senha.getBytes());
			retorno = Conversion.byteArrayToHexString(md.digest());

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return retorno;
	}

	private String gerarSenha() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().substring(0, 7);

	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public CadastroUsuarioValidator getValidator() {
		return validator;
	}

	public void setValidator(CadastroUsuarioValidator validator) {
		this.validator = validator;
	}

}
