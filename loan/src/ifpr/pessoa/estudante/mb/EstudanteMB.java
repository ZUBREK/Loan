package ifpr.pessoa.estudante.mb;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.criptografia.Criptografia;
import ifpr.model.LoginControllerMB;
import ifpr.perfilUsuario.HomeMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.pessoa.estudante.model.EstudanteLazyDataModel;
import ifpr.utils.Paths;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "estudanteMB")
@ViewScoped
public class EstudanteMB {

	private Estudante estudante;

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	@ManagedProperty(value = "#{estudanteLazyDataModel}")
	private EstudanteLazyDataModel estudanteLazyDataModel;

	private List<Estudante> estudanteFiltered;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	@ManagedProperty(value = "#{homeMB}")
	private HomeMB homeMB;

	@ManagedProperty(value = "#{cadastroValidator}")
	private CadastroUsuarioValidator emailHelper;

	private LoginControllerMB loginController;

	private List<Campus> listaCampus;

	private Campus campus;

	private Pessoa pessoaLogada;

	private Arquivo fotoPerfil;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	public EstudanteMB() {

		estudanteFiltered = new ArrayList<Estudante>();
	}

	public void criar() {
		estudante = new Estudante();
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(
				context, "#{loginControllerMB}", LoginControllerMB.class);
		pessoaLogada = loginController.getPessoaLogada();
	}

	public void remover() {
		estudanteDao.remover(estudante);
	}

	public void cancelar() {
		estudante = null;
	}

	public void salvar() {
		if (emailHelper.validarDadosEstudante(estudante)) {
			if (estudante.getId() != null) {
				estudanteDao.update(estudante);
			} else if (validarLoginExistente()) {
				estudante.setCampus(campus);
				gerarSenha();
				enviarEmail();
				estudante.setTipo(TipoPessoa.ROLE_ESTUDANTE);
				String md5 = criptografia.criptografar(estudante.getSenha());
				estudante.setSenha(md5);
				estudanteDao.salvar(estudante);

			}
		}

	}

	public void handleFileUpload(FileUploadEvent event) {

		try {

			String nomeArquivoStreamed = event.getFile().getFileName();
			byte[] arquivoByte = event.getFile().getContents();
			String caminho = Paths.PASTA_ARQUIVO_EVENTO
					+ "/"
					+ pessoaLogada.getId()
					+ nomeArquivoStreamed.substring(
							nomeArquivoStreamed.lastIndexOf('.'),
							nomeArquivoStreamed.length());
			criarArquivoDisco(arquivoByte, caminho);

			fotoPerfil.setUploader(estudante);
			fotoPerfil.setCaminho(caminho);
			fotoPerfil.setNome("foto_perfil" + estudante.getId());
			fotoPerfil.setDataUpload(new Date());
			fotoPerfil.setFotoPerfil(true);
			if (fotoPerfil.getId() != null) {
				arquivoDao.update(fotoPerfil);
			} else {
				arquivoDao.salvar(fotoPerfil);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return;
	}

	private void criarArquivoDisco(byte[] bytes, String arquivo)
			throws IOException {
		File file = new File(Paths.CAMINHO_FOTO_PERFIL);
		file.mkdirs();
		FileOutputStream fos;
		fos = new FileOutputStream(arquivo);
		fos.write(bytes);
		fos.close();
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		estudante.setSenha(myRandom.substring(0, 7));
	}

	private void enviarEmail() {
		emailHelper.setPessoa(estudante);
		emailHelper.enviarEmail();
	}

	public boolean validarLoginExistente() {
		if (!emailHelper.validarEmail(estudante)) {
			return false;
		}
		return true;
	}

	public boolean validarCpf() {
		emailHelper.setPessoa(estudante);
		if (!emailHelper.validarCpf2(estudante.getCpf())) {
			return false;
		}
		return true;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public EstudanteDao getEstudanteDao() {
		return estudanteDao;
	}

	public void setEstudanteDao(EstudanteDao estudanteDao) {
		this.estudanteDao = estudanteDao;
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

	public EstudanteLazyDataModel getEstudanteLazyDataModel() {
		return estudanteLazyDataModel;
	}

	public void setEstudanteLazyDataModel(
			EstudanteLazyDataModel estudanteLazyDataModel) {
		this.estudanteLazyDataModel = estudanteLazyDataModel;
	}

	public List<Estudante> getEstudanteFiltered() {
		return estudanteFiltered;
	}

	public void setEstudanteFiltered(List<Estudante> estudanteFiltered) {
		this.estudanteFiltered = estudanteFiltered;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}

	public List<Campus> getListaCampus() {
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public HomeMB getHomeMB() {
		return homeMB;
	}

	public void setHomeMB(HomeMB homeMB) {
		this.homeMB = homeMB;
	}

	public CadastroUsuarioValidator getEmailHelper() {
		return emailHelper;
	}

	public void setEmailHelper(CadastroUsuarioValidator emailHelper) {
		this.emailHelper = emailHelper;
	}

	public LoginControllerMB getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginControllerMB loginController) {
		this.loginController = loginController;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public Arquivo getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(Arquivo fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

}
