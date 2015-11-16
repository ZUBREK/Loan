package ifpr.pessoa.tecnicoAdministrativo.mb;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

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
	
	private CadastroUsuarioValidator emailHelper;

	@ManagedProperty(value = "#{homeMB}")
	private HomeMB homeMB;

	public TecnicoAdministrativoMB() {
		tecnicoAdmFiltered = new ArrayList<TecnicoAdministrativo>();
		emailHelper = new CadastroUsuarioValidator(); 
	}

	public void criar() {

		tecnicoAdm = new TecnicoAdministrativo();

	}

	public void remover() {
		tecnicoAdmDao.remover(tecnicoAdm);
	}

	public void cancelar() {
		tecnicoAdm = null;
	}

	public void salvar() {
		if (tecnicoAdm.getId() != null) {

			tecnicoAdmDao.update(tecnicoAdm);
		} else if (validarLoginExistente()) {
			tecnicoAdm.setCampus(campus);
			gerarSenha();
			tecnicoAdm.setTipo(TipoPessoa.ROLE_TEC_ADM);
			String md5 = criptografia.criptografar(tecnicoAdm.getSenha());
			tecnicoAdm.setSenha(md5);
			tecnicoAdmDao.salvar(tecnicoAdm);
			homeMB.criarArqFotoPerfil(tecnicoAdm);
			enviarEmail();
		}
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		tecnicoAdm.setSenha(myRandom.substring(0, 6));
	}

	private void enviarEmail() {
		emailHelper.setPessoa(tecnicoAdm);
		emailHelper.run();
	}

	public boolean validarLoginExistente() {
		if (!emailHelper.validarEmail(tecnicoAdm)) {
			return false;
		}
		try {
			pessoaDao.findByLogin(tecnicoAdm.getLogin());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"E-mail já existe, escolha outro");
			FacesContext.getCurrentInstance().addMessage("Atenção", message);
			FacesContext.getCurrentInstance().validationFailed();

			return false;
		} catch (NoResultException nre) {
			return true;
		}

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

	public CadastroUsuarioValidator getEmailHelper() {
		return emailHelper;
	}

	public void setEmailHelper(CadastroUsuarioValidator emailHelper) {
		this.emailHelper = emailHelper;
	}
	
	
	
}
