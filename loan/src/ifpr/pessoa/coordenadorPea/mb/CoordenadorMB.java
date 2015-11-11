package ifpr.pessoa.coordenadorPea.mb;

import ifpr.cadastroUsuarios.CadastroUsuarioValidator;
import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.criptografia.Criptografia;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;
import ifpr.pessoa.coordenadorPea.model.CoordenadorLazyDataModel;
import ifpr.pessoa.dao.PessoaDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

@ManagedBean(name = "coordenadorMB")
@ViewScoped
public class CoordenadorMB {

	private CoordenadorPea coordenador;

	@ManagedProperty(value = "#{coordenadorDao}")
	private CoordenadorDao coordenadorDao;

	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	@ManagedProperty(value = "#{coordLazyDataModel}")
	private CoordenadorLazyDataModel coordenadorLazyDataModel;

	private List<CoordenadorPea> coordenadorFiltered;
	
	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;
	
	private List<Campus> listaCampus;
	
	private Campus campus;

	public CoordenadorMB() {

		coordenadorFiltered = new ArrayList<CoordenadorPea>();
	}

	public void criar() {
		coordenador = new CoordenadorPea();

	}

	public void remover() {
		coordenadorDao.remover(coordenador);
	}

	public void cancelar() {
		coordenador = null;
	}

	public void salvar() {
		if (coordenador.getId() != null) {

			coordenadorDao.update(coordenador);
		} else if (validarLoginExistente()) {
			coordenador.setCampus(campus);
			gerarSenha();
			coordenador.setTipo(TipoPessoa.ROLE_COORDENADOR);
			enviarEmail();
			String md5 = criptografia.criptografar(coordenador.getSenha());
			coordenador.setSenha(md5);
			coordenadorDao.salvar(coordenador);
		}
	}

	private void gerarSenha() {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		coordenador.setSenha(myRandom.substring(0, 6));
	}

	private void enviarEmail() {
		CadastroUsuarioValidator.enviarEmail(coordenador);
	}

	public boolean validarLoginExistente() {
		if (!CadastroUsuarioValidator.validarEmail(coordenador)) {
			return false;
		}
		try {
			pessoaDao.findByLogin(coordenador.getLogin());
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"E-mail j� existe, escolha outro");
			FacesContext.getCurrentInstance().addMessage("Aten��o", message);
			FacesContext.getCurrentInstance().validationFailed();

			return false;
		} catch (NoResultException nre) {
			return true;
		}

	}

	public CoordenadorPea getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(CoordenadorPea coordenador) {
		this.coordenador = coordenador;
	}

	public CoordenadorDao getCoordenadorDao() {
		return coordenadorDao;
	}

	public void setCoordenadorDao(CoordenadorDao coordenadorDao) {
		this.coordenadorDao = coordenadorDao;
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

	public CoordenadorLazyDataModel getCoordenadorLazyDataModel() {
		return coordenadorLazyDataModel;
	}

	public void setCoordenadorLazyDataModel(
			CoordenadorLazyDataModel coordenadorLazyDataModel) {
		this.coordenadorLazyDataModel = coordenadorLazyDataModel;
	}

	public List<CoordenadorPea> getCoordenadorFiltered() {
		return coordenadorFiltered;
	}

	public void setCoordenadorFiltered(List<CoordenadorPea> coordenadorFiltered) {
		this.coordenadorFiltered = coordenadorFiltered;
	}
	

	public List<Campus> getListaCampus() {
		listaCampus = campusDao.listarAlfabetica();
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
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
	
	
}
