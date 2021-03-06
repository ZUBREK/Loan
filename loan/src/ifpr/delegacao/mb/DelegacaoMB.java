package ifpr.delegacao.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.delegacao.Delegacao;
import ifpr.delegacao.dao.DelegacaoDao;
import ifpr.delegacao.delegacaoPessoa.DelegacaoPessoa;
import ifpr.delegacao.delegacaoPessoa.dao.DelegacaoPessoaDao;
import ifpr.delegacao.model.DelegacaoLazyDataModel;
import ifpr.geradorPdf.CrachasPdf;
import ifpr.geradorPdf.DeclaracoesPdf;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.dao.PessoaDao;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

@ManagedBean(name = "delegacaoMB")
@ViewScoped
public class DelegacaoMB {

	private Delegacao delegacao;

	@ManagedProperty(value = "#{delegacaoDao}")
	private DelegacaoDao delegacaoDao;

	@ManagedProperty(value = "#{delegacaoLazyDataModel}")
	private DelegacaoLazyDataModel delegacaoLazyDataModel;

	private List<Delegacao> delegacaoFiltered;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;

	private List<Campus> listaCampus;

	private Campus campus;

	@ManagedProperty(value = "#{pessoaDao}")
	private PessoaDao pessoaDao;

	private List<Pessoa> listaPessoa;

	private Pessoa pessoa;

	private Pessoa chefeDelegacao;
	private Pessoa oldChefeDelegacao;

	@ManagedProperty(value = "#{delegacaoPessoaDao}")
	private DelegacaoPessoaDao delegacaoPessoaDao;

	private DelegacaoPessoa delegacaoPessoa;

	@ManagedProperty(value = "#{crachasPdf}")
	public CrachasPdf crachasPdf;

	@ManagedProperty(value = "#{declaracoesPdf}")
	public DeclaracoesPdf declaracoesPdf;

	private Delegacao novaDelegacao;

	private List<DelegacaoPessoa> delegacaoPessoas;

	private Integer anoOld;

	public DelegacaoMB() {
		delegacaoPessoas = new ArrayList<>();
		delegacaoFiltered = new ArrayList<Delegacao>();
		listaPessoa = new ArrayList<Pessoa>();
	}

	public void criar() {
		delegacao = new Delegacao();
		campus = null;
	}

	@PostConstruct
	public void poust() {
		listaCampus = campusDao.listarAlfabetica();
	}

	public void remover() {

		try {
			for (DelegacaoPessoa dlg : delegacao.getDelegacaoPessoas()) {
				delegacaoPessoaDao.remover(dlg);
			}
			delegacaoDao.remover(delegacao);
		} catch (Exception e) {
			mensagemFaces("Erro!", "Não foi possível remover a delegação!");
		}
	}

	public void mensagemFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, message));
	}

	public void cancelar() {
		removerRegistrosRelacionamentos();
		delegacao = null;
		campus = null;
	}

	private void removerRegistrosRelacionamentos() {
		try {
			for (DelegacaoPessoa delegacaoPessoa : delegacao.getDelegacaoPessoas()) {
				if (delegacaoPessoa.getDelegacao() == null) {
					delegacaoPessoaDao.remover(delegacaoPessoa);
					delegacao.getDelegacaoPessoas().remove(delegacaoPessoa);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvar() {
		if (delegacao.getId() != null) {

			if (!anoOld.equals(delegacao.getAno())) {
				novaDelegacao = new Delegacao();
				novaDelegacao.setCampus(delegacao.getCampus());
				novaDelegacao.setNome(delegacao.getNome());
				novaDelegacao.setAno(delegacao.getAno());
				resalvarPessoasDelegacao(delegacao.getDelegacaoPessoas());
				delegacaoDao.salvar(novaDelegacao);
			} else {
				delegacaoDao.update(delegacao);
			}
		}

		else {
			delegacao.setCampus(campus);
			delegacaoDao.salvar(delegacao);
		}
		if (chefeDelegacao != null && !chefeDelegacao.equals(oldChefeDelegacao))

		{
			marcarChefe();
		}
	}

	private void resalvarPessoasDelegacao(List<DelegacaoPessoa> delegacaoPessoas) {
		for (DelegacaoPessoa delegacaoPessoa : delegacaoPessoas) {
			DelegacaoPessoa delegacaoPessoaNova = new DelegacaoPessoa();
			delegacaoPessoaNova.setPessoa(delegacaoPessoa.getPessoa());
			delegacaoPessoaNova.setChefe(delegacaoPessoa.isChefe());
			delegacaoPessoaDao.salvar(delegacaoPessoa);
			novaDelegacao.getDelegacaoPessoas().add(delegacaoPessoa);
		}
	}

	public void adicionarPessoa() {
		if (pessoa != null) {
			delegacaoPessoa = new DelegacaoPessoa();
			delegacaoPessoa.setPessoa(pessoa);
			delegacaoPessoaDao.salvar(delegacaoPessoa);
			delegacao.getDelegacaoPessoas().add(delegacaoPessoa);
			pessoa = new Pessoa();
		}
	}

	public void removerPessoa() {
		try {
			delegacaoPessoaDao.remover(delegacaoPessoa);
			delegacao.getDelegacaoPessoas().remove(delegacaoPessoa);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Pessoa> pesquisarPessoaNome(String nome) {

		List<Pessoa> listaPessoaNome;
		listaPessoa = new ArrayList<Pessoa>();
		if (delegacao.getId() != null) {
			listaPessoaNome = pessoaDao.pesquisarPorNomeParaDelegacao(nome, delegacao);
			verificarCampusListaPessoas(listaPessoaNome);
		} else {
			listaPessoaNome = pessoaDao.findByNome(nome);
			verificarCampusListaPessoas(listaPessoaNome);
		}
		return listaPessoa;
	}

	private void verificarCampusListaPessoas(List<Pessoa> listaPessoaNome) {
		Pessoa pessoaVerifica = new Pessoa();
		for (int i = 0; i < listaPessoaNome.size(); i++) {
			pessoaVerifica = (Pessoa) listaPessoaNome.get(i);
			verificaPessoa(pessoaVerifica);
		}
	}

	public void verificaPessoa(Pessoa pessoaVerifica) {
		if (pessoaVerifica.getTipo().equals(TipoPessoa.ROLE_TEC_ADM)) {
			TecnicoAdministrativo tecAdm = (TecnicoAdministrativo) pessoaVerifica;
			if (tecAdm.getCampus().equals(campus)) {
				listaPessoa.add(tecAdm);
			}
		} else if (pessoaVerifica.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)) {
			TecnicoEsportivo tecEsp = (TecnicoEsportivo) pessoaVerifica;
			if (tecEsp.getCampus().equals(campus)) {
				listaPessoa.add(tecEsp);
			}
		} else if (pessoaVerifica.getTipo().equals(TipoPessoa.ROLE_COORDENADOR)) {
			CoordenadorPea coordPea = (CoordenadorPea) pessoaVerifica;
			if (coordPea.getCampus().equals(campus)) {
				listaPessoa.add(coordPea);
			}
		} else if (pessoaVerifica.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {
			Estudante estudante = (Estudante) pessoaVerifica;
			if (estudante.getCampus().equals(campus)) {
				listaPessoa.add(estudante);
			}
		}
	}

	public void onItemSelect(SelectEvent event) {
		Object item = event.getObject();
		pessoa = (Pessoa) item;
	}

	public void gerarCrachas(Delegacao delegacao) {
		crachasPdf.gerarPdfDelegacao(delegacaoDao.listarPessoas(delegacao));
	}

	public void gerarDeclaracoes(Delegacao delegacao) {
		declaracoesPdf.gerarDeclaracoes(delegacaoDao.listarPessoas(delegacao));
	}

	public void marcarChefe() {
		for (DelegacaoPessoa dlg : delegacao.getDelegacaoPessoas()) {
			if (dlg.getPessoa().getId() == chefeDelegacao.getId()) {
				dlg.setChefe(true);
				delegacaoPessoaDao.update(dlg);
			} else if (dlg.isChefe()) {
				dlg.setChefe(false);
				delegacaoPessoaDao.update(dlg);
			}
		}
	}

	public Delegacao getDelegacao() {
		return delegacao;
	}

	public void setDelegacao(Delegacao delegacao) {
		campus = delegacao.getCampus();
		for (DelegacaoPessoa delegacaoPessoa : delegacao.getDelegacaoPessoas()) {
			if (delegacaoPessoa.isChefe()) {
				chefeDelegacao = delegacaoPessoa.getPessoa();
				oldChefeDelegacao = chefeDelegacao;
			}
		}
		anoOld = delegacao.getAno();
		this.delegacao = delegacao;
	}

	public DelegacaoDao getDelegacaoDao() {
		return delegacaoDao;
	}

	public void setDelegacaoDao(DelegacaoDao delegacaoDao) {
		this.delegacaoDao = delegacaoDao;
	}

	public DelegacaoLazyDataModel getDelegacaoLazyDataModel() {
		return delegacaoLazyDataModel;
	}

	public void setDelegacaoLazyDataModel(DelegacaoLazyDataModel delegacaoLazyDataModel) {
		this.delegacaoLazyDataModel = delegacaoLazyDataModel;
	}

	public List<Delegacao> getDelegacaoFiltered() {
		return delegacaoFiltered;
	}

	public void setDelegacaoFiltered(List<Delegacao> delegacaoFiltered) {
		this.delegacaoFiltered = delegacaoFiltered;
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

	public PessoaDao getPessoaDao() {
		return pessoaDao;
	}

	public void setPessoaDao(PessoaDao pessoaDao) {
		this.pessoaDao = pessoaDao;
	}

	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DelegacaoPessoaDao getDelegacaoPessoaDao() {
		return delegacaoPessoaDao;
	}

	public void setDelegacaoPessoaDao(DelegacaoPessoaDao delegacaoPessoaDao) {
		this.delegacaoPessoaDao = delegacaoPessoaDao;
	}

	public DelegacaoPessoa getDelegacaoPessoa() {
		return delegacaoPessoa;
	}

	public void setDelegacaoPessoa(DelegacaoPessoa delegacaoPessoa) {
		this.delegacaoPessoa = delegacaoPessoa;
	}

	public CrachasPdf getCrachasPdf() {
		return crachasPdf;
	}

	public void setCrachasPdf(CrachasPdf crachasPdf) {
		this.crachasPdf = crachasPdf;
	}

	public DeclaracoesPdf getDeclaracoesPdf() {
		return declaracoesPdf;
	}

	public void setDeclaracoesPdf(DeclaracoesPdf declaracoesPdf) {
		this.declaracoesPdf = declaracoesPdf;
	}

	public Pessoa getChefeDelegacao() {
		return chefeDelegacao;
	}

	public void setChefeDelegacao(Pessoa chefeDelegacao) {
		this.chefeDelegacao = chefeDelegacao;
	}

	public Pessoa getOldChefeDelegacao() {
		return oldChefeDelegacao;
	}

	public void setOldChefeDelegacao(Pessoa oldChefeDelegacao) {
		this.oldChefeDelegacao = oldChefeDelegacao;
	}

	public Delegacao getNovaDelegacao() {
		return novaDelegacao;
	}

	public void setNovaDelegacao(Delegacao novaDelegacao) {
		this.novaDelegacao = novaDelegacao;
	}

	public List<DelegacaoPessoa> getDelegacaoPessoas() {
		delegacaoPessoas.clear();
		if (delegacao != null) {
			delegacaoPessoas.addAll(delegacao.getDelegacaoPessoas());
			for (DelegacaoPessoa delegacaoPess : delegacao.getDelegacaoPessoas()) {
				if (delegacaoPess.getPessoa().getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {
					delegacaoPessoas.remove(delegacaoPess);
				}
			}
		}
		return delegacaoPessoas;
	}

	public void setDelegacaoPessoas(List<DelegacaoPessoa> delegacaoPessoas) {
		this.delegacaoPessoas = delegacaoPessoas;
	}
}
