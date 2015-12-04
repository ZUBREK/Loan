package ifpr.noticia.mb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.noticia.Noticia;
import ifpr.noticia.dao.NoticiaDao;
import ifpr.noticia.model.NoticiaLazyDataModel;
import ifpr.perfilUsuario.HomeMB;
import ifpr.pessoa.Pessoa;
import ifpr.utils.Paths;

@ManagedBean(name = "noticiaMB")
@ViewScoped
public class NoticiaMB {

	private Noticia noticia;

	@ManagedProperty(value = "#{noticiaDao}")
	private NoticiaDao noticiaDao;

	@ManagedProperty(value = "#{noticiaLazyDataModel}")
	private NoticiaLazyDataModel noticiaLazyDataModel;

	private List<Noticia> noticiaFiltered;

	@ManagedProperty(value = "#{homeMB}")
	private HomeMB homeMB;

	private Arquivo fotoNoticia;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private Pessoa pessoaLogada;

	public NoticiaMB() {

		noticiaFiltered = new ArrayList<Noticia>();
	}

	@PostConstruct
	public void poust() {
		pessoaLogada = homeMB.getPessoaLogada();
	}

	public void criar() {
		fotoNoticia = null;
		noticia = new Noticia();

	}

	public void remover() {
		noticiaDao.remover(noticia);
	}

	public void cancelar() {
		noticia = null;
	}

	public void salvar() {
		if (fotoNoticia == null) {
			mensagemErroFaces("ERRO!", "Selecione uma foto para a not�cia!");
		} else {
			if (noticia.getId() != null) {
				noticia.setData(new Date());
				noticiaDao.update(noticia);
			} else {
				noticia.setData(new Date());
				noticiaDao.salvar(noticia);
			}
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		if (fotoNoticia == null) {
			fotoNoticia = new Arquivo();
		} else if (noticia.getData() == null) {
			noticia.setData(new Date());
		}
		try {
			fotoNoticia.setDataUpload(new Date());
			String nomeArquivoStreamed = event.getFile().getFileName();
			byte[] arquivoByte = event.getFile().getContents();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
			String caminho = Paths.PASTA_IMAGEM_NOTICIA + "/" + formater.format(noticia.getData())
					+ nomeArquivoStreamed.substring(nomeArquivoStreamed.lastIndexOf('.'), nomeArquivoStreamed.length());
			criarArquivoDisco(arquivoByte, caminho);
			fotoNoticia.setUploader(pessoaLogada);
			fotoNoticia.setCaminho(caminho);
			fotoNoticia.setNome(nomeArquivoStreamed);
			if (fotoNoticia.getId() != null) {
				arquivoDao.update(fotoNoticia);
			} else {
				arquivoDao.salvar(fotoNoticia);
				noticia.setFoto(fotoNoticia);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return;
	}

	private void criarArquivoDisco(byte[] bytes, String arquivoPath) throws IOException {
		File file = new File(Paths.PASTA_IMAGEM_NOTICIA);
		file.mkdirs();
		FileOutputStream fos;
		fos = new FileOutputStream(arquivoPath);
		fos.write(bytes);
		fos.close();
	}

	public void mensagemErroFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, message));
		FacesContext context = FacesContext.getCurrentInstance();
		context.validationFailed();
	}

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		fotoNoticia = noticia.getFoto();
		this.noticia = noticia;
	}

	public NoticiaDao getNoticiaDao() {
		return noticiaDao;
	}

	public void setNoticiaDao(NoticiaDao noticiaDao) {
		this.noticiaDao = noticiaDao;
	}

	public NoticiaLazyDataModel getNoticiaLazyDataModel() {
		return noticiaLazyDataModel;
	}

	public void setNoticiaLazyDataModel(NoticiaLazyDataModel noticiaLazyDataModel) {
		this.noticiaLazyDataModel = noticiaLazyDataModel;
	}

	public List<Noticia> getNoticiaFiltered() {
		return noticiaFiltered;
	}

	public void setNoticiaFiltered(List<Noticia> noticiaFiltered) {
		this.noticiaFiltered = noticiaFiltered;
	}

	public HomeMB getHomeMB() {
		return homeMB;
	}

	public void setHomeMB(HomeMB homeMB) {
		this.homeMB = homeMB;
	}

	public Arquivo getFotoNoticia() {
		return fotoNoticia;
	}

	public void setFotoNoticia(Arquivo fotoNoticia) {
		this.fotoNoticia = fotoNoticia;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

}
