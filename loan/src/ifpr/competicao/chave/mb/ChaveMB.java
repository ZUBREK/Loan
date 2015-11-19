package ifpr.competicao.chave.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.validation.ConstraintViolationException;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ifpr.competicao.chave.Chave;
import ifpr.competicao.chave.TipoCompeticao;
import ifpr.competicao.chave.dao.ChaveDao;
import ifpr.competicao.chave.model.ChaveLazyDataModel;
import ifpr.competicao.partida.Partida;
import ifpr.competicao.partida.dao.PartidaDao;
import ifpr.competicao.partida.local.Local;
import ifpr.competicao.partida.local.dao.LocalDao;
import ifpr.competicao.partidaTimePlacar.PartidaTimePlacar;
import ifpr.competicao.partidaTimePlacar.dao.PartidaTimePlacarDao;
import ifpr.competicao.placar.Placar;
import ifpr.competicao.placar.dao.PlacarDao;
import ifpr.competicao.time.Time;
import ifpr.competicao.time.dao.TimeDao;
import ifpr.competicao.time.pontos.PontosTime;
import ifpr.competicao.time.pontos.dao.PontosTimeDao;
import ifpr.modalidade.Modalidade;

@ManagedBean(name = "chaveMB")
@ViewScoped
public class ChaveMB {

	private Chave chave;

	private List<Chave> chaveList;

	@ManagedProperty(value = "#{chaveDao}")
	private ChaveDao chaveDao;

	@ManagedProperty(value = "#{partidaDao}")
	private PartidaDao partidaDao;

	@ManagedProperty(value = "#{placarDao}")
	private PlacarDao placarDao;

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;

	@ManagedProperty(value = "#{pontosTimeDao}")
	private PontosTimeDao pontosTimeDao;

	@ManagedProperty(value = "#{partidaTimePlacarDao}")
	private PartidaTimePlacarDao partidaTimePlacarDao;

	@ManagedProperty(value = "#{chaveLazyDataModel}")
	private ChaveLazyDataModel chaveLazyDataModel;

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	private TipoCompeticao tipo;

	private TreeNode rootNode;
	private List<TreeNode> nodes;

	private List<PartidaTimePlacar> partidaTimesPlacares;
	private List<Time> timesSemNullSemDuplicado;

	private Modalidade modalidade;

	private Placar placar;

	private PartidaTimePlacar partidaTimePlacar;

	private Partida partida;

	private int qtdTimes;

	private List<Local> listaLocais;

	private Local local;

	private TreeNode selectedNode;

	private PartidaTimePlacar ptpAdversario;
	private PartidaTimePlacar partidaTime;
	private PartidaTimePlacar partidaTimeMeuPai;
	private Time timeSelectOne;
	private TreeNode meuPai;
	private TreeNode node;
	private PontosTime pontosTime;
	private List<Time> times;

	public ChaveMB() {
		chave = new Chave();
		chave.setPrimeiraFase(true);
	}

	@PostConstruct
	public void init() {
		try {
			listaLocais = localDao.listAsc();
		} catch (Exception e) {
			// nao tem local
		}
	}

	public TreeNode adicionarNodeParentTipo(Object nome, TreeNode nodeRoot) {
		TreeNode node = new DefaultTreeNode(nome, nodeRoot);
		node.setExpanded(true);
		nodes.add(node);
		return node;
	}

	public void iniciarTreeNode() {
		nodes = new ArrayList<>();
		rootNode = new DefaultTreeNode(chave.getNome(), null);
		rootNode.setExpanded(true);
		nodes.add(rootNode);

		if (chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {

		} else if (chave.getTipo().equals(TipoCompeticao.GRUPOS)) {

		} else if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {
			gerarNodeTipoPtosCorridos();
		} else if (chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
			gerarNodeTipoMataMata();
		}

	}

	private void gerarNodeTipoPtosCorridos() {
		pegarTimes();
		for (int i = 0; i < partidaTimesPlacares.size(); i++) {
			if (i % 3 == 0) {
				if (partidaTimesPlacares.get(i).getPlacar().getPontos() == -2) {
					node = adicionarNodeParentTipo(partidaTimesPlacares.get(i), nodes.get(0));
				} else {
					Time time = new Time();
					time.setNome("EMPATE");
					partidaTimesPlacares.get(i).setTime(time);
					node = adicionarNodeParentTipo(partidaTimesPlacares.get(i), nodes.get(0));
				}
			} else {
				adicionarNodeParentTipo(partidaTimesPlacares.get(i), node);
			}
		}
	}

	private void gerarNodeTipoMataMata() {
		pegarTimes();
		Collections.reverse(partidaTimesPlacares);
		int i2 = 0;
		for (int i = 0; i < partidaTimesPlacares.size(); i++) {
			adicionarNodeParentTipo(partidaTimesPlacares.get(i), nodes.get(i2));
			if (i % 2 == 0) {
				i2++;
			}
		}
	}

	public void pegarTimes() {
		partidaTimesPlacares = new ArrayList<>();
		List<Partida> partidas = chave.getPartidas();
		Collections.sort(partidas, new Comparator<Partida>() {
			@Override
			public int compare(Partida p1, Partida p2) {
				return p1.getId() - p2.getId();
			}
		});
		for (Partida partida : partidas) {
			List<PartidaTimePlacar> ptps = partida.getPartidasTimesPlacares();
			Collections.sort(ptps, new Comparator<PartidaTimePlacar>() {
				@Override
				public int compare(PartidaTimePlacar ptp1, PartidaTimePlacar ptp2) {
					return ptp1.getId() - ptp2.getId();
				}
			});

			if (!chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS) || timeSelectOne == null) {
				adicionarItensLista(ptps);
			} else if (verificarTimeEmPtps(ptps)) {
				adicionarItensLista(ptps);
			}
		}
		qtdTimes = partidaTimesPlacares.size();
		resetarListaSelectOne();
		if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {
			setarListaSelectOneMenu();
		}
	}

	private void setarListaSelectOneMenu() {
		for (int i = 0; i < partidaTimesPlacares.size(); i++) {
			Time time = partidaTimesPlacares.get(i).getTime();
			if (time != null && !timesSemNullSemDuplicado.contains(time)) {
				timesSemNullSemDuplicado.add(time);
			}
		}
	}

	public void resetarListaSelectOne() {
		timesSemNullSemDuplicado = new ArrayList<>();
	}

	private boolean verificarTimeEmPtps(List<PartidaTimePlacar> ptps) {
		for (PartidaTimePlacar partidaTimePlacar : ptps) {
			Time time = partidaTimePlacar.getTime();
			if (time != null) {
				if (timeSelectOne != null) {
					if (time.equals(timeSelectOne)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void adicionarItensLista(List<PartidaTimePlacar> ptps) {
		for (PartidaTimePlacar partidaTimePlacar : ptps) {
			partidaTimesPlacares.add(partidaTimePlacar);
		}
	}

	public void criar() {
		chave = new Chave();
	}

	public void remover() {
		try {
			chaveDao.remover(chave);
		} catch (ConstraintViolationException e) {
			// facesmessage bagaça
		}

	}

	public void cancelar() {

	}

	public void salvarChave() {
		chave.setTipo(tipo);
		if (chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {

		} else if (chave.getTipo().equals(TipoCompeticao.GRUPOS)) {

		} else if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {
			gerarPartidasTipoPtosCorridos();
		} else if (chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
			gerarPartidasTipoMataMata();
		}
		chaveDao.salvar(chave);
	}

	private void gerarPartidasTipoPtosCorridos() {
		qtdTimes = times.size();
		for (int i = 0; i < qtdTimes; i++) {
			for (int i2 = qtdTimes - 1; i2 > i; i2--) {
				partida = new Partida();
				chave.getPartidas().add(partida);
				salvarPartida();
				criarSalvarPlacarPartida(-1);
				criarSalvarPlacarPartida(i);
				criarSalvarPlacarPartida(i2);
			}
		}
	}

	private void criarSalvarPlacarPartida(int i) {
		placar = new Placar();
		placar.setPontos(-1);
		salvarPlacar(placar);
		setarPartidaTimePlacar();
		if (i != -1) {
			partidaTimePlacar.setTime(times.get(i));
		}
		partidaTimePlacarDao.salvar(partidaTimePlacar);
		partida.getPartidasTimesPlacares().add(partidaTimePlacar);
		salvarPartida();
	}

	public void gerarPartidasTipoMataMata() {
		randomizarTimes();
		gerarPartidasPrimeiraFaseMataMata();
		criarPartidasRestantes();

	}

	private void criarPartidasRestantes() {
		while (qtdTimes >= 1) {
			qtdTimes /= 2;
			for (int i = 0; i < qtdTimes; i++) {
				if (i % 2 == 0) {
					partida = new Partida();
					chave.getPartidas().add(partida);
					salvarPartida();
				}
				criarSalvarPlacarPartida(-1);
			}
		}
	}

	private void gerarPartidasPrimeiraFaseMataMata() {
		qtdTimes = times.size();
		for (int i = 0; i < qtdTimes; i++) {
			if (i % 2 == 0) {
				partida = new Partida();
				chave.getPartidas().add(partida);
				salvarPartida();
			}
			criarSalvarPlacarPartida(i);
		}
	}

	public void randomizarTimes() {
		long seed = System.nanoTime();
		Collections.shuffle(times, new Random(seed));
	}

	public void salvarPartida() {
		if (partida.getId() == null) {
			partidaDao.salvar(partida);
		} else {
			partida.setLocal(local);
			partidaDao.update(partida);
		}
	}

	public void salvarPlacar(Placar placar) {
		if (placar.getId() == null) {
			placarDao.salvar(placar);
		} else {
			placarDao.update(placar);
		}
	}

	private void setarPartidaTimePlacar() {
		partidaTimePlacar = new PartidaTimePlacar();
		partidaTimePlacar.setPartida(partida);
		partidaTimePlacar.setPlacar(placar);
	}

	public Chave getChave() {
		return chave;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		partidaTime = (PartidaTimePlacar) selectedNode.getData();
		meuPai = selectedNode.getParent();
		partidaTimeMeuPai = (PartidaTimePlacar) meuPai.getData();
		List<TreeNode> nodesCompetidores = meuPai.getChildren();
		if (partidaTime.getTime() != null) {
			if (partidaTimeMeuPai.getTime() == null) {
				pontosTime = partidaTime.getTime().getPontosTime();
				partida = partidaTime.getPartida();
				placar = partidaTime.getPlacar();
				for (int i = 0; i < nodesCompetidores.size(); i++) {
					PartidaTimePlacar ptpAtual = (PartidaTimePlacar) nodesCompetidores.get(i).getData();
					if (!ptpAtual.equals(partidaTime)) {
						ptpAdversario = ptpAtual;
						RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').show()");
					}
				}
			}
		}
	}

	public void salvarPartidaPlacar() {
		salvarPartida();
		if (placar.getPontos() != -1) {
			placarDao.update(placar);
			PontosTime pontosTimeAdversario = ptpAdversario.getTime().getPontosTime();
			Time timeAdversario = ptpAdversario.getTime();
			Placar placarAdversario = ptpAdversario.getPlacar();
			if (placar.getPontos() > placarAdversario.getPontos() && placarAdversario.getPontos() != -1) {

				setarPontosTimeDerrotado(pontosTimeAdversario);
				setarPontosTimeVitorioso(pontosTime);
				setarTimeVitoriosoPtp(partidaTime.getTime());

			} else if (placar.getPontos() < placarAdversario.getPontos()) {
				setarPontosTimeDerrotado(pontosTime);
				setarPontosTimeVitorioso(pontosTimeAdversario);
				setarTimeVitoriosoPtp(timeAdversario);
			} else if (placar.getPontos() == placarAdversario.getPontos()
					&& !chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
				setarEmpateTime(pontosTime);
				setarEmpateTime(pontosTimeAdversario);
				Placar placar = new Placar();
				placar.setPontos(-2);
				partidaTimeMeuPai.setPlacar(placar);
				partidaTimePlacarDao.update(partidaTimeMeuPai);
			}
			setarPontoTotalTime(pontosTime);
			chave = chaveDao.findById(chave.getId());
			iniciarTreeNode();
		}
		RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').hide()");
	}

	private void setarPontoTotalTime(PontosTime pontosTime2) {
		pontosTime2.setSaldoPontos(pontosTime2.getSaldoPontos() + placar.getPontos());
		pontosTimeDao.update(pontosTime2);
	}

	private void setarEmpateTime(PontosTime pontosTime2) {
		pontosTime2.setVitorias(pontosTime2.getEmpates() + 1);
		pontosTimeDao.update(pontosTime2);
	}

	private void setarPontosTimeVitorioso(PontosTime pontosTime2) {
		pontosTime2.setVitorias(pontosTime2.getVitorias() + 1);
		pontosTimeDao.update(pontosTime2);

	}

	private void setarPontosTimeDerrotado(PontosTime pontosTime2) {
		pontosTime2.setDerrotas(pontosTime2.getDerrotas() + 1);
		pontosTimeDao.update(pontosTime2);
	}

	public void setarTimeVitoriosoPtp(Time time) {
		partidaTimeMeuPai.setTime(time);
		partidaTimePlacarDao.update(partidaTimeMeuPai);
	}

	public void cancelarChave() {
		timeSelectOne = null;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public List<TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeNode> nodes) {
		this.nodes = nodes;
	}

	public void setChave(Chave chave) {
		this.chave = chave;
	}

	public List<Chave> getChaveList() {
		return chaveList;
	}

	public void setChaveList(List<Chave> chaveList) {
		this.chaveList = chaveList;
	}

	public ChaveDao getChaveDao() {
		return chaveDao;
	}

	public void setChaveDao(ChaveDao chaveDao) {
		this.chaveDao = chaveDao;
	}

	public ChaveLazyDataModel getChaveLazyDataModel() {
		return chaveLazyDataModel;
	}

	public void setChaveLazyDataModel(ChaveLazyDataModel chaveLazyDataModel) {
		this.chaveLazyDataModel = chaveLazyDataModel;
	}

	public PartidaDao getPartidaDao() {
		return partidaDao;
	}

	public void setPartidaDao(PartidaDao partidaDao) {
		this.partidaDao = partidaDao;
	}

	public PlacarDao getPlacarDao() {
		return placarDao;
	}

	public void setPlacarDao(PlacarDao placarDao) {
		this.placarDao = placarDao;
	}

	public TipoCompeticao[] getTipos() {
		chave.setModalidade(modalidade);
		TipoCompeticao[] tipos = TipoCompeticao.values();
		times = timeDao.pesquisarPorModalidade(chave.getModalidade());
		if (verificarTamanhoTime()) {
			return tipos;
		} else {
			ArrayList<TipoCompeticao> tiposOld = new ArrayList<TipoCompeticao>(Arrays.asList(tipos));
			tiposOld.remove(0);
			TipoCompeticao[] tiposNew = tiposOld.toArray(new TipoCompeticao[tiposOld.size()]);
			return tiposNew;
		}
	}

	public boolean verificarTamanhoTime() {
		float qtdTimes = times.size();
		while (qtdTimes > 1 && qtdTimes % 2 == 0) {
			qtdTimes = qtdTimes / (float) 2;
			if (qtdTimes == 1) {
				return true;
			}
		}
		return false;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
	}

	public TipoCompeticao getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompeticao tipo) {
		this.tipo = tipo;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public PartidaTimePlacarDao getPartidaTimePlacarDao() {
		return partidaTimePlacarDao;
	}

	public void setPartidaTimePlacarDao(PartidaTimePlacarDao partidaTimePlacarDao) {
		this.partidaTimePlacarDao = partidaTimePlacarDao;
	}

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public PartidaTimePlacar getPartidaTimePlacar() {
		return partidaTimePlacar;
	}

	public void setPartidaTimePlacar(PartidaTimePlacar partidaTimePlacar) {
		this.partidaTimePlacar = partidaTimePlacar;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public int getQtdTimes() {
		return qtdTimes;
	}

	public void setQtdTimes(int qtdTimes) {
		this.qtdTimes = qtdTimes;
	}

	public List<Local> getListaLocais() {
		return listaLocais;
	}

	public void setListaLocais(List<Local> listaLocais) {
		this.listaLocais = listaLocais;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public LocalDao getLocalDao() {
		return localDao;
	}

	public void setLocalDao(LocalDao localDao) {
		this.localDao = localDao;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public PartidaTimePlacar getPtpAdversario() {
		return ptpAdversario;
	}

	public void setPtpAdversario(PartidaTimePlacar ptpAdversario) {
		this.ptpAdversario = ptpAdversario;
	}

	public Time getTimeSelectOne() {
		return timeSelectOne;
	}

	public void setTimeSelectOne(Time timeSelectOne) {
		this.timeSelectOne = timeSelectOne;
	}

	public TreeNode getMeuPai() {
		return meuPai;
	}

	public void setMeuPai(TreeNode meuPai) {
		this.meuPai = meuPai;
	}

	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

	public List<Time> getTimesSemNullSemDuplicado() {
		return timesSemNullSemDuplicado;
	}

	public void setTimesSemNullSemDuplicado(List<Time> timesSemNullSemDuplicado) {
		this.timesSemNullSemDuplicado = timesSemNullSemDuplicado;
	}

	public void subjectSelectionChanged(final AjaxBehaviorEvent event) {
		chaveDao.findById(chave.getId());
		iniciarTreeNode();
	}

	public PontosTimeDao getPontosTimeDao() {
		return pontosTimeDao;
	}

	public void setPontosTimeDao(PontosTimeDao pontosTimeDao) {
		this.pontosTimeDao = pontosTimeDao;
	}

	public PontosTime getPontosTime() {
		return pontosTime;
	}

	public void setPontosTime(PontosTime pontosTime) {
		this.pontosTime = pontosTime;
	}

	public List<PartidaTimePlacar> getPartidaTimesPlacares() {
		return partidaTimesPlacares;
	}

	public void setPartidaTimesPlacares(List<PartidaTimePlacar> partidaTimesPlacares) {
		this.partidaTimesPlacares = partidaTimesPlacares;
	}

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public PartidaTimePlacar getPartidaTime() {
		return partidaTime;
	}

	public void setPartidaTime(PartidaTimePlacar partidaTime) {
		this.partidaTime = partidaTime;
	}

	public PartidaTimePlacar getPartidaTimeMeuPai() {
		return partidaTimeMeuPai;
	}

	public void setPartidaTimeMeuPai(PartidaTimePlacar partidaTimeMeuPai) {
		this.partidaTimeMeuPai = partidaTimeMeuPai;
	}

}
