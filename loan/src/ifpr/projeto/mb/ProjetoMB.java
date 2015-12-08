package ifpr.projeto.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;
import ifpr.model.LoginControllerMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.projeto.Projeto;
import ifpr.projeto.dao.ProjetoDao;
import ifpr.projeto.model.ProjetoLazyDataModel;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;
import ifpr.projeto.projetoEstudante.dao.ProjetoEstudanteDao;
import ifpr.projeto.relatorio.RelatorioProjeto;
import ifpr.projeto.relatorio.dao.RelatorioProjetoDao;
import ifpr.utils.Paths;

@ManagedBean(name = "projetoMB")
@ViewScoped
public class ProjetoMB {

	private Projeto projeto;

	@ManagedProperty(value = "#{projetoDao}")
	private ProjetoDao projetoDao;

	@ManagedProperty(value = "#{projetoLazyDataModel}")
	private ProjetoLazyDataModel projetoLazyDataModel;

	private List<Projeto> projetoFiltered;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	private List<Campus> listaCampus;

	private Campus campus;

	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;

	private List<Modalidade> listaModalidade;

	private Modalidade modalidade;

	@ManagedProperty(value = "#{coordenadorDao}")
	private CoordenadorDao coordenadorDao;

	private List<CoordenadorPea> listaCoordenador;

	private CoordenadorPea coordenador;

	private List<Estudante> listaEstudante;

	private Estudante estudante;

	private LoginControllerMB loginController;

	private Pessoa pessoaLogada;

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	@ManagedProperty(value = "#{projetoEstudanteDao}")
	private ProjetoEstudanteDao projetoEstudanteDao;

	private ProjetoEstudante projetoEstudante;

	private List<RelatorioProjeto> relatorios;

	private RelatorioProjeto relatorio;

	@ManagedProperty(value = "#{relatorioProjetoDao}")
	private RelatorioProjetoDao relatorioProjetoDao;

	private StreamedContent arqStreamed;

	private boolean isUpdate;

	public ProjetoMB() {

		projetoFiltered = new ArrayList<Projeto>();
		isUpdate = true;
	}

	public void criar() {
		projeto = new Projeto();
		campus = null;
		modalidade = null;
		coordenador = null;
		isUpdate = false;
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
		listaModalidade = modalidadeDao.listarAlfabetica();
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(context, "#{loginControllerMB}",
				LoginControllerMB.class);
		pessoaLogada = loginController.getPessoaLogada();
	}

	public void remover() {
		try {
			for (RelatorioProjeto relProj : projeto.getRelatoriosProjeto()) {
				apagarArquivo(relProj);
			}
			projetoDao.findById(projeto.getId());
			projetoDao.remover(projeto);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Não foi possível apagar o projeto!");
			e.printStackTrace();
		}
	}

	public void cancelar() {
		if (isUpdate == true) {
			projeto = null;
		} else {
			remover();
			isUpdate = true;
		}
	}

	public void salvar() {
		projeto.setCampus(campus);
		projeto.setModalidade(modalidade);
		Pessoa pessoa = (Pessoa) coordenador;
		projeto.setCoordenador(pessoa);
		if (projeto.getId() != null) {
			projetoDao.update(projeto);
		} else {
			projetoDao.salvar(projeto);
		}
	}

	public void adicionarEstudante() {
		if (estudante != null && estudante.getId() != null) {
			ProjetoEstudante projetoEstd = new ProjetoEstudante();
			projetoEstd.setEstudante(estudante);
			projetoEstd.setProjeto(projeto);
			projetoEstudanteDao.salvar(projetoEstd);
			projeto.getProjetoEstudante().add(projetoEstd);
			estudante = new Estudante();
		}
	}

	public void removerEstudante() {
		try {
			projetoEstudanteDao.remover(projetoEstudante);
			projeto.getProjetoEstudante().remove(projetoEstudante);
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Não foi possível remover o estudante!");
			e.printStackTrace();
		}

	}

	public List<Estudante> pesquisarEstudanteNome(String nome) {
		listaEstudante = estudanteDao.pesquisarEstudanteNomeCampusProjeto(nome, campus, projeto);
		return listaEstudante;
	}

	public void procurarRelatorios() {
		projeto = projetoDao.findById(projeto.getId());
		relatorios = projeto.getRelatoriosProjeto();
	}

	public void onItemSelect(SelectEvent event) {
		Object item = event.getObject();
		estudante = (Estudante) item;
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(relatorio.getCaminho());
			arqStreamed = new DefaultStreamedContent(stream, null, relatorio.getNome());
		} catch (FileNotFoundException e) {
			mensagemAvisoFaces("Erro!", "Não foi possível achar o arquivo!");
		}

		return arqStreamed;
	}

	public void handleFileUpload(FileUploadEvent event) {
		relatorio = new RelatorioProjeto();
		try {
			String nomeArquivoStreamed = event.getFile().getFileName();
			byte[] arquivoByte = event.getFile().getContents();
			String caminho = Paths.PASTA_ARQUIVO_RELATORIO + "/" + pessoaLogada.getId()
					+ nomeArquivoStreamed.substring(nomeArquivoStreamed.lastIndexOf('.'), nomeArquivoStreamed.length());
			criarArquivoDisco(arquivoByte, caminho);
			relatorio.setUploader(pessoaLogada);
			relatorio.setCaminho(caminho);
			relatorio.setNome(nomeArquivoStreamed);
			relatorio.setDataUpload(new Date());
			projeto.getRelatoriosProjeto().add(relatorio);

			relatorioProjetoDao.salvar(relatorio);
			procurarRelatorios();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return;
	}

	public void apagarRelatorio() {
		apagarArquivo(relatorio);
	}

	public void apagarArquivo(RelatorioProjeto relProj) {
		try {
			if (relProj != null) {
				File file = new File(relProj.getCaminho());
				file.delete();
				relatorioProjetoDao.remover(relProj);
				relProj = null;
				procurarRelatorios();
			}
		} catch (Exception e) {
			mensagemAvisoFaces("Erro!", "Não foi possível apagar o arquivo!");
		}

		return;
	}

	private void criarArquivoDisco(byte[] bytes, String arquivoPath) throws IOException {
		File file = new File(Paths.PASTA_ARQUIVO_RELATORIO);
		file.mkdirs();
		FileOutputStream fos;
		fos = new FileOutputStream(arquivoPath);
		fos.write(bytes);
		fos.close();
	}

	public void mensagemAvisoFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, message));
	}

	public ProjetoEstudanteDao getProjetoEstudanteDao() {
		return projetoEstudanteDao;
	}

	public void setProjetoEstudanteDao(ProjetoEstudanteDao projetoEstudanteDao) {
		this.projetoEstudanteDao = projetoEstudanteDao;
	}

	public ProjetoEstudante getProjetoEstudante() {
		return projetoEstudante;
	}

	public void setProjetoEstudante(ProjetoEstudante projetoEstudante) {
		this.projetoEstudante = projetoEstudante;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		campus = projeto.getCampus();
		modalidade = projeto.getModalidade();
		coordenador = (CoordenadorPea) projeto.getCoordenador();
		this.projeto = projeto;
	}

	public ProjetoDao getProjetoDao() {
		return projetoDao;
	}

	public void setProjetoDao(ProjetoDao projetoDao) {
		this.projetoDao = projetoDao;
	}

	public ProjetoLazyDataModel getProjetoLazyDataModel() {
		return projetoLazyDataModel;
	}

	public void setProjetoLazyDataModel(ProjetoLazyDataModel projetoLazyDataModel) {
		this.projetoLazyDataModel = projetoLazyDataModel;
	}

	public List<Projeto> getProjetoFiltered() {
		return projetoFiltered;
	}

	public void setProjetoFiltered(List<Projeto> projetoFiltered) {
		this.projetoFiltered = projetoFiltered;
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
		listaCoordenador = coordenadorDao.listarPessoaByCampusEmAlfabetica(campus);
		this.campus = campus;
	}

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	public List<Modalidade> getListaModalidade() {
		return listaModalidade;
	}

	public void setCoordenador(CoordenadorPea coordenador) {
		this.coordenador = coordenador;
	}

	public List<CoordenadorPea> getListaCoordenador() {
		listaCoordenador = coordenadorDao.listarPessoaByCampusEmAlfabetica(campus);
		return listaCoordenador;
	}

	public CoordenadorDao getCoordenadorDao() {
		return coordenadorDao;
	}

	public void setCoordenadorDao(CoordenadorDao coordenadorDao) {
		this.coordenadorDao = coordenadorDao;
	}

	public void setListaModalidade(List<Modalidade> listaModalidade) {
		this.listaModalidade = listaModalidade;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public List<Estudante> getListaEstudante() {
		return listaEstudante;
	}

	public void setListaEstudante(List<Estudante> listaEstudante) {
		this.listaEstudante = listaEstudante;
	}

	public void setListaCoordenador(List<CoordenadorPea> listaCoordenador) {
		this.listaCoordenador = listaCoordenador;
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

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public RelatorioProjetoDao getRelatorioProjetoDao() {
		return relatorioProjetoDao;
	}

	public void setRelatorioProjetoDao(RelatorioProjetoDao relatorioProjetoDao) {
		this.relatorioProjetoDao = relatorioProjetoDao;
	}

	public List<RelatorioProjeto> getRelatorios() {
		return relatorios;
	}

	public void setRelatorios(List<RelatorioProjeto> relatorios) {
		this.relatorios = relatorios;
	}

	public RelatorioProjeto getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(RelatorioProjeto relatorio) {
		this.relatorio = relatorio;
	}

	public LoginControllerMB getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginControllerMB loginController) {
		this.loginController = loginController;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public Pessoa getCoordenador() {
		return coordenador;
	}

	public void setArqStreamed(StreamedContent arqStreamed) {
		this.arqStreamed = arqStreamed;
	}
}
