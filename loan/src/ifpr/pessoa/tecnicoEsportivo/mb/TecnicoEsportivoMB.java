package ifpr.pessoa.tecnicoEsportivo.mb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.criptografia.Criptografia;
import ifpr.perfilUsuario.HomeMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;
import ifpr.pessoa.tecnicoEsportivo.dao.TecnicoEsportivoDao;
import ifpr.pessoa.tecnicoEsportivo.model.TecnicoEsportivoLazyDataModel;

@ManagedBean(name = "tecEspMB")
@ViewScoped
public class TecnicoEsportivoMB {

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;
	private Arquivo arquivo;

	private TecnicoEsportivo tecnicoEsp;

	@ManagedProperty(value = "#{tecnicoEsportivoDao}")
	private TecnicoEsportivoDao tecEspDao;

	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	@ManagedProperty(value = "#{tecnicoEspLazyDataModel}")
	private TecnicoEsportivoLazyDataModel tecEspLazyDataModel;

	private List<TecnicoEsportivo> tecnicoEsportivoFiltered;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	private List<Campus> listaCampus;

	private Campus campus;

	@ManagedProperty(value = "#{cadastroValidator}")
	private CadastroUsuarioValidator cadastroValidator;

	@ManagedProperty(value = "#{homeMB}")
	private HomeMB homeMB;

	public TecnicoEsportivoMB() {

		tecnicoEsportivoFiltered = new ArrayList<TecnicoEsportivo>();
	}

	public void criar() {
		tecnicoEsp = new TecnicoEsportivo();

	}

	public void remover() {
		try {
			removerFotoPerf(tecnicoEsp);
			tecEspDao.remover(tecnicoEsp);
			File file = new File(arquivo.getCaminho());
			file.delete();
		} catch (Exception e) {
			arquivoDao.salvar(arquivo);
			mensagemFaces("Erro!", "A pessoa é utilizada em outros registros, portanto não pode ser removida!");
		}
	}

	private void removerFotoPerf(Pessoa pessoa) throws Exception {
		arquivo = arquivoDao.pesquisarFotoPerfil(pessoa);
		arquivoDao.remover(arquivo);
	}

	public void mensagemFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, message));
	}

	public void cancelar() {
		tecnicoEsp = null;
	}

	public void salvar() {
		tecnicoEsp.setTipo(TipoPessoa.ROLE_TEC_ESP);
		cadastroValidator.setPessoa(tecnicoEsp);
		if (cadastroValidator.validarDados(tecnicoEsp.getSiape())) {
			if (tecnicoEsp.getId() != null) {
				tecEspDao.update(tecnicoEsp);
			} else if (validarLoginExistente()) {

				tecnicoEsp.setCampus(campus);
				gerarSenha();
				enviarEmail();
				String md5 = criptografia.criptografar(tecnicoEsp.getSenha());
				tecnicoEsp.setSenha(md5);
				tecEspDao.salvar(tecnicoEsp);
				homeMB.criarArqFotoPerfil(tecnicoEsp);

			}
		}
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		tecnicoEsp.setSenha(myRandom.substring(0, 7));
	}

	private void enviarEmail() {
		cadastroValidator.enviarEmail(tecnicoEsp);
	}

	public boolean validarLoginExistente() {
		if (tecnicoEsp.getId() == null) {
			if (!cadastroValidator.validarEmail(tecnicoEsp)) {
				return false;
			}
		}
		return true;
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

	public TecnicoEsportivoDao getTecEspDao() {
		return tecEspDao;
	}

	public void setTecEspDao(TecnicoEsportivoDao tecEspDao) {
		this.tecEspDao = tecEspDao;
	}

	public TecnicoEsportivoLazyDataModel getTecEspLazyDataModel() {
		return tecEspLazyDataModel;
	}

	public void setTecEspLazyDataModel(TecnicoEsportivoLazyDataModel tecEspLazyDataModel) {
		this.tecEspLazyDataModel = tecEspLazyDataModel;
	}

	public List<TecnicoEsportivo> getTecnicoEsportivoFiltered() {
		return tecnicoEsportivoFiltered;
	}

	public void setTecnicoEsportivoFiltered(List<TecnicoEsportivo> tecnicoEsportivoFiltered) {
		this.tecnicoEsportivoFiltered = tecnicoEsportivoFiltered;
	}

	public TecnicoEsportivo getTecnicoEsp() {
		return tecnicoEsp;
	}

	public void setTecnicoEsp(TecnicoEsportivo tecnicoEsp) {
		campus = tecnicoEsp.getCampus();
		this.tecnicoEsp = tecnicoEsp;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public List<Campus> getListaCampus() {
		listaCampus = campusDao.listarAlfabetica();
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public HomeMB getHomeMB() {
		return homeMB;
	}

	public void setHomeMB(HomeMB homeMB) {
		this.homeMB = homeMB;
	}

	public CadastroUsuarioValidator getCadastroValidator() {
		return cadastroValidator;
	}

	public void setCadastroValidator(CadastroUsuarioValidator cadastroValidator) {
		this.cadastroValidator = cadastroValidator;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

}
