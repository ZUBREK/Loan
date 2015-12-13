package ifpr.arquivo.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.TipoArquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.model.LoginControllerMB;
import ifpr.utils.Paths;

@ManagedBean(name = "arquivoMB")
@ViewScoped
public class ArquivoMB {

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private Arquivo arquivo;
	private StreamedContent arqStreamed;

	@ManagedProperty(value = "#{arquivoLazyDataModel}")
	private ArquivoLazyDataModel arquivoLazyDataModel;

	private List<Arquivo> arquivosFiltered;

	private LoginControllerMB loginController;

	TipoArquivo[] listaTipo;

	private TipoArquivo tipo;

	public ArquivoMB() {
		arquivosFiltered = new ArrayList<Arquivo>();
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(context, "#{loginControllerMB}",
				LoginControllerMB.class);
	}

	public void cancelar() {
		arquivo = null;
		tipo = null;
	}

	public void criar() {
		arquivo = new Arquivo();
		listaTipo = null;
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			File file = new File(Paths.LOAN_DOCS);
			file.mkdirs();

			byte[] arquivoByte = event.getFile().getContents();
			String caminho = Paths.LOAN_DOCS + "/" + verificaCaminho();

			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arquivoByte);
			fos.close();
			arquivo.setCaminho(caminho);
			arquivo.setNome(verificaCaminho());
			arquivo.setDataUpload(new Date());
			arquivo.setUploader(loginController.getPessoaLogada());
		} catch (Exception ex) {
			System.out.println("Erro no upload de arquivo" + ex);
		}

	}

	private String verificaCaminho() {
		String caminho = null;
		if (tipo.equals(TipoArquivo.ASSINATURA_DECLARACAO)) {
			caminho = "assinaturaDeclaracao.png";
		} else if (tipo.equals(TipoArquivo.LOGO_IFPR)) {
			caminho = "logoIFPR.png";
		} else if (tipo.equals(TipoArquivo.LOGO_JIFPR)) {
			caminho = "logoJIFPR.png";
		} else if (tipo.equals(TipoArquivo.MOLDURA_CRACHA)) {
			caminho = "molduraCracha.png";
		} else if (tipo.equals(TipoArquivo.MOLDURA_DECLARACAO)) {
			caminho = "molduraDeclaracao.png";
		}
		return caminho;
	}

	public void salvar() {
		if (arquivo.getId() == null) {
			arquivoDao.salvar(arquivo);
		}
		arquivo = null;
		tipo = null;
	}

	public void remover() {
		try {
			arquivoDao.remover(arquivo);
			File file = new File(arquivo.getCaminho());
			file.delete();
		} catch (Exception e) {
			mensagemFaces("ERRO!", "Arquivo não pôde ser removido!");
		}
	}

	public void mensagemFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, message));
	}

	private void verificaHasArquivo() {
		listaTipo = TipoArquivo.values();
		List<Arquivo> listaArq = arquivoDao.listDesc();
		for (int i = 0; i < listaArq.size(); i++) {
			if (listaArq.get(i).getNome().equals("logoIFPR.png")) {
				ArrayList<TipoArquivo> tiposOld = new ArrayList<TipoArquivo>(Arrays.asList(listaTipo));
				tiposOld.remove(TipoArquivo.LOGO_IFPR);
				TipoArquivo[] tiposNew = tiposOld.toArray(new TipoArquivo[tiposOld.size()]);
				listaTipo = tiposNew;
			} else if (listaArq.get(i).getNome().equals("logoJIFPR.png")) {
				ArrayList<TipoArquivo> tiposOld = new ArrayList<TipoArquivo>(Arrays.asList(listaTipo));
				tiposOld.remove(TipoArquivo.LOGO_JIFPR);
				TipoArquivo[] tiposNew = tiposOld.toArray(new TipoArquivo[tiposOld.size()]);
				listaTipo = tiposNew;
			} else if (listaArq.get(i).getNome().equals("assinaturaDeclaracao.png")) {
				ArrayList<TipoArquivo> tiposOld = new ArrayList<TipoArquivo>(Arrays.asList(listaTipo));
				tiposOld.remove(TipoArquivo.ASSINATURA_DECLARACAO);
				TipoArquivo[] tiposNew = tiposOld.toArray(new TipoArquivo[tiposOld.size()]);
				listaTipo = tiposNew;
			} else if (listaArq.get(i).getNome().equals("molduraCracha.png")) {
				ArrayList<TipoArquivo> tiposOld = new ArrayList<TipoArquivo>(Arrays.asList(listaTipo));
				tiposOld.remove(TipoArquivo.MOLDURA_CRACHA);
				TipoArquivo[] tiposNew = tiposOld.toArray(new TipoArquivo[tiposOld.size()]);
				listaTipo = tiposNew;
			} else if (listaArq.get(i).getNome().equals("molduraDeclaracao.png")) {
				ArrayList<TipoArquivo> tiposOld = new ArrayList<TipoArquivo>(Arrays.asList(listaTipo));
				tiposOld.remove(TipoArquivo.MOLDURA_DECLARACAO);
				TipoArquivo[] tiposNew = tiposOld.toArray(new TipoArquivo[tiposOld.size()]);
				listaTipo = tiposNew;
			}
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

	public void setArquivoLazyDataModel(ArquivoLazyDataModel arquivoLazyDataModel) {
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
			arqStreamed = new DefaultStreamedContent(stream, null, arquivo.getNome());
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

	public TipoArquivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoArquivo tipo) {
		this.tipo = tipo;
	}

	public TipoArquivo[] getListaTipo() {
		verificaHasArquivo();
		return listaTipo;
	}

	public void setListaTipo(TipoArquivo[] listaTipo) {
		this.listaTipo = listaTipo;
	}

}
