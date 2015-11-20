package ifpr.pessoa.estudante.mb;

import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.criptografia.Criptografia;
import ifpr.perfilUsuario.HomeMB;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.pessoa.estudante.model.EstudanteLazyDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

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

	private List<Campus> listaCampus;

	private Campus campus;

	public EstudanteMB() {

		estudanteFiltered = new ArrayList<Estudante>();
	}

	public void criar() {
		estudante = new Estudante();
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
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
				estudante.setTipo(TipoPessoa.ROLE_ESTUDANTE);
				String md5 = criptografia.criptografar(estudante.getSenha());
				estudante.setSenha(md5);
				estudante.setBolsista(false);
				estudanteDao.salvar(estudante);
				homeMB.criarArqFotoPerfil(estudante);
				enviarEmail();
			}
		}

	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		estudante.setSenha(myRandom.substring(0, 6));
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
		if (!emailHelper.validarCpf(estudante.getCpf())) {
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
	
	

}
