package ifpr.cadastroUsuarios;

import ifpr.pessoa.Pessoa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;




public class CadastroUsuarioValidator implements Runnable{

	private final static String LINK_ACESSO = "http://localhost:8080/loan";
	
	private Pessoa pessoa;
	
	private  boolean isEmailValid(String email) {
		if ((email == null) || (email.trim().length() == 0))
			return false;

		String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
		Pattern pattern = Pattern.compile(emailPattern,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	private void enviarEmail() {
		try {

			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(454);
			email.setAuthenticator(new DefaultAuthenticator(
					"notificador.loan@gmail.com", "sysloan123"));
			email.setSSLOnConnect(true);
			// [ADMINISTRADOR LOAN]
			email.setFrom("notificador.loan@gmail.com");

			email.setSubject("[LOAN] - Novo cadastro");
			// gerar senha

			email.setMsg("Ol� "
					+ pessoa.getNome()
					+ ", \n\n Voc� foi cadastrado no Sistema Loan como um "+ pessoa.getTipo().getLabel()
					+ " \n Para acessar o sistema seus dados s�o:"
					+ " \n\n\t Login de acesso: " + pessoa.getLogin()
					+ " \n\t Senha: " + pessoa.getSenha()
					+ "\n\n Para acessar o site clique aqui: "
					+ LINK_ACESSO + " \n\n Atenciosamente \n Loan Team");

			email.addTo(pessoa.getLogin());

			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	public boolean validarEmail(Pessoa pessoa){
		if(!isEmailValid(pessoa.getLogin())){
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite um e-mail v�lido!");
			FacesContext.getCurrentInstance().addMessage("Aten��o", message);
			FacesContext.getCurrentInstance().validationFailed();	
			return false;
		}
		
		return true;
	}

	@Override
	public void run() {
		enviarEmail();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
}