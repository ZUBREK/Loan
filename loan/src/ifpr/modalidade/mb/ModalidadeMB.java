package ifpr.modalidade.mb;

import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;
import ifpr.modalidade.model.ModalidadeLazyDataModel;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "modalidadeMB")
@ViewScoped
public class ModalidadeMB {

	private Modalidade modalidade;

	private List<Modalidade> modalidadeList;

	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;

	@ManagedProperty(value = "#{modalidadeLazyDataModel}")
	private ModalidadeLazyDataModel modalidadeLazyDataModel;

	public void criar() {
		modalidade = new Modalidade();

	}

	public void remover() {
		try {
			modalidadeDao.remover(modalidade);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!",
					"Impossível remover Modalidade - verifique se ela tem relação com outros registros!");
		}

	}

	public void mensagemAvisoFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, titulo, message));
	}

	public void cancelar() {
		modalidade = null;
	}

	public void salvar() {

		if (modalidade.getId() != null) {

			modalidadeDao.update(modalidade);
		} else {
			modalidadeDao.salvar(modalidade);
		}
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public List<Modalidade> getModalidadeList() {
		return modalidadeList;
	}

	public void setModalidadeList(List<Modalidade> modalidadeList) {
		this.modalidadeList = modalidadeList;
	}

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	public ModalidadeLazyDataModel getModalidadeLazyDataModel() {
		return modalidadeLazyDataModel;
	}

	public void setModalidadeLazyDataModel(ModalidadeLazyDataModel modalidadeLazyDataModel) {
		this.modalidadeLazyDataModel = modalidadeLazyDataModel;
	}
}
