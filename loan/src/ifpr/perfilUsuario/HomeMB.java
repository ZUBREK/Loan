package ifpr.perfilUsuario;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.geradorPdf.RelatorioFinal;
import ifpr.model.LoginControllerMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.secretario.Secretario;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;
import ifpr.utils.Paths;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "homeMB")
@SessionScoped
public class HomeMB {


	private StreamedContent imagem;
	private LoginControllerMB loginController;
	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private Arquivo arquivo;
	private Pessoa pessoaLogada;
	private boolean isAluno;
	private boolean isAdm;
	private List<String> atributos;
	public RelatorioFinal relatorioFinal;


	public HomeMB() {
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(
				context, "#{loginControllerMB}", LoginControllerMB.class);
		relatorioFinal = context.getApplication().evaluateExpressionGet(
				context, "#{relatorioFinal}", RelatorioFinal.class);
		pessoaLogada = loginController.getPessoaLogada();
		isAluno = false;
		isAdm = false;
	}

	@PostConstruct
	public void init() {
		try {
			arquivo = arquivoDao.pesquisarFotoPerfil(pessoaLogada);
		} catch (Exception e) {
			criarArqFotoPerfil(pessoaLogada);
		}
		if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE))
			isAluno = true;
		if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN))
			isAdm = true;

	}

	public void gerarRelatorio() {
		relatorioFinal.gerarRelatorio();
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			String nomeArquivoStreamed = event.getFile().getFileName();
			byte[] arquivoByte = event.getFile().getContents();
			String caminho = Paths.CAMINHO_FOTO_PERFIL
					+ "/"
					+ pessoaLogada.getId()
					+ nomeArquivoStreamed.substring(
							nomeArquivoStreamed.lastIndexOf('.'),
							nomeArquivoStreamed.length());
			criarArquivoDisco(arquivoByte, caminho);
			arquivo.setCaminho(caminho);
			salvarArquivo();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void criarArqFotoPerfil(Pessoa pessoa) {
		arquivo = new Arquivo();
		arquivo.setFotoPerfil(true);
		arquivo.setCaminho(Paths.FOTO_DEFAULT);
		arquivo.setNome("fotoPerfil" + pessoa.getId());
		arquivo.setDataUpload(new Date());
		arquivo.setUploader(pessoa);
		salvarArquivo();
	}

	private void salvarArquivo() {
		if (arquivo.getId() == null) {
			arquivoDao.salvar(arquivo);
		} else {
			arquivoDao.update(arquivo);
		}
	}

	private void criarArquivoDisco(byte[] bytes, String arquivo)
			throws IOException {
		File file = new File(Paths.CAMINHO_FOTO_PERFIL);
		file.mkdirs();
		FileOutputStream fos;
		fos = new FileOutputStream(arquivo);
		fos.write(bytes);
		fos.close();
	}

	public LoginControllerMB getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginControllerMB loginController) {
		this.loginController = loginController;
	}

	public void setImagem(StreamedContent imagem) {
		this.imagem = imagem;
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

	public StreamedContent getImagem() {
		arquivo = arquivoDao.pesquisarFotoPerfil(pessoaLogada);
		InputStream stream;
		File file = new File(arquivo.getCaminho());
		if (file.exists()) {
			stream = null;
			try {
				stream = new FileInputStream(file);
				imagem = new DefaultStreamedContent(stream);//
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imagem;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public List<String> getAtributos() {
		atributos = new ArrayList<String>();
		pegarAtributos();
		return atributos;
	}

	private void pegarAtributos() {
		atributos.add("Nome: " + pessoaLogada.getNome());
		if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ADM)) {
			TecnicoAdministrativo tecAdm = (TecnicoAdministrativo) pessoaLogada;
			atributos.add("Siape: " + tecAdm.getSiape());
			atributos.add("Campus: " + tecAdm.getCampus());
		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)) {
			TecnicoEsportivo tecEsp = (TecnicoEsportivo) pessoaLogada;
			atributos.add("Siape: " + tecEsp.getSiape());
			atributos.add("Campus: " + tecEsp.getCampus());
		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_COORDENADOR)) {
			CoordenadorPea coordPea = (CoordenadorPea) pessoaLogada;
			atributos.add("Siape: " + coordPea.getSiape());
			atributos.add("Campus: " + coordPea.getCampus());
		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_SECRETARIO)) {
			Secretario secretario = (Secretario) pessoaLogada;
			atributos.add("RG: " + secretario.getRg());
			atributos.add("CPF: " + secretario.getCpf());
		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {
			Estudante estudante = (Estudante) pessoaLogada;
			atributos.add("Matricula: " + estudante.getMatricula());
			atributos.add("RG: " + estudante.getRg());
			atributos.add("CPF: " + estudante.getCpf());
			atributos.add("Data de Nascimento: "
					+ new SimpleDateFormat("dd/MM/yyyy").format(estudante
							.getDataNascimento()));
			atributos.add("Câmpus: " + estudante.getCampus());

		}

	}

	public void setAtributos(List<String> atributos) {
		this.atributos = atributos;
	}

	public boolean isAluno() {
		return isAluno;
	}

	public void setAluno(boolean isAluno) {
		this.isAluno = isAluno;
	}

	public RelatorioFinal getRelatorioFinal() {
		return relatorioFinal;
	}

	public void setRelatorioFinal(RelatorioFinal relatorioFinal) {
		this.relatorioFinal = relatorioFinal;
	}

	public boolean isAdm() {
		return isAdm;
	}

	public void setAdm(boolean isAdm) {
		this.isAdm = isAdm;
	}
	
}
