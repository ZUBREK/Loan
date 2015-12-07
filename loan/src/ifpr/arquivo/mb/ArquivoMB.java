package ifpr.arquivo.mb;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.model.LoginControllerMB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "arquivoMB")
@ViewScoped
public class ArquivoMB {
	
	private final String CAMINHO_PASTA = "C:/home/loan_docs";

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private Arquivo arquivo;
	private StreamedContent arqStreamed;
	
	@ManagedProperty(value = "#{arquivoLazyDataModel}")
	private ArquivoLazyDataModel arquivoLazyDataModel;

	private List<Arquivo> arquivosFiltered;

	private LoginControllerMB loginController;
	
	public ArquivoMB() {
		arquivosFiltered = new ArrayList<Arquivo>();
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(context, "#{loginControllerMB}", LoginControllerMB.class);
	}

	public void cancelar() {
		arquivo = null;
	}

	public void handleFileUpload(FileUploadEvent event) {
		arquivo = new Arquivo();
		try {
			File file = new File(CAMINHO_PASTA);
			file.mkdirs();

			byte[] arquivoByte = event.getFile().getContents();
			String caminho = CAMINHO_PASTA
					+ event.getFile().getFileName();

			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arquivoByte);
			fos.close();
			arquivo.setCaminho(caminho);
			arquivo.setNome(event.getFile().getFileName());
			arquivo.setDataUpload(new Date());
			arquivo.setUploader(loginController.getPessoaLogada());
			salvar();
		} catch (Exception ex) {
			System.out.println("Erro no upload de arquivo" + ex);
		}

	}

	public void salvar() {
		if (arquivo.getId() == null) {
			arquivoDao.salvar(arquivo);
		}
	}

	public void remover() {
		File file = new File(arquivo.getCaminho());
		file.delete();
		try{
			arquivoDao.remover(arquivo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public ArquivoLazyDataModel getArquivoLazyDataModel() {
		return arquivoLazyDataModel;
	}

	public void setArquivoLazyDataModel(
			ArquivoLazyDataModel arquivoLazyDataModel) {
		this.arquivoLazyDataModel = arquivoLazyDataModel;
	}

	public List<Arquivo> getArquivosFiltered() {
		return arquivosFiltered;
	}

	public void setArquivosFiltered(List<Arquivo> arquivosFiltered) {
		this.arquivosFiltered = arquivosFiltered;
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(arquivo.getCaminho());
			arqStreamed = new DefaultStreamedContent(stream, null,
					arquivo.getNome());
		} catch (FileNotFoundException e) {
			System.out.println("Erro no download de imagem");
		}

		return arqStreamed;
	}

	public void setArqStreamed(StreamedContent arqStreamed) {
		this.arqStreamed = arqStreamed;
	}

	public LoginControllerMB getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginControllerMB loginController) {
		this.loginController = loginController;
	}
	

}
