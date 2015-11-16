package ifpr.competicao.chave.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.ConstraintViolationException;

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

	private Modalidade modalidade;

	private Placar placar;

	private PartidaTimePlacar partidaTimePlacar;

	private Partida partida;

	private int qtdTimes;

	private List<Local> listaLocais;

	private Local local;
	
	private TreeNode selectedNode;

	public ChaveMB() {
		chave = new Chave();
		chave.setPrimeiraFase(true);
	}

	@PostConstruct
	public void init() {
		listaLocais = localDao.listAsc();
		listaLocais.get(0);
	}

	public void adicionarNodeParent(Object nome, TreeNode nodeRoot) {
		TreeNode node = new DefaultTreeNode(nome, nodeRoot);
		node.setExpanded(true);
		nodes.add(node);

	}

	public void iniciarTreeNode() {
		nodes = new ArrayList<>();
		pegarTimes();
		qtdTimes = times.size();

		rootNode = new DefaultTreeNode(chave.getNome(), null);
		rootNode.setExpanded(true);
		nodes.add(rootNode);

		int i2 = 0;
		Collections.reverse(times);
		for (int i = 0; i < times.size(); i++) {
			adicionarNodeParent(times.get(i), nodes.get(i2));
			if (i % 2 == 0) {
				i2++;
			}
		}
	}

	public void pegarTimes() {
		times = new ArrayList<>();
		for (Partida partida : chave.getPartidas()) {
			for (PartidaTimePlacar partidaTimePlacar : partida.getPartidasTimesPlacares()) {
				Time time = partidaTimePlacar.getTime();
				if (time == null) {
					time = new Time();
					time.setNome("");
				}
				times.add(time);
			}
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
		chave = null;
	}

	public void salvarChave() {
		chave.setTipo(tipo);
		if (chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {

		} else if (chave.getTipo().equals(TipoCompeticao.GRUPOS)) {

		} else if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {

		} else if (chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
			gerarPartidasTipoMataMata();
			chaveDao.salvar(chave);
		}
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
				placar = new Placar();
				placar.setResultado(0);
				salvarPlacar(placar);
				setarPartidaTimePlacar();
				partidaTimePlacarDao.salvar(partidaTimePlacar);
				partida.getPartidasTimesPlacares().add(partidaTimePlacar);
				salvarPartida();
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
			placar = new Placar();
			placar.setResultado(0);
			salvarPlacar(placar);
			setarPartidaTimePlacar();
			partidaTimePlacar.setTime(times.get(i));
			partidaTimePlacarDao.salvar(partidaTimePlacar);
			partida.getPartidasTimesPlacares().add(partidaTimePlacar);
			salvarPartida();
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
		Time time = (Time) event.getTreeNode().getData();
		for (Partida partida : chave.getPartidas()) {
			for (int i = 0; i < partida.getPartidasTimesPlacares().size(); i++) {

				PartidaTimePlacar ptpAtual = partida.getPartidasTimesPlacares().get(i);
				if (ptpAtual.getTime() != null) {
					if (ptpAtual.getTime().equals(time)) {
						try {
							PartidaTimePlacar proximaPtp = partida.getPartidasTimesPlacares().get(i + 1);
							if (proximaPtp.getTime() != null) {
								this.partida = partida;
							}
						} catch (IndexOutOfBoundsException ex) {
							try {
								PartidaTimePlacar ptpAnterior = partida.getPartidasTimesPlacares().get(i - 1);
								if (ptpAnterior.getTime() != null) {
									this.partida = partida;
								}
							} catch (IndexOutOfBoundsException ex2) {
								ex2.printStackTrace();
							}
						}

					}
				}
			}
		}
		event.getTreeNode().setSelected(false);
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

}
