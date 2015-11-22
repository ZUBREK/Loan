package ifpr.competicao.grupoChaves.chave.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ifpr.competicao.grupoChaves.GrupoChaves;
import ifpr.competicao.grupoChaves.TipoChaveamento;
import ifpr.competicao.grupoChaves.chave.Chave;
import ifpr.competicao.grupoChaves.chave.TipoCompeticao;
import ifpr.competicao.grupoChaves.chave.dao.ChaveDao;
import ifpr.competicao.grupoChaves.dao.GrupoChavesDao;
import ifpr.competicao.grupoChaves.model.GrupoChavesLazyDataModel;
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

	private GrupoChaves grupoChaves;

	private List<Chave> chaveList;

	@ManagedProperty(value = "#{grupoChavesDao}")
	private GrupoChavesDao grupoChavesDao;

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

	@ManagedProperty(value = "#{grupoChavesLazyDataModel}")
	private GrupoChavesLazyDataModel grupoChavesLazyDataModel;

	@ManagedProperty(value = "#{localDao}")
	private LocalDao localDao;

	private TipoChaveamento tipo;

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
	private List<Time> timesModalidade;

	TipoChaveamento[] tipos;
	private boolean isTipoGrupos;
	private List<Integer> qtdGruposPossiveis;
	private Integer qtdGruposEscolhida;

	private Chave chaveSelected;

	public ChaveMB() {
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
		if (grupoChaves.getTipo().equals(TipoChaveamento.GRUPOS)) {
		} else {
			chave = grupoChaves.getChaves().get(0);
		}
		if (chave != null) {
			rootNode = new DefaultTreeNode(chave.getNome(), null);
			rootNode.setExpanded(true);
			nodes.add(rootNode);
			pegarTimes(chave);
			if (chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {
				chave.getPartidas().get(0);
			} else if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS)) {
				gerarNodeTipoPtosCorridos();
			} else if (chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
				gerarNodeTipoMataMata();
			}
		}
	}

	private void gerarNodeTipoPtosCorridos() {
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
		Collections.reverse(partidaTimes);
		int i2 = 0;
		for (int i = 0; i < partidaTimes.size(); i++) {
			adicionarNodeParentTipo(partidaTimes.get(i), nodes.get(i2));
			if (i % 2 == 0) {
				i2++;
			}
		}
	}

	public void pegarTimes(Chave chave) {
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
		setarListaTimes(chave);
	}

	private void setarListaTimes(Chave chave) {
		for (int i = 0; i < partidaTimes.size(); i++) {
			Time time = partidaTimes.get(i).getTime();
			if (time != null && !timesSemNullSemDuplicado.contains(time) && !time.getNome().equals("EMPATE")) {
				timesSemNullSemDuplicado.add(time);
			}
		}
		Collections.sort(timesSemNullSemDuplicado, new Comparator<Time>() {
			@Override
			public int compare(Time time1, Time time2) {
				return time1.getPontosTime().getPontos() - time2.getPontosTime().getPontos();
			}
		});
		Collections.reverse(timesSemNullSemDuplicado);
		if (!chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {
			for (int i = 0; i < timesSemNullSemDuplicado.size(); i++) {
				Time time = timesSemNullSemDuplicado.get(i);
				time.getPontosTime().setClassificacao(i + 1);
				try {
					Time timeAnterior = timesSemNullSemDuplicado.get(i - 1);
					if (time.getPontosTime().getPontos() == timeAnterior.getPontosTime().getPontos()) {
						time.getPontosTime().setClassificacao(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				timeDao.update(time);
			}
		}
		Collections.sort(timesSemNullSemDuplicado, new Comparator<Time>() {
			@Override
			public int compare(Time time1, Time time2) {
				return time1.getPontosTime().getClassificacao() - time2.getPontosTime().getClassificacao();
			}
		});

	}

	public void visualizarPartidasTime() {
		timeSelectOne = timeDataTable;
		iniciarTreeNode();

		RequestContext.getCurrentInstance().execute("PF('tabChave').select(1)");
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
		grupoChaves = new GrupoChaves();
	}

	public void remover() {
		try {
			if (grupoChaves.getTipo().equals(TipoChaveamento.GRUPOS)) {
				for (Chave chave : grupoChaves.getChaves()) {
					pegarTimes(chave);
					for (Time time : timesSemNullSemDuplicado) {
						PontosTime pt = time.getPontosTime();
						pt.setDerrotas(0);
						pt.setEmpates(0);
						pt.setPontos(0);
						pt.setVitorias(0);
						pt.setClassificacao(0);
						pontosTimeDao.update(pt);
					}
				}
			} else {
				chave = grupoChaves.getChaves().get(0);
				pegarTimes(chave);
				for (Time time : timesSemNullSemDuplicado) {
					PontosTime pt = time.getPontosTime();
					pt.setDerrotas(0);
					pt.setEmpates(0);
					pt.setPontos(0);
					pt.setVitorias(0);
					pt.setClassificacao(0);
					pontosTimeDao.update(pt);
				}
			}
			grupoChavesDao.remover(grupoChaves);
		} catch (ConstraintViolationException e) {
			// facesmessage bagaça
		}

	}

	public void mensagemFaces(String titulo, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, message));
	}

	public void cancelar() {
		timeDataTable = null;
	}

	public void cancelarSalvarPartidaTime() {
		partidaTime = null;
		timeSelectOne = null;
		RequestContext.getCurrentInstance().execute("PF('confirmPartidaDialog').hide()");
		RequestContext.getCurrentInstance().execute("PF('partidaChavDialog').hide()");
		iniciarTreeNode();
	}

	public void salvarGrupoChave() {
		grupoChaves.setModalidade(modalidade);
		grupoChaves.setTipo(tipo);

		if (grupoChaves.getTipo().equals(TipoChaveamento.GRUPOS)) {
			gerarPartidasTipoGrupos();

		} else {
			chave = new Chave();
			if (grupoChaves.getTipo().equals(TipoChaveamento.CLASSIFICATORIO)) {
				chave.setTipo(TipoCompeticao.CLASSIFICATORIO);
				gerarPartidaTipoClassificatorio();
			} else if (grupoChaves.getTipo().equals(TipoChaveamento.PONTOS_CORRIDOS)) {
				chave.setTipo(TipoCompeticao.PONTOS_CORRIDOS);
				gerarPartidasTipoPtosCorridos(timesModalidade);
			} else if (grupoChaves.getTipo().equals(TipoChaveamento.MATA_MATA)) {
				chave.setTipo(TipoCompeticao.MATA_MATA);
				gerarPartidasTipoMataMata();
			}
			chave.setNome(grupoChaves.getNome());
			chaveDao.salvar(chave);
			adicionarEmGrupoChaves();
			grupoChavesDao.salvar(grupoChaves);
		}
		timesModalidade = null;
		modalidade = null;
		tipo = null;
	}

	private void adicionarEmGrupoChaves() {
		grupoChaves.getChaves().add(chave);

	}

	private void gerarPartidasTipoGrupos() {
		randomizarTimes();
		qtdTimes = timesModalidade.size();
		int i2 = 0;
		for (int i = 0; i < qtdGruposEscolhida; i++) {
			chave = new Chave();
			chave.setTipo(TipoCompeticao.PONTOS_CORRIDOS);
			chave.setNome("Grupo " + (i + 1));
			List<Time> listaTimesGrupo = new ArrayList<>();
			for (int j = 0; j < qtdTimes / qtdGruposEscolhida; j++) {
				listaTimesGrupo.add(timesModalidade.get(i2));
				i2++;
			}
			gerarPartidasTipoPtosCorridos(listaTimesGrupo);
			adicionarEmGrupoChaves();
			chaveDao.salvar(chave);
		}
		chave = new Chave();
		chave.setTipo(TipoCompeticao.MATA_MATA);
		chave.setNome("Fase de Mata-Mata");
		criarPartidasRestantes(qtdGruposEscolhida);
		adicionarEmGrupoChaves();
		chaveDao.salvar(chave);
		grupoChavesDao.salvar(grupoChaves);
	}

	private void gerarPartidaTipoClassificatorio() {
		qtdTimes = timesModalidade.size();
		partida = new Partida();
		chave.getPartidas().add(partida);
		salvarPartida();
		for (int i = 0; i < qtdTimes; i++) {
			criarSalvarPlacarPartida(i, timesModalidade);
		}
	}

	private void gerarPartidasTipoPtosCorridos(List<Time> listaTimes) {
		int qtdTimes = listaTimes.size();
		for (int i = 0; i < qtdTimes; i++) {
			for (int i2 = qtdTimes - 1; i2 > i; i2--) {
				partida = new Partida();
				chave.getPartidas().add(partida);
				salvarPartida();
				criarSalvarPlacarPartida(-1, listaTimes);
				criarSalvarPlacarPartida(i, listaTimes);
				criarSalvarPlacarPartida(i2, listaTimes);
			}
		}
	}

	private void criarSalvarPlacarPartida(int i, List<Time> listaTimes) {
		setarPartidaTimePlacar();
		if (i != -1) {
			partidaTime.setTime(listaTimes.get(i));
		}
		partidaTimeDao.salvar(partidaTime);
		partida.getPartidasTimesPlacares().add(partidaTime);
		salvarPartida();
	}

	public void gerarPartidasTipoMataMata() {
		randomizarTimes();
		gerarPartidasPrimeiraFaseMataMata();
		criarPartidasRestantes(qtdTimes);

	}

	private void criarPartidasRestantes(int qtdTimes) {
		while (qtdTimes >= 1) {
			for (int i = 0; i < qtdTimes; i++) {
				if (i % 2 == 0) {
					partida = new Partida();
					chave.getPartidas().add(partida);
					salvarPartida();
				}
				criarSalvarPlacarPartida(-1, timesModalidade);
			}
			qtdTimes /= 2;
		}
	}

	private void gerarPartidasPrimeiraFaseMataMata() {
		qtdTimes = timesModalidade.size();
		for (int i = 0; i < qtdTimes; i++) {
			if (i % 2 == 0) {
				partida = new Partida();
				chave.getPartidas().add(partida);
				salvarPartida();
			}
			criarSalvarPlacarPartida(i, timesModalidade);
		}
		qtdTimes = timesModalidade.size() / 2;
	}

	public void randomizarTimes() {
		long seed = System.nanoTime();
		Collections.shuffle(timesModalidade, new Random(seed));
	}

	public void salvarPartida() {
		if (partida.getId() == null) {
			partidaDao.salvar(partida);
		} else {
			partida.setLocal(local);
			partidaDao.update(partida);
		}
	}

	public void salvarPartidaTipoClassificatorio() {
		salvarPartida();
		pontosTimeDao.update(timeDataTable.getPontosTime());
		pegarTimes(chave);
		RequestContext.getCurrentInstance().execute("PF('classificatorioDialog').hide()");
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
		salvarPartida();
		if (partidaTime.getPlacar() != -1) {

			PontosTime pontosTimeAdversario = ptpAdversario.getTime().getPontosTime();
			Time timeAdversario = ptpAdversario.getTime();
			pontosTime = partidaTime.getTime().getPontosTime();
			if (partidaTime.getPlacar() > ptpAdversario.getPlacar() && ptpAdversario.getPlacar() != -1) {
				partidaTimeDao.update(partidaTime);	
				setarTimeVitoriosoPtp(partidaTime.getTime());
				setarPlacarTime(pontosTimeAdversario, 0);
				setarPlacarTime(pontosTime, 3);
				terminarPartida();
			} else if (partidaTime.getPlacar() < ptpAdversario.getPlacar()) {
				partidaTimeDao.update(partidaTime);
				setarTimeVitoriosoPtp(timeAdversario);
				setarPlacarTime(pontosTime, 0);
				setarPlacarTime(pontosTimeAdversario, 3);
				terminarPartida();
			} else if (partidaTime.getPlacar() == ptpAdversario.getPlacar()
					&& !chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
				partidaTimeDao.update(partidaTime);	
				setarPlacarTime(pontosTime, 0);
				setarPlacarTime(pontosTimeAdversario, 0);
				partidaTimeMeuPai.setPlacar(-2);
				partidaTimeDao.update(partidaTimeMeuPai);
				terminarPartida();
			}
			else if(chave.getTipo().equals(TipoCompeticao.MATA_MATA)){
				mensagemFaces("PLACAR INVÁLIDO!", "Não pode haver empate no tipo MATA-MATA!");
			}
			chave = chaveDao.findById(chave.getId());
			iniciarTreeNode();
			if (grupoChaves.getTipo().equals(TipoChaveamento.GRUPOS)) {
				if (checkTodasAcabadas()) {
					terminarChave();
				}
			}
		}
		RequestContext.getCurrentInstance().execute("PF('confirmPartidaDialog').hide()");
	}

	private void terminarChave() {
		chave.setAcabado(true);
		chaveDao.update(chave);
		Time timeVencedor = timesSemNullSemDuplicado.get(0);
		for (Chave chave : grupoChaves.getChaves()) {
			if(chave.getTipo().equals(TipoCompeticao.MATA_MATA)){
				for (Partida partida : chave.getPartidas()) {
					for (PartidaTime partidaTime : partida.getPartidasTimesPlacares()) {
						if(partidaTime.getTime() == null){
							partidaTime.setTime(timeVencedor);
							partidaTimeDao.update(partidaTime);
							return;
						}
					}
				}
			}
		}
	}

	private boolean checkTodasAcabadas() {
		for (Partida partida : chave.getPartidas()) {
			if (!partida.isAcabado()) {
				return false;
			}
		}
		return true;
	}

	private void terminarPartida() {
		partida.setAcabado(true);
		salvarPartida();
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
		timesModalidade = null;
		modalidade = null;
		tipo = null;
	}

	public boolean isPossivelMataMata() {
		float qtdTimes = timesModalidade.size();
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

	public PartidaDao getPartidaDao() {
		return partidaDao;
	}

	public void setPartidaDao(PartidaDao partidaDao) {
		this.partidaDao = partidaDao;
	}

	public TipoChaveamento[] getTipos() {
		return tipos;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
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

	public void modalidadeSelectionChanged(final AjaxBehaviorEvent event) throws ValidationException {
		tipos = TipoChaveamento.values();
		if (!grupoChavesDao.hasChave(modalidade)) {
			timesModalidade = timeDao.pesquisarPorModalidade(modalidade);
			if (timesModalidade.size() < 2) {
				mensagemFaces("Erro na Quantidade de Times!", "Quantidade de times insuficiente!");
				ArrayList<TipoChaveamento> tiposOld = new ArrayList<TipoChaveamento>(Arrays.asList(tipos));
				tiposOld.clear();
				TipoChaveamento[] tiposNew = tiposOld.toArray(new TipoChaveamento[tiposOld.size()]);
				tipos = tiposNew;
			} else {

				if (!isPossivelMataMata()) {
					ArrayList<TipoChaveamento> tiposOld = new ArrayList<TipoChaveamento>(Arrays.asList(tipos));
					tiposOld.remove(TipoChaveamento.MATA_MATA);
					TipoChaveamento[] tiposNew = tiposOld.toArray(new TipoChaveamento[tiposOld.size()]);
					tipos = tiposNew;
				}
				if (!isPossivelGrupos()) {
					ArrayList<TipoChaveamento> tiposOld = new ArrayList<TipoChaveamento>(Arrays.asList(tipos));
					tiposOld.remove(TipoChaveamento.GRUPOS);
					TipoChaveamento[] tiposNew = tiposOld.toArray(new TipoChaveamento[tiposOld.size()]);
					tipos = tiposNew;
				}
			}
		} else {
			mensagemFaces("Erro!", "Já existe uma chave para a modalidade selecionada!");
			ArrayList<TipoChaveamento> tiposOld = new ArrayList<TipoChaveamento>(Arrays.asList(tipos));
			tiposOld.clear();
			TipoChaveamento[] tiposNew = tiposOld.toArray(new TipoChaveamento[tiposOld.size()]);
			tipos = tiposNew;
		}
	}

	public boolean isPossivelGrupos() {
		qtdGruposPossiveis = new ArrayList<>();
		boolean isPossivel = false;
		for (int i = 0; i < timesModalidade.size(); i++) {
			if (i % 2 == 0 && i != 0) {
				if (timesModalidade.size() % i == 0 && timesModalidade.size() / i > 2) {
					qtdGruposPossiveis.add(i);
					isPossivel = true;
				}
			}
		}
		return isPossivel;
	}

	public void tipoSelectionChanged(final AjaxBehaviorEvent event) {
		if (tipo.equals(TipoChaveamento.GRUPOS)) {
			isTipoGrupos = true;
		} else {
			isTipoGrupos = false;
		}

	}

	public void chaveSelectionChanged(final AjaxBehaviorEvent event) {
		chave = chaveSelected;
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

	public List<Time> getTimesModalidade() {
		return timesModalidade;
	}

	public void setTimesModalidade(List<Time> timesModalidade) {
		this.timesModalidade = timesModalidade;
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

	public void setTipos(TipoChaveamento[] tipos) {
		this.tipos = tipos;
	}

	public TipoCompeticao getTipoClassificatorio() {
		return TipoCompeticao.CLASSIFICATORIO;
	}

	public boolean getIsNotMataMata() {
		if (chave != null) {
			if (chave.getTipo().equals(TipoCompeticao.MATA_MATA)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public boolean getIsClassificatorio() {
		if (chave != null) {
			if (chave.getTipo().equals(TipoCompeticao.CLASSIFICATORIO)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean getIsTipoGrupos() {
		return isTipoGrupos;
	}

	public void setTipoGrupos(boolean isTipoGrupos) {
		this.isTipoGrupos = isTipoGrupos;
	}

	public List<Integer> getQtdGruposPossiveis() {
		return qtdGruposPossiveis;
	}

	public void setQtdGruposPossiveis(List<Integer> qtdGruposPossiveis) {
		this.qtdGruposPossiveis = qtdGruposPossiveis;
	}

	public Integer getQtdGruposEscolhida() {
		return qtdGruposEscolhida;
	}

	public void setQtdGruposEscolhida(Integer qtdGruposEscolhida) {
		this.qtdGruposEscolhida = qtdGruposEscolhida;
	}

	public GrupoChaves getGrupoChaves() {
		return grupoChaves;
	}

	public void setGrupoChaves(GrupoChaves grupoChaves) {
		this.grupoChaves = grupoChaves;
	}

	public GrupoChavesDao getGrupoChavesDao() {
		return grupoChavesDao;
	}

	public void setGrupoChavesDao(GrupoChavesDao grupoChavesDao) {
		this.grupoChavesDao = grupoChavesDao;
	}

	public GrupoChavesLazyDataModel getGrupoChavesLazyDataModel() {
		return grupoChavesLazyDataModel;
	}

	public void setGrupoChavesLazyDataModel(GrupoChavesLazyDataModel grupoChavesLazyDataModel) {
		this.grupoChavesLazyDataModel = grupoChavesLazyDataModel;
	}

	public TipoChaveamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoChaveamento tipo) {
		this.tipo = tipo;
	}

	public boolean getIsGrupos() {
		try {
			if (!grupoChaves.getTipo().equals(TipoChaveamento.GRUPOS)) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	public Chave getChaveSelected() {
		return chaveSelected;
	}

	public void setChaveSelected(Chave chaveSelected) {
		this.chaveSelected = chaveSelected;
	}

}
