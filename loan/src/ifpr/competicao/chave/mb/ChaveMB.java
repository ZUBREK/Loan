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
import ifpr.competicao.partidaTime.PartidaTime;
import ifpr.competicao.partidaTime.dao.PartidaTimeDao;
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

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;

	@ManagedProperty(value = "#{pontosTimeDao}")
	private PontosTimeDao pontosTimeDao;

	@ManagedProperty(value = "#{partidaTimeDao}")
	private PartidaTimeDao partidaTimeDao;

	@ManagedProperty(value = "#{chaveLazyDataModel}")
	private ChaveLazyDataModel chaveLazyDataModel;

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	private TipoCompeticao tipo;

	private TreeNode rootNode;
	private List<TreeNode> nodes;

	private List<PartidaTime> partidaTimes;
	private List<Time> timesSemNullSemDuplicado;

	private Modalidade modalidade;

	private Partida partida;

	private int qtdTimes;

	private List<Local> listaLocais;

	private Local local;

	private TreeNode selectedNode;

	private PartidaTime ptpAdversario;
	private PartidaTime partidaTime;
	private PartidaTime partidaTimeMeuPai;
	private Time timeSelectOne;
	private Time timeDataTable;
	private TreeNode meuPai;
	private TreeNode node;
	private PontosTime pontosTime;
	private List<Time> times;

	TipoCompeticao[] tipos;

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
		for (int i = 0; i < partidaTimes.size(); i++) {
			if (i % 3 == 0) {
				if (partidaTimes.get(i).getPlacar() == -2) {
					Time time = new Time();
					time.setNome("EMPATE");
					partidaTimes.get(i).setTime(time);
				}
				node = adicionarNodeParentTipo(partidaTimes.get(i), nodes.get(0));
			} else {
				adicionarNodeParentTipo(partidaTimes.get(i), node);
			}
		}
	}

	private void gerarNodeTipoMataMata() {
		pegarTimes();
		Collections.reverse(partidaTimes);
		int i2 = 0;
		for (int i = 0; i < partidaTimes.size(); i++) {
			adicionarNodeParentTipo(partidaTimes.get(i), nodes.get(i2));
			if (i % 2 == 0) {
				i2++;
			}
		}
	}

	public void pegarTimes() {
		partidaTimes = new ArrayList<>();
		List<Partida> partidas = chave.getPartidas();
		Collections.sort(partidas, new Comparator<Partida>() {
			@Override
			public int compare(Partida p1, Partida p2) {
				return p1.getId() - p2.getId();
			}
		});
		for (Partida partida : partidas) {
			List<PartidaTime> ptps = partida.getPartidasTimesPlacares();
			Collections.sort(ptps, new Comparator<PartidaTime>() {
				@Override
				public int compare(PartidaTime ptp1, PartidaTime ptp2) {
					return ptp1.getId() - ptp2.getId();
				}
			});

			if (!chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS) || timeSelectOne == null) {
				adicionarItensLista(ptps);
			} else if (verificarTimeEmPtps(ptps)) {
				adicionarItensLista(ptps);
			}
		}
		qtdTimes = partidaTimes.size();
		resetarListaSelectOne();
		setarListaSelectOneMenu();
	}

	private void setarListaSelectOneMenu() {
		for (int i = 0; i < partidaTimes.size(); i++) {
			Time time = partidaTimes.get(i).getTime();
			if (time != null && !timesSemNullSemDuplicado.contains(time)) {
				timesSemNullSemDuplicado.add(time);
			}
		}
		Collections.sort(timesSemNullSemDuplicado, new Comparator<Time>() {
			@Override
			public int compare(Time time1, Time time2) {
				return time1.getPontosTime().getPontos() - time2.getPontosTime().getPontos();
			}
		});
	}

	public void resetarListaSelectOne() {
		timesSemNullSemDuplicado = new ArrayList<>();
	}

	private boolean verificarTimeEmPtps(List<PartidaTime> ptps) {
		for (PartidaTime partidaTimePlacar : ptps) {
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

	private void adicionarItensLista(List<PartidaTime> ptps) {
		for (PartidaTime partidaTimePlacar : ptps) {
			partidaTimes.add(partidaTimePlacar);
		}
	}

	public void criar() {
		chave = new Chave();
	}

	public void remover() {
		try {
			chaveDao.remover(chave);
			pegarTimes();
			for (Time time : timesSemNullSemDuplicado) {
				PontosTime pt = time.getPontosTime();
				pt.setDerrotas(0);
				pt.setEmpates(0);
				pt.setPontos(0);
				pt.setVitorias(0);
				pontosTimeDao.update(pt);
			}
		} catch (ConstraintViolationException e) {
			// facesmessage bagaça
		}

	}

	public void cancelar() {

	}

	public void salvarChave() {
		chave.setModalidade(modalidade);
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
		setarPartidaTimePlacar();
		if (i != -1) {
			partidaTime.setTime(times.get(i));
		}
		partidaTimeDao.salvar(partidaTime);
		partida.getPartidasTimesPlacares().add(partidaTime);
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

	private void setarPartidaTimePlacar() {
		partidaTime = new PartidaTime();
		partidaTime.setPartida(partida);
		partidaTime.setPlacar(-1);
	}

	public Chave getChave() {
		return chave;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		try {
			partidaTime = (PartidaTime) selectedNode.getData();
			meuPai = selectedNode.getParent();
			partidaTimeMeuPai = (PartidaTime) meuPai.getData();
			List<TreeNode> nodesCompetidores = meuPai.getChildren();
			if (partidaTime.getTime() != null) {
				if (partidaTimeMeuPai.getTime() == null) {
					if (partidaTime.getPlacar() == -1) {
						pontosTime = partidaTime.getTime().getPontosTime();
						partida = partidaTime.getPartida();
						for (int i = 0; i < nodesCompetidores.size(); i++) {
							PartidaTime ptpAtual = (PartidaTime) nodesCompetidores.get(i).getData();
							if (!ptpAtual.equals(partidaTime)) {
								ptpAdversario = ptpAtual;
								RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').show()");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvarPartidaTime() {
		RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').hide()");
		salvarPartida();
		if (partidaTime.getPlacar() != -1) {
			partidaTimeDao.update(partidaTime);
			PontosTime pontosTimeAdversario = ptpAdversario.getTime().getPontosTime();
			Time timeAdversario = ptpAdversario.getTime();
			if (partidaTime.getPlacar() > ptpAdversario.getPlacar() && ptpAdversario.getPlacar() != -1) {

				setarTimeVitoriosoPtp(partidaTime.getTime());
				setarPlacarTime(pontosTimeAdversario, 0);
				setarPlacarTime(pontosTime, 3);
			} else if (partidaTime.getPlacar() < ptpAdversario.getPlacar()) {

				setarTimeVitoriosoPtp(timeAdversario);
				setarPlacarTime(pontosTime, 0);
				setarPlacarTime(pontosTimeAdversario, 3);
			} else if (partidaTime.getPlacar() == ptpAdversario.getPlacar()
					&& !chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
				setarPlacarTime(pontosTime, 0);
				setarPlacarTime(pontosTimeAdversario, 0);
				partidaTimeMeuPai.setPlacar(-2);
				partidaTimeDao.update(partidaTimeMeuPai);
			}
			chave = chaveDao.findById(chave.getId());
			iniciarTreeNode();
		}
		RequestContext.getCurrentInstance().execute("PF('confirmPartidaDialog').hide()");
	}

	private void setarPlacarTime(PontosTime pontosTime2, int pontosGanhos) {
		pontosTime2.setPontos(pontosTime2.getPontos() + pontosGanhos);
		if (pontosGanhos > 1) {
			pontosTime2.setVitorias(pontosTime2.getVitorias() + 1);
		} else if (pontosGanhos == 1) {
			pontosTime2.setEmpates(pontosTime2.getEmpates() + 1);
		} else {
			pontosTime2.setDerrotas(pontosTime2.getDerrotas() + 1);
		}
		pontosTimeDao.update(pontosTime2);
	}

	public void setarTimeVitoriosoPtp(Time time) {
		partidaTimeMeuPai.setTime(time);
		partidaTimeDao.update(partidaTimeMeuPai);
	}

	public void cancelarChave() {
		timeSelectOne = null;
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

	public TipoCompeticao[] getTipos() {
		return tipos;
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

	public PartidaTime getPtpAdversario() {
		return ptpAdversario;
	}

	public void setPtpAdversario(PartidaTime ptpAdversario) {
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

	public void timeSelectionChanged(final AjaxBehaviorEvent event) {
		chaveDao.findById(chave.getId());
		iniciarTreeNode();
	}

	public void modalidadeSelectionChanged(final AjaxBehaviorEvent event) {
		tipos = TipoCompeticao.values();
		times = timeDao.pesquisarPorModalidade(modalidade);
		if (!verificarTamanhoTime()) {
			ArrayList<TipoCompeticao> tiposOld = new ArrayList<TipoCompeticao>(Arrays.asList(tipos));
			tiposOld.remove(TipoCompeticao.MATA_MATA);
			TipoCompeticao[] tiposNew = tiposOld.toArray(new TipoCompeticao[tiposOld.size()]);
			tipos = tiposNew;
		}
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

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public PartidaTime getPartidaTime() {
		return partidaTime;
	}

	public void setPartidaTime(PartidaTime partidaTime) {
		this.partidaTime = partidaTime;
	}

	public PartidaTime getPartidaTimeMeuPai() {
		return partidaTimeMeuPai;
	}

	public void setPartidaTimeMeuPai(PartidaTime partidaTimeMeuPai) {
		this.partidaTimeMeuPai = partidaTimeMeuPai;
	}

	public Time getTimeDataTable() {
		return timeDataTable;
	}

	public void setTimeDataTable(Time timeDataTable) {
		this.timeDataTable = timeDataTable;
	}

	public TipoCompeticao getTipoMataMata() {
		return TipoCompeticao.MATA_MATA;
	}

	public PartidaTimeDao getPartidaTimeDao() {
		return partidaTimeDao;
	}

	public void setPartidaTimeDao(PartidaTimeDao partidaTimeDao) {
		this.partidaTimeDao = partidaTimeDao;
	}

	public List<PartidaTime> getPartidaTimes() {
		return partidaTimes;
	}

	public void setPartidaTimes(List<PartidaTime> partidaTimes) {
		this.partidaTimes = partidaTimes;
	}

	public void setTipos(TipoCompeticao[] tipos) {
		this.tipos = tipos;
	}

}
