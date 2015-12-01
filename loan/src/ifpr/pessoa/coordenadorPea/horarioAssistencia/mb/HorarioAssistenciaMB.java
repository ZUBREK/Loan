package ifpr.pessoa.coordenadorPea.horarioAssistencia.mb;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.pessoa.coordenadorPea.horarioAssistencia.HorarioAssistencia;
import ifpr.pessoa.coordenadorPea.horarioAssistencia.dao.HorarioAssistenciaDao;
import ifpr.pessoa.coordenadorPea.horarioAssistencia.model.HorarioAssistenciaLazyDataModel;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.utils.Paths;

@ManagedBean(name = "horarioAssistenciaMB")
@ViewScoped
public class HorarioAssistenciaMB {

	private HorarioAssistencia horarioAssistencia;

	@ManagedProperty(value = "#{horarioAssistenciaDao}")
	private HorarioAssistenciaDao horarioAssistenciaDao;

	@ManagedProperty(value = "#{horarioAssistenciaLazyDataModel}")
	private HorarioAssistenciaLazyDataModel horarioAssistenciaLazyDataModel;

	private List<HorarioAssistencia> horarioAssistenciaFiltered;

	private List<Estudante> listaEstudante;

	private Estudante estudante;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	private List<Campus> listaCampus;

	private Campus campus;

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private Arquivo arquivo;

	public HorarioAssistenciaMB() {
		horarioAssistenciaFiltered = new ArrayList<HorarioAssistencia>();
	}

	public void criar() {
		horarioAssistencia = new HorarioAssistencia();
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
	}

	public void remover() {
		horarioAssistenciaDao.remover(horarioAssistencia);
	}

	public void cancelar() {
		horarioAssistencia = null;
	}

	public void salvar() {
		if (horarioAssistencia.getId() != null) {
			horarioAssistenciaDao.update(horarioAssistencia);
		} else {
			horarioAssistencia.setEstudante(estudante);
			horarioAssistenciaDao.salvar(horarioAssistencia);
		}
		horarioAssistencia = null;
	}

	public void handleFileUpload(FileUploadEvent event) {
		arquivo = horarioAssistencia.getFotoAssinatura();
		if (arquivo == null) {
			arquivo = new Arquivo();
		}
		try {
			File file = new File(Paths.PASTA_ASSISTENCIA);
			file.mkdirs();

			byte[] arquivoByte = event.getFile().getContents();
			String caminho = Paths.PASTA_ASSISTENCIA +"/" + estudante.getNome() + estudante.getId() + event.getFile().getFileName();
			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arquivoByte);
			fos.close();
			arquivo.setCaminho(caminho);
			arquivo.setDataUpload(new Date());
			arquivo.setFotoPerfil(false);
			arquivo.setNome(event.getFile().getFileName());
			arquivo.setUploader(estudante);
			horarioAssistencia.setFotoAssinatura(arquivo);
			salvarArquivo();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void salvarArquivo() {
		if (arquivo.getId() == null) {
			arquivoDao.salvar(arquivo);
			horarioAssistencia.setFotoAssinatura(arquivo);
		} else {
			arquivoDao.update(arquivo);
			horarioAssistencia.setFotoAssinatura(arquivo);
		}
		horarioAssistenciaDao.update(horarioAssistencia);
	}

	public HorarioAssistencia getHorarioAssistencia() {
		return horarioAssistencia;
	}

	public void setHorarioAssistencia(HorarioAssistencia horarioAssistencia) {
		estudante = horarioAssistencia.getEstudante();
		campus = estudante.getCampus();
		this.horarioAssistencia = horarioAssistencia;
	}

	public HorarioAssistenciaDao getHorarioAssistenciaDao() {
		return horarioAssistenciaDao;
	}

	public void setHorarioAssistenciaDao(
			HorarioAssistenciaDao horarioAssistenciaDao) {
		this.horarioAssistenciaDao = horarioAssistenciaDao;
	}

	public HorarioAssistenciaLazyDataModel getHorarioAssistenciaLazyDataModel() {
		return horarioAssistenciaLazyDataModel;
	}

	public void setHorarioAssistenciaLazyDataModel(
			HorarioAssistenciaLazyDataModel horarioAssistenciaLazyDataModel) {
		this.horarioAssistenciaLazyDataModel = horarioAssistenciaLazyDataModel;
	}

	public List<HorarioAssistencia> getHorarioAssistenciaFiltered() {
		return horarioAssistenciaFiltered;
	}

	public void setHorarioAssistenciaFiltered(
			List<HorarioAssistencia> horarioAssistenciaFiltered) {
		this.horarioAssistenciaFiltered = horarioAssistenciaFiltered;
	}

	public void setListaEstudante(List<Estudante> listaEstudante) {
		this.listaEstudante = listaEstudante;
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

	public List<Estudante> getListaEstudante() {
		listaEstudante = estudanteDao.listarPessoaByCampusEmAlfabetica(campus);
		return listaEstudante;
	}
}
