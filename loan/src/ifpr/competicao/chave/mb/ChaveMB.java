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
	private PartidaTimePlacarDao partidaTimeDao;

	@ManagedProperty(value = "#{chaveLazyDataModel}")
	private ChaveLazyDataModel chaveLazyDataModel;

	private TipoCompeticao tipo;

	private TreeNode rootNode;
	private List<TreeNode> nodes;

	private List<Time> times;

	private Modalidade modalidade;

	private Placar placar;

	private PartidaTimePlacar partidaTimePlacar;

	private Partida partida;

	public ChaveMB() {
	}

	@PostConstruct
	public void init() {

	}

	public void adicionarNodeParent(String nome, TreeNode nodeRoot) {
		nodes.add(new DefaultTreeNode(nome, nodeRoot));

	}

	public void iniciarTreeNode() {

		nodes = new ArrayList<TreeNode>();
		rootNode = new DefaultTreeNode(chave.getNome(), null);
		String nomePartida = "";
		for (Partida partida : chave.getPartidas()) {
			for (PartidaTimePlacar partidaTimePlacar : partida.getPartidasTimesPlacares()) {
				nomePartida += partidaTimePlacar.getTime().getNome() + " ";
			}
		}
		adicionarNodeParent(nomePartida, rootNode);
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
		if (chave.getId() == null) {
			chave.setTipo(tipo);
			long seed = System.nanoTime();
			Collections.shuffle(times, new Random(seed));
			if (chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {

			} else if (chave.getTipo().equals(TipoCompeticao.GRUPOS)) {

			} else if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {

			} else if (chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
				for (int i = 0; i < times.size(); i++) {
					if (i % 2 == 0) {
						partidaTimePlacar = new PartidaTimePlacar();
						partida = new Partida();
						salvarPartida(partida);
						placar = new Placar();
						salvarPlacar(placar);
					}
					setarSalvarPartidaTimePlacar(i);
					partida.getPartidasTimesPlacares().add(partidaTimePlacar);
					partidaDao.salvar(partida);
					chave.getPartidas().add(partida);
				}
				chaveDao.salvar(chave);
			}
		}
		iniciarTreeNode();
	}

	public void salvarPartida(Partida partida) {
		if (partida.getId() == null) {
			partidaDao.salvar(partida);
		} else {
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

	private void setarSalvarPartidaTimePlacar(int i) {
		partidaTimePlacar.setPartida(partida);
		partidaTimePlacar.setTime(times.get(i));
		partidaTimePlacar.setPlacar(placar);
		partidaTimeDao.salvar(partidaTimePlacar);

	}

	public Chave getChave() {
		return chave;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		System.out.println("Node Data ::" + event.getTreeNode().getData() + " :: Selected");
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
		if (times.size() % 2 == 0) {
			return tipos;
		} else {
			ArrayList<TipoCompeticao> tiposOld = new ArrayList<TipoCompeticao>(Arrays.asList(tipos));
			tiposOld.remove(0);
			TipoCompeticao[] tiposNew = tiposOld.toArray(new TipoCompeticao[tiposOld.size()]);
			return tiposNew;
		}
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

	public PartidaTimePlacarDao getPartidaTimeDao() {
		return partidaTimeDao;
	}

	public void setPartidaTimeDao(PartidaTimePlacarDao partidaTimeDao) {
		this.partidaTimeDao = partidaTimeDao;
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
}
