package ifpr.cadastroUsuarios;

import ifpr.pessoa.Pessoa;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.pessoa.secretario.Secretario;
import ifpr.pessoa.secretario.dao.SecretarioDao;
import ifpr.pessoa.tecnicoAdministrativo.dao.TecnicoAdministrativoDao;
import ifpr.pessoa.tecnicoEsportivo.dao.TecnicoEsportivoDao;
import ifpr.utils.Paths;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

@ManagedBean(name = "cadastroValidator")
@ViewScoped
public class CadastroUsuarioValidator {

	private Pessoa pessoa;

	@ManagedProperty(value = "#{secretarioDao}")
	private SecretarioDao secretarioDao;

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	@ManagedProperty(value = "#{tecnicoEsportivoDao}")
	private TecnicoEsportivoDao tecEspDao;

	@ManagedProperty(value = "#{tecnicoAdmDao}")
	private TecnicoAdministrativoDao tecAdmDao;

	@ManagedProperty(value = "#{coordenadorDao}")
	private CoordenadorDao coordenadorDao;

	private boolean isEmailValid(String email) {
		if ((email == null) || (email.trim().length() == 0))
			return false;

		String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
		Pattern pattern = Pattern.compile(emailPattern,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public void enviarEmail() {
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
			email.setMsg("Olá " + pessoa.getNome()
					+ ", \n\n Você foi cadastrado no Sistema Loan como um "
					+ pessoa.getTipo().getLabel()
					+ " \n Para acessar o sistema seus dados são:"
					+ " \n\n\t Login de acesso: " + pessoa.getLogin()
					+ " \n\t Senha: " + pessoa.getSenha()
					+ "\n\n Para acessar o site clique aqui: "
					+ Paths.SISTEMA_LOAN + " \n\n Atenciosamente \n Loan Team");

			email.addTo(pessoa.getLogin());

			email.send();
		} catch (EmailException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Falha no envio do e-mail!");
			context.addMessage("Atenção", message);
			context.validationFailed();
		}
	}

	public boolean validarDados(String siape) {
		boolean isValid = true;

		if (!validarEmail(pessoa)) {
			isValid = false;
		}

		if (!validarSiape(siape)) {
			isValid = false;
		}

		return isValid;
	}

	public boolean validarDadosSec(Secretario secretario) {
		boolean isValid = true;

		if (!validarEmail(secretario)) {
			isValid = false;
		}

		if (!validarCpf1(secretario.getCpf())) {
			isValid = false;
		}

		if (!validarRg1(secretario.getRg())) {
			isValid = false;
		}

		return isValid;
	}

	public boolean validarDadosEstudante(Estudante estudante) {
		boolean isValid = true;

		if (!validarEmail(estudante)) {
			isValid = false;
		}

		if (!validarCpf2(estudante.getCpf())) {
			isValid = false;
		}

		if (!validarMatricula(estudante)) {
			isValid = false;
		}
		if (!validarRg2(estudante.getRg())) {
			isValid = false;
		}

		return isValid;
	}

	private boolean validarRg1(String rg) {
		String regex = "\\d+";
		if (!rg.matches(regex)) {

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite apenas os números do RG!");

			context.addMessage("Atenção", message);
			context.validationFailed();
			UIInput input = (UIInput) context.getViewRoot().findComponent(
					":dados_form:rg");
			input.setValid(false);

			return false;
		}
		return true;
	}

	private boolean validarRg2(String rg) {
		String regex = "\\d+";
		if (!rg.matches(regex)) {

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite apenas os números do RG!");

			context.addMessage("Atenção", message);
			context.validationFailed();
			UIInput input = (UIInput) context.getViewRoot().findComponent(
					"tabGrande:dados_form:rg");
			input.setValid(false);

			return false;
		}
		return true;
	}

	private boolean validarSiape(String siape) {
		boolean isSiapeValido = true;
		if (!isNovoSiape(siape))
			isSiapeValido = false;
		if (siape.length() != 7) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Siape com tamanho indevido, use 7 números!");

			context.addMessage("Atenção", message);
			context.validationFailed();

			UIInput input = (UIInput) context.getViewRoot().findComponent(
					":dados_form:siape");
			input.setValid(false);
			isSiapeValido = false;
		}
		String regex = "\\d+";
		if (!siape.matches(regex)) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Siape deve conter apenas números!");

			context.addMessage("Atenção", message);
			context.validationFailed();

			UIInput input = (UIInput) context.getViewRoot().findComponent(
					":dados_form:siape");
			input.setValid(false);
			isSiapeValido = false;
		}
		return isSiapeValido;
	}

	private boolean isNovoSiape(String siape) {
		try {
			if (pessoa.getId() == null) {
				tecAdmDao.pesquisarPorSiape(siape);

			} else {
				tecAdmDao.pesquisarPorSiape(siape, pessoa.getId());
			}

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Siape já cadastrado!");

			context.addMessage("Atenção", message);
			context.validationFailed();

			UIInput input = (UIInput) context.getViewRoot().findComponent(
					":dados_form:siape");
			input.setValid(false);

			return false;
		} catch (NoResultException e) {
			try {

				if (pessoa.getId() == null) {
					tecEspDao.pesquisarPorSiape(siape);
				} else {
					tecEspDao.pesquisarPorSiape(siape, pessoa.getId());
				}

				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro!",
						"Siape já cadastrado!");

				context.addMessage("Atenção", message);
				context.validationFailed();

				UIInput input = (UIInput) context.getViewRoot().findComponent(
						":dados_form:siape");
				input.setValid(false);

				return false;
			} catch (Exception ex) {
				try {

					if (pessoa.getId() == null) {
						coordenadorDao.pesquisarPorSiape(siape);
					} else {
						coordenadorDao.pesquisarPorSiape(siape, pessoa.getId());
					}

					FacesContext context = FacesContext.getCurrentInstance();
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Erro!",
							"Siape já cadastrado!");

					context.addMessage("Atenção", message);
					context.validationFailed();

					UIInput input = (UIInput) context.getViewRoot()
							.findComponent(":dados_form:siape");
					input.setValid(false);

					return false;
				} catch (Exception exc) {
					return true;
				}
			}

		}
	}

	private boolean validarMatricula(Estudante estudante) {

		try {
			if (estudante.getId() == null) {
				estudanteDao.pesquisarPorMatricula(estudante.getMatricula());
			} else {
				estudanteDao.pesquisarPorMatricula(estudante.getMatricula(),
						estudante.getId());
			}

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Matrícula já cadastrada!");

			context.addMessage("Atenção", message);
			context.validationFailed();

			UIInput input = (UIInput) context.getViewRoot().findComponent(
					"tabGrande:dados_form:matricula");
			input.setValid(false);

			return false;
		} catch (NoResultException e) {
			return true;
		}

	}

	public boolean validarCpf1(String CPFOrig) {
		String cpf = onlyNumbers(CPFOrig);
		if (!isCpf(onlyNumbers(cpf))) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite um CPF válido!");
			context.addMessage("Atenção", message);
			context.validationFailed();
			return false;
		}
		if (!isNovoCpf1(CPFOrig)) {
			return false;
		}

		return true;
	}

	public boolean validarCpf2(String CPFOrig) {
		String cpf = onlyNumbers(CPFOrig);
		if (!isCpf(onlyNumbers(cpf))) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite um CPF válido!");
			context.addMessage("Atenção", message);
			context.validationFailed();
			return false;
		}
		if (!isNovoCpf2(CPFOrig)) {
			return false;
		}

		return true;
	}

	private String onlyNumbers(String str) {
		if (str != null) {
			return str.replaceAll("[^0123456789]", "");
		} else {
			return "";
		}
	}

	private boolean isNovoCpf1(String cpf) {
		try {
			if (pessoa.getId() == null) {
				estudanteDao.pesquisarPorCpf(cpf);

			} else {
				estudanteDao.pesquisarPorCpf(cpf, pessoa.getId());
			}

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!", "CPF já cadastrado!");

			context.addMessage("Atenção", message);
			context.validationFailed();

			UIInput input = (UIInput) context.getViewRoot().findComponent(
					":dados_form:cpf");
			input.setValid(false);

			return false;
		} catch (NoResultException e) {
			try {

				if (pessoa.getId() == null) {
					secretarioDao.pesquisarPorCpf(cpf);
				} else {
					secretarioDao.pesquisarPorCpf(cpf, pessoa.getId());
				}

				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro!",
						"CPF já cadastrado!");

				context.addMessage("Atenção", message);
				context.validationFailed();

				UIInput input = (UIInput) context.getViewRoot().findComponent(
						":dados_form:cpf");
				input.setValid(false);

				return false;
			} catch (Exception ex) {
				return true;
			}

		}

	}

	private boolean isNovoCpf2(String cpf) {
		try {
			if (pessoa.getId() == null) {
				estudanteDao.pesquisarPorCpf(cpf);

			} else {
				estudanteDao.pesquisarPorCpf(cpf, pessoa.getId());
			}

			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!", "CPF já cadastrado!");

			context.addMessage("Atenção", message);
			context.validationFailed();

			UIInput input = (UIInput) context.getViewRoot().findComponent(
					"tabGrande:dados_form:cpf");
			input.setValid(false);

			return false;
		} catch (NoResultException e) {
			try {

				if (pessoa.getId() == null) {
					secretarioDao.pesquisarPorCpf(cpf);
				} else {
					secretarioDao.pesquisarPorCpf(cpf, pessoa.getId());
				}

				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro!",
						"CPF já cadastrado!");

				context.addMessage("Atenção", message);
				context.validationFailed();

				UIInput input = (UIInput) context.getViewRoot().findComponent(
						":dados_form:cpf");
				input.setValid(false);

				return false;
			} catch (Exception ex) {
				return true;
			}

		}

	}

	private boolean isCpf(String CPF) {
		if (CPF.equals("00000000000") || CPF.equals("11111111111")
				|| CPF.equals("22222222222") || CPF.equals("33333333333")
				|| CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777")
				|| CPF.equals("88888888888") || CPF.equals("99999999999")
				|| (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		try {

			sm = 0;
			peso = 10;

			for (i = 0; i < 9; i++) {

				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);

			if ((r == 10) || (r == 11)) {

				dig10 = '0';

			} else {
				dig10 = (char) (r + 48);
			}

			sm = 0;
			peso = 11;

			for (i = 0; i < 10; i++) {

				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;

			}

			r = 11 - (sm % 11);

			if ((r == 10) || (r == 11)) {

				dig11 = '0';

			} else {

				dig11 = (char) (r + 48);
			}

			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
				return (true);
			} else {
				return (false);
			}

		} catch (InputMismatchException erro) {
			return (false);
		}

	}

	public boolean validarEmail(Pessoa pessoa) {
		if (!isEmailValid(pessoa.getLogin())) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite um e-mail válido!");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("Atenção", message);
			context.validationFailed();
			return false;
		}
		try {
			pessoaDao.findByLogin(pessoa.getLogin());
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"E-mail já existe, cadastre outro!");
			FacesContext.getCurrentInstance().addMessage("Atenção", message);
			FacesContext.getCurrentInstance().validationFailed();

			return false;
		} catch (NoResultException nre) {
			return true;
		}

	}

	public boolean validarEmail(String email) {
		if (!isEmailValid(email)) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Digite um e-mail válido!");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("Atenção", message);
			context.validationFailed();
			return false;
		}
		try {
			pessoaDao.findByLogin(email);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"E-mail já existe, cadastre outro!");
			FacesContext.getCurrentInstance().addMessage("Atenção", message);
			FacesContext.getCurrentInstance().validationFailed();

			return false;
		} catch (NoResultException nre) {
			return true;
		}

	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public SecretarioDao getSecretarioDao() {
		return secretarioDao;
	}

	public void setSecretarioDao(SecretarioDao secretarioDao) {
		this.secretarioDao = secretarioDao;
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public TecnicoEsportivoDao getTecEspDao() {
		return tecEspDao;
	}

	public void setTecEspDao(TecnicoEsportivoDao tecEspDao) {
		this.tecEspDao = tecEspDao;
	}

	public TecnicoAdministrativoDao getTecAdmDao() {
		return tecAdmDao;
	}

	public void setTecAdmDao(TecnicoAdministrativoDao tecAdmDao) {
		this.tecAdmDao = tecAdmDao;
	}

	public CoordenadorDao getCoordenadorDao() {
		return coordenadorDao;
	}

	public void setCoordenadorDao(CoordenadorDao coordenadorDao) {
		this.coordenadorDao = coordenadorDao;
	}

}