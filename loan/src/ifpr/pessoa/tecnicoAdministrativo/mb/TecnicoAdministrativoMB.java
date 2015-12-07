package ifpr.pessoa.tecnicoAdministrativo.mb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.criptografia.Criptografia;
import ifpr.perfilUsuario.HomeMB;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;
import ifpr.pessoa.tecnicoAdministrativo.dao.TecnicoAdministrativoDao;
import ifpr.pessoa.tecnicoAdministrativo.model.TecnicoAdministrativoLazyDataModel;

@ManagedBean(name = "tecnicoAdmMB")
@ViewScoped
public class TecnicoAdministrativoMB {

	@ManagedProperty(value = "#{tecnicoAdmDao}")
	private TecnicoAdministrativoDao tecnicoAdmDao;

	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	private List<TecnicoAdministrativo> tecnicoAdmFiltered;

	private TecnicoAdministrativo tecnicoAdm;

	@ManagedProperty(value = "#{tecnicoAdmLazyDataModel}")
	private TecnicoAdministrativoLazyDataModel tecnicoAdmLazyDataModel;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	private List<Campus> listaCampus;

	private Campus campus;

	@ManagedProperty(value = "#{cadastroValidator}")
	private CadastroUsuarioValidator cadastroValidator;

	@ManagedProperty(value = "#{homeMB}")
	private HomeMB homeMB;

	public TecnicoAdministrativoMB() {
		tecnicoAdmFiltered = new ArrayList<TecnicoAdministrativo>();
	}

	public void criar() {

		tecnicoAdm = new TecnicoAdministrativo();

	}

	public void remover() {
		try {
			tecnicoAdmDao.remover(tecnicoAdm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cancelar() {
		tecnicoAdm = null;
	}

	public void salvar() {
		tecnicoAdm.setTipo(TipoPessoa.ROLE_TEC_ADM);
		cadastroValidator.setPessoa(tecnicoAdm);
		if (cadastroValidator.validarDados(tecnicoAdm.getSiape())) {
			if (tecnicoAdm.getId() != null) {
				tecnicoAdmDao.update(tecnicoAdm);
			} else if (validarLoginExistente()) {
				tecnicoAdm.setCampus(campus);
				gerarSenha();
				String md5 = criptografia.criptografar(tecnicoAdm.getSenha());
				tecnicoAdm.setSenha(md5);
				tecnicoAdmDao.salvar(tecnicoAdm);
				homeMB.criarArqFotoPerfil(tecnicoAdm);
				enviarEmail();
			}
		}
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		tecnicoAdm.setSenha(myRandom.substring(0, 7));
	}

	private void enviarEmail() {
		cadastroValidator.enviarEmail(tecnicoAdm);
	}

	public boolean validarLoginExistente() {
		if (tecnicoAdm.getId() == null) {
			if (!cadastroValidator.validarEmail(tecnicoAdm)) {
				return false;
			}
		}
		return true;

	}

	public TecnicoAdministrativoDao getTecnicoAdmDao() {
		return tecnicoAdmDao;
	}

	public void setTecnicoAdmDao(TecnicoAdministrativoDao tecnicoAdmDao) {
		this.tecnicoAdmDao = tecnicoAdmDao;
	}

	public Criptografia getCriptografia() {
		return criptografia;
	}

	public void setCriptografia(Criptografia criptografia) {
		this.criptografia = criptografia;
	}

	public List<TecnicoAdministrativo> getTecnicoAdmFiltered() {
		return tecnicoAdmFiltered;
	}

	public void setTecnicoAdmFiltered(List<TecnicoAdministrativo> tecnicoAdmFiltered) {
		this.tecnicoAdmFiltered = tecnicoAdmFiltered;
	}

	public TecnicoAdministrativo getTecnicoAdm() {
		return tecnicoAdm;
	}

	public void setTecnicoAdm(TecnicoAdministrativo tecnicoAdm) {
		campus = tecnicoAdm.getCampus();	
		this.tecnicoAdm = tecnicoAdm;
	}

	public TecnicoAdministrativoLazyDataModel getTecnicoAdmLazyDataModel() {
		return tecnicoAdmLazyDataModel;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}

	public List<Campus> getListaCampus() {
		listaCampus = campusDao.listarAlfabetica();
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public void setTecnicoAdmLazyDataModel(TecnicoAdministrativoLazyDataModel tecnicoAdmLazyDataModel) {
		this.tecnicoAdmLazyDataModel = tecnicoAdmLazyDataModel;
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

	public CadastroUsuarioValidator getCadastroValidator() {
		return cadastroValidator;
	}

	public void setCadastroValidator(CadastroUsuarioValidator cadastroValidator) {
		this.cadastroValidator = cadastroValidator;
	}

}
