package ifpr.projeto.mb;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.coordenadorPea.dao.CoordenadorDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.estudante.dao.EstudanteDao;
import ifpr.projeto.Projeto;
import ifpr.projeto.dao.ProjetoDao;
import ifpr.projeto.model.ProjetoLazyDataModel;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;
import ifpr.projeto.projetoEstudante.dao.ProjetoEstudanteDao;
import ifpr.projeto.relatorio.RelatorioProjeto;
import ifpr.projeto.relatorio.dao.RelatorioProjetoDao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

	@ManagedProperty(value = "#{estudanteDao}")
	private EstudanteDao estudanteDao;

	@ManagedProperty(value = "#{projetoEstudanteDao}")
	private ProjetoEstudanteDao projetoEstudanteDao;

	private ProjetoEstudante projetoEstudante;

	private List<RelatorioProjeto> relatorios;

	private RelatorioProjeto relatorio;

	
	@ManagedProperty(value="#{relatorioProjetoDao}")
	private RelatorioProjetoDao relatorioProjetoDao;

	private StreamedContent arqStreamed;

	private boolean isUpdate;

	public ProjetoMB() {

		projetoFiltered = new ArrayList<Projeto>();
		isUpdate = true;
	}

	public void criar() {
		projeto = new Projeto();
		isUpdate = false;
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
		listaModalidade = modalidadeDao.listarAlfabetica();
	}

	public void remover() {
		projetoDao.remover(projeto);
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
		if (projeto.getId() != null) {
			projetoDao.update(projeto);
		} else {
			projeto.setCampus(campus);
			projeto.setModalidade(modalidade);
			projeto.setCoordenador(coordenador);
			projetoDao.salvar(projeto);
		}
	}

	public void adicionarEstudante() {
		ProjetoEstudante projetoEstd = new ProjetoEstudante();
		estudante.setBolsista(true);
		projetoEstd.setEstudante(estudante);
		projetoEstd.setProjeto(projeto);
		projetoEstudanteDao.salvar(projetoEstd);
		projeto.getProjetoEstudante().add(projetoEstd);
		estudante = new Estudante();
	}

	public void removerEstudante() {
		projetoEstudanteDao.remover(projetoEstudante);
		projeto.getProjetoEstudante().remove(projetoEstudante);
	}

	public List<Estudante> pesquisarEstudanteNome(String nome) {
		listaEstudante = estudanteDao
				.pesquisarEstudanteNomeCampus(nome, campus);
		return listaEstudante;
	}
	
	public void procurarRelatorios(){
		relatorios = relatorioProjetoDao.pesquisarPorProjeto(projeto);
	}

	public void onItemSelect(SelectEvent event) {
		Object item = event.getObject();
		estudante = (Estudante) item;
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(relatorio.getCaminho());
			arqStreamed = new DefaultStreamedContent(stream, null,
					relatorio.getNome());
		} catch (FileNotFoundException e) {
			System.out.println("Erro no download do arquivo");
		}

		return arqStreamed;
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

	public void setProjetoLazyDataModel(
			ProjetoLazyDataModel projetoLazyDataModel) {
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

	public CoordenadorPea getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(CoordenadorPea coordenador) {
		this.coordenador = coordenador;
	}

	public List<CoordenadorPea> getListaCoordenador() {
		listaCoordenador = coordenadorDao
				.listarPessoaByCampusEmAlfabetica(campus);
		return listaCoordenador;
	}

	public void setListaCoordenador(List<CoordenadorPea> listaCoordenador) {
		this.listaCoordenador = listaCoordenador;
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

	 
	
}
