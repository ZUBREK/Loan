package ifpr.pessoa.secretario.mb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.criptografia.Criptografia;
import ifpr.geradorPdf.CrachasPdf;
import ifpr.perfilUsuario.HomeMB;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.secretario.Secretario;
import ifpr.pessoa.secretario.dao.SecretarioDao;
import ifpr.pessoa.secretario.model.SecretarioLazyDataModel;

@ManagedBean(name = "secretarioMB")
@ViewScoped
public class SecretarioMB {

	private Secretario secretario;

	@ManagedProperty(value = "#{secretarioDao}")
	private SecretarioDao secretarioDao;

	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	@ManagedProperty(value = "#{secretarioLazyDataModel}")
	private SecretarioLazyDataModel secretarioLazyDataModel;

	private List<Secretario> secretarioFiltered;

	@ManagedProperty(value = "#{homeMB}")
	private HomeMB homeMB;

	@ManagedProperty(value = "#{crachasPdf}")
	public CrachasPdf crachasPdf;

	@ManagedProperty(value = "#{cadastroValidator}")
	private CadastroUsuarioValidator cadastroValidator;

	public SecretarioMB() {
		secretarioFiltered = new ArrayList<Secretario>();
	}

	public void criar() {
		secretario = new Secretario();
	}

	public void remover() {
		secretarioDao.remover(secretario);
	}

	public void cancelar() {
		secretario = null;
	}

	public void salvar() {
		secretario.setTipo(TipoPessoa.ROLE_SECRETARIO);
		if (cadastroValidator.validarDadosSec(secretario)) {
			if (secretario.getId() != null) {
				secretarioDao.update(secretario);
			} else if (validarLoginExistente()) {
				gerarSenha();
				String md5 = criptografia.criptografar(secretario.getSenha());
				secretario.setSenha(md5);
				secretarioDao.salvar(secretario);
				homeMB.criarArqFotoPerfil(secretario);
				enviarEmail();
			}
		}
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		secretario.setSenha(myRandom.substring(0, 7));
	}

	private void enviarEmail() {
		cadastroValidator.enviarEmail(secretario);
	}

	public boolean validarLoginExistente() {
		if (secretario.getId() == null) {
			if (!cadastroValidator.validarEmail(secretario)) {
				return false;
			}
		}
		return true;
	}

	public void gerarCrachas() {
		crachasPdf.gerarPdfSecretario(secretarioDao.listDesc());
	}

	public Criptografia getCriptografia() {
		return criptografia;
	}

	public void setCriptografia(Criptografia criptografia) {
		this.criptografia = criptografia;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public Secretario getSecretario() {
		return secretario;
	}

	public void setSecretario(Secretario secretario) {
		this.secretario = secretario;
	}

	public SecretarioDao getSecretarioDao() {
		return secretarioDao;
	}

	public void setSecretarioDao(SecretarioDao secretarioDao) {
		this.secretarioDao = secretarioDao;
	}

	public SecretarioLazyDataModel getSecretarioLazyDataModel() {
		return secretarioLazyDataModel;
	}

	public void setSecretarioLazyDataModel(SecretarioLazyDataModel secretarioLazyDataModel) {
		this.secretarioLazyDataModel = secretarioLazyDataModel;
	}

	public List<Secretario> getSecretarioFiltered() {
		return secretarioFiltered;
	}

	public void setSecretarioFiltered(List<Secretario> secretarioFiltered) {
		this.secretarioFiltered = secretarioFiltered;
	}

	public HomeMB getHomeMB() {
		return homeMB;
	}

	public void setHomeMB(HomeMB homeMB) {
		this.homeMB = homeMB;
	}

	public CrachasPdf getCrachasPdf() {
		return crachasPdf;
	}

	public void setCrachasPdf(CrachasPdf crachasPdf) {
		this.crachasPdf = crachasPdf;
	}

	public CadastroUsuarioValidator getCadastroValidator() {
		return cadastroValidator;
	}

	public void setCadastroValidator(CadastroUsuarioValidator cadastroValidator) {
		this.cadastroValidator = cadastroValidator;
	}

}
