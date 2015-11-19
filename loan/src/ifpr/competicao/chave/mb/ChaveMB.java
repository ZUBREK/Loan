package ifpr.competicao.chave.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import ifpr.modalidade.Modalidade;

@ManagedBean(name = "chaveMB")
@ViewScoped
public class ChaveMB {
	private final String TIPO_CARREGADOR = "carregador";
	private final String TIPO_TIME = "time";

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

	@ManagedProperty(value = "#{partidaTimePlacarDao}")
	private PartidaTimePlacarDao partidaTimePlacarDao;

	@ManagedProperty(value = "#{chaveLazyDataModel}")
	private ChaveLazyDataModel chaveLazyDataModel;

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	private TipoCompeticao tipo;

	private TreeNode rootNode;
	private List<TreeNode> nodes;

	private List<Time> times;
	private List<Time> timesSemNullSemDuplicado;

	private Modalidade modalidade;

	private Placar placar;

	private PartidaTimePlacar partidaTimePlacar;

	private Partida partida;

	private int qtdTimes;

	private List<Local> listaLocais;

	private Local local;

	private TreeNode selectedNode;

	private PartidaTimePlacar proximaPtp;
	private Time time;
	private Time timeSelectOne;
	private TreeNode meuPai;
	private TreeNode node;

	public ChaveMB() {
		chave = new Chave();
		chave.setPrimeiraFase(true);
	}

	@PostConstruct
	public void init() {
		listaLocais = localDao.listAsc();
		listaLocais.get(0);
	}

	public TreeNode adicionarNodeParentTipo(Object nome, TreeNode nodeRoot, String tipo) {
		TreeNode node = new DefaultTreeNode(tipo, nome, nodeRoot);
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
		for (int i = 0; i < times.size(); i++) {
			if (i % 3 == 0) {
				if (times.get(i).getNome().equals("")) {
					node = adicionarNodeParentTipo(times.get(i), nodes.get(0), TIPO_CARREGADOR);
				} else {
					node = adicionarNodeParentTipo(times.get(i), nodes.get(0), TIPO_TIME);
				}
			} else {
				adicionarNodeParentTipo(times.get(i), node, TIPO_TIME);
			}
		}
	}

	private void gerarNodeTipoMataMata() {
		pegarTimes();

		Collections.reverse(times);
		int i2 = 0;
		for (int i = 0; i < times.size(); i++) {
			if (times.get(i).getNome().equals("")) {
				adicionarNodeParentTipo(times.get(i), nodes.get(i2), TIPO_CARREGADOR);
			} else {
				adicionarNodeParentTipo(times.get(i), nodes.get(i2), TIPO_TIME);
			}
			if (i % 2 == 0) {
				i2++;
			}
		}
	}

	public void pegarTimes() {
		times = new ArrayList<>();
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
		qtdTimes = times.size();
		resetarListaSelectOne();
		if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {
			setarListaSelectOneMenu();
		}
	}

	private void setarListaSelectOneMenu() {
		Set<Time> hs = new HashSet<>();
		hs.addAll(times);
		timesSemNullSemDuplicado.addAll(hs);
		for (Time time : times) {
			if (time.getNome().equals("")) {
				timesSemNullSemDuplicado.remove(time);
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
			Time time2 = partidaTimePlacar.getTime();
			if (time2 == null) {
				time2 = new Time();
				time2.setNome("");
				time2.setId(partidaTimePlacar.getId());
			}
			times.add(time2);
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
		placar.setResultado(-1);
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
		time = (Time) selectedNode.getData();
		meuPai = selectedNode.getParent();
		if (!time.getNome().equals("")) {
			if (!meuPai.getData().equals(time)) {
				if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {
					if (meuPai.getType().equals(TIPO_CARREGADOR)) {
						partidaTimePlacar = partidaTimePlacarDao.findById(((Time) (meuPai.getData())).getId());
					} else {
						partidaTimePlacar = procurarPartidaTimePlacar((Time) meuPai.getData());
					}
					partida = partidaTimePlacar.getPartida();
					for (int i = 0; i < partida.getPartidasTimesPlacares().size(); i++) {
						PartidaTimePlacar ptpAtual = partida.getPartidasTimesPlacares().get(i);
						if (ptpAtual.getTime() != null) {
							if (ptpAtual.getTime().equals(time)) {
								if (ptpAtual.getPlacar().getResultado() == -1) {
									placar = ptpAtual.getPlacar();
									RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').show()");
								}
							} else {
								proximaPtp = ptpAtual;
							}
						}
					}

				} else {
					for (Partida partida : chave.getPartidas()) {
						for (int i = 0; i < partida.getPartidasTimesPlacares().size(); i++) {
							PartidaTimePlacar ptpAtual = partida.getPartidasTimesPlacares().get(i);
							if (ptpAtual.getTime() != null) {
								if (ptpAtual.getTime().equals(time)) {
									if (ptpAtual.getPlacar().getResultado() == -1) {
										if (pegarPartidaTimePlacar(i + 1, ptpAtual, partida)) {
										} else {
											pegarPartidaTimePlacar(i - 1, ptpAtual, partida);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean pegarPartidaTimePlacar(int i, PartidaTimePlacar ptpAtual, Partida partida2) {
		try {
			proximaPtp = partida2.getPartidasTimesPlacares().get(i);
			if (proximaPtp.getTime() != null) {
				this.partida = partida2;
				this.partidaTimePlacar = ptpAtual;
				this.placar = partidaTimePlacar.getPlacar();
				RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').show()");
				return true;
			} else {
				return false;
			}
		} catch (IndexOutOfBoundsException ex2) {
			return false;
		}
	}

	public void salvarPartidaPlacar() {
		salvarPartida();
		if (placar.getResultado() != -1) {
			placarDao.update(placar);
			if (placar.getResultado() != 0) {
				if (placar.getResultado() > proximaPtp.getPlacar().getResultado()) {
					if (meuPai.getType().equals(TIPO_CARREGADOR)) {
						partidaTimePlacar = partidaTimePlacarDao.findById(((Time) (meuPai.getData())).getId());
					} else {
						partidaTimePlacar = procurarPartidaTimePlacar((Time) meuPai.getData());
					}
					partidaTimePlacar.setTime((Time) selectedNode.getData());
					partidaTimePlacarDao.update(partidaTimePlacar);
					chave = chaveDao.findById(chave.getId());
					iniciarTreeNode();
				}
			}
		}
		RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').hide()");
	}

	private PartidaTimePlacar procurarPartidaTimePlacar(Time time) {
		for (Partida partida : chave.getPartidas()) {
			for (int i = 0; i < partida.getPartidasTimesPlacares().size(); i++) {
				PartidaTimePlacar ptpAtual = partida.getPartidasTimesPlacares().get(i);
				if (ptpAtual.getTime() != null) {
					if (ptpAtual.getTime().equals(time)) {
						if (ptpAtual.getPlacar().getResultado() == -1) {
							return ptpAtual;
						}
					}
				}
			}
		}
		return null;
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

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
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

	public String getTIPO_CARREGADOR() {
		return TIPO_CARREGADOR;
	}

	public String getTIPO_TIME() {
		return TIPO_TIME;
	}

	public PartidaTimePlacar getProximaPtp() {
		return proximaPtp;
	}

	public void setProximaPtp(PartidaTimePlacar proximaPtp) {
		this.proximaPtp = proximaPtp;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
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

}
