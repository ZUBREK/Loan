package ifpr.campus.mb;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.campus.model.CampusLazyDataModel;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name = "campusMB")
@ViewScoped
public class CampusMB {

	
	private Campus campus;
	
	
	private List<Campus> campusList;
	
	@ManagedProperty(value="#{campusDao}")
	private CampusDao campusDao;
	
	@ManagedProperty(value="#{campusLazyDataModel}")
	private CampusLazyDataModel campusLazyDataModel;
	
	
	
	public void criar() {
		campus = new Campus();

	}

	public void remover() {
		try{
			campusDao.remover(campus);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Impossível remover Campus - verifique se ele tem relação com outros registros!");
		}
	}

	public void mensagemAvisoFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, message));
	}
	
	public void cancelar() {
		campus = null;
	}
	
	public void salvar(){
		if (campus.getId() != null) {

			campusDao.update(campus);
		} else {
			campusDao.salvar(campus);
		}
	}
	
	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public List<Campus> getCampusList() {
		return campusList;
	}

	public void setCampusList(List<Campus> campusList) {
		this.campusList = campusList;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}

	public CampusLazyDataModel getCampusLazyDataModel() {
		return campusLazyDataModel;
	}

	public void setCampusLazyDataModel(CampusLazyDataModel campusLazyDataModel) {
		this.campusLazyDataModel = campusLazyDataModel;
	}
	
	
}
