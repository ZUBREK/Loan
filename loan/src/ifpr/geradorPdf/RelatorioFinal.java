package ifpr.geradorPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
import ifpr.competicao.grupoChaves.GrupoChaves;
import ifpr.competicao.grupoChaves.TipoChaveamento;
import ifpr.competicao.grupoChaves.chave.Chave;
import ifpr.competicao.grupoChaves.chave.TipoCompeticao;
import ifpr.competicao.grupoChaves.chave.dao.ChaveDao;
import ifpr.competicao.grupoChaves.dao.GrupoChavesDao;
import ifpr.competicao.partida.Partida;
import ifpr.competicao.partidaTime.PartidaTime;
import ifpr.competicao.time.Time;
import ifpr.competicao.time.dao.TimeDao;
import ifpr.delegacao.Delegacao;
import ifpr.delegacao.dao.DelegacaoDao;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.estudante.Estudante;
import ifpr.utils.Paths;

@ManagedBean(name = "relatorioFinal")
@ViewScoped
public class RelatorioFinal {
	private Document doc;

	private Font heveltica12Bold = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
	private Font heveltica10 = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);
	private final int TAMANHO_TABELA_PDF = 5;
	private PdfPTable table = new PdfPTable(TAMANHO_TABELA_PDF);
	private File arquivo;
	private StreamedContent arqStreamed;
	private Image header;

	@ManagedProperty(value = "#{campusDao}")
	private CampusDao campusDao;
	private List<Campus> listaCampus;

	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;
	private List<Modalidade> listaModalidade;

	@ManagedProperty(value = "#{timeDao}")
	private TimeDao timeDao;
	private List<Time> listaTime;

	@ManagedProperty(value = "#{delegacaoDao}")
	private DelegacaoDao delegacaoDao;
	private List<Delegacao> listaDelegacao;

	@ManagedProperty(value = "#{chaveDao}")
	private ChaveDao chaveDao;
	private List<Chave> listaChave;
	private List<Partida> listaPartida;
	private List<PartidaTime> listaPartidaTime;

	@ManagedProperty(value = "#{grupoChavesDao}")
	private GrupoChavesDao grupoDao;
	private List<GrupoChaves> listaGrupoChaves;

	private Modalidade modalidade;
	private Campus campus;
	private Time time;
	private Delegacao delegacao;
	private Chave chave;
	private Partida partida;
	private GrupoChaves grupoChaves;

	public RelatorioFinal() throws BadElementException, MalformedURLException, IOException {
		doc = new Document();
		arquivo = new File(Paths.PASTA_RELATORIO);
		arquivo.mkdirs();
		header = Image.getInstance(Paths.LOGO_JOGOS);
	}

	public void gerarRelatorio() {
		File caminho = new File(Paths.CAMINHO_RELATORIO);
		if (caminho.exists()) {
			caminho.delete();
		}
		try {
			doc = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(caminho));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();
			addHeader();
			doc.setMargins(50, 50, 70, 70);
			addCampus();
			addModalidades();
			addTimes();
			addDelegacoes();
			addChaves();
			doc.add(table);
			doc.close();
			writer.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private void addChaves() throws DocumentException {
		listaGrupoChaves = grupoDao.listAsc();
		String titulo = "CHAVES";
		addTitulo(titulo);
		for (int l = 0; l < listaGrupoChaves.size(); l++) {
			grupoChaves = listaGrupoChaves.get(l);
			addLinha(grupoChaves.getNome() + "/" + grupoChaves.getModalidade().getNome(), true);
			addLinha("Tipo: " + grupoChaves.getTipo().getLabel(), true);
			if (grupoChaves.getTipo().equals(TipoChaveamento.MATA_MATA)) {
				addChaveMataMata(grupoChaves);
			} else if (grupoChaves.getTipo().equals(TipoChaveamento.PONTOS_CORRIDOS)) {
				addChavePontosCorridos(grupoChaves);
			} else if (grupoChaves.getTipo().equals(TipoChaveamento.CLASSIFICATORIO)) {
				addChaveClassificatorio(grupoChaves);
			} else if (grupoChaves.getTipo().equals(TipoChaveamento.GRUPOS)) {
				addChaveGrupos(grupoChaves);
			}
		}
		separadorTabela();
	}

	private void addChaveGrupos(GrupoChaves grupoChaves2) {
		listaChave = grupoChaves2.getChaves();
		for (int i = 0; i < listaChave.size(); i++) {
			chave = listaChave.get(i);
			listaTime = timeDao.pesquisarPorModalidade(grupoChaves2.getModalidade());
			if (chave.getTipo().equals(TipoCompeticao.PONTOS_CORRIDOS) && chave.isAcabado() == true) {
				addClassificacao(listaTime);
				listaPartida = chave.getPartidas();
				addPartidas(listaPartida);
			} else if(chave.getTipo().equals(TipoCompeticao.MATA_MATA)){
				addVencedores2(listaTime);
				listaPartida = chave.getPartidas();
				addPartidas(listaPartida);
			}
		}
	}

	private void addChaveClassificatorio(GrupoChaves grupoChaves2) {
		listaChave = grupoChaves2.getChaves();
		for (int i = 0; i < listaChave.size(); i++) {
			chave = listaChave.get(i);
			listaTime = timeDao.pesquisarPorModalidade(grupoChaves2.getModalidade());
			addVencedores(listaTime);
			addClassificatorio(listaTime);
		}
	}

	private void addChaveMataMata(GrupoChaves grupoChaves) {
		listaChave = grupoChaves.getChaves();
		for (int i = 0; i < listaChave.size(); i++) {
			chave = listaChave.get(i);
			if (chave.isAcabado() == true) {
				listaTime = timeDao.pesquisarPorModalidade(grupoChaves.getModalidade());
				addVencedores2(listaTime);
				listaPartida = chave.getPartidas();
				addPartidas(listaPartida);
			}
		}
	}

	private void addChavePontosCorridos(GrupoChaves grupoChaves) {
		listaChave = grupoChaves.getChaves();
		for (int i = 0; i < listaChave.size(); i++) {
			chave = listaChave.get(i);
			listaTime = timeDao.pesquisarPorModalidade(grupoChaves.getModalidade());
			addVencedores(listaTime);
			addClassificacao(listaTime);
			listaPartida = chave.getPartidas();
			addPartidas(listaPartida);
		}
	}

	private void addClassificacao(List<Time> listaTime2) {
		Collections.sort(listaTime2, new Comparator<Time>() {
			@Override
			public int compare(Time t1, Time t2) {
				return t1.getPontosTime().getClassificacao() - t2.getPontosTime().getClassificacao();
			}
		});
		addTitulo("CLASSIFICAÇÃO FINAL");
		addCelula("POSIÇÃO", true);
		add3Linha("TIME");
		addCelula("PONTOS", true);
		for (int i = 0; i < listaTime2.size(); i++) {
			time = listaTime2.get(i);
			addCelula(String.valueOf(time.getPontosTime().getClassificacao()), true);
			add3Linha(time.getNome());
			addCelula(String.valueOf(time.getPontosTime().getPontos()), false);
		}
	}

	private void addClassificatorio(List<Time> listaTime2) {
		Collections.sort(listaTime2, new Comparator<Time>() {
			@Override
			public int compare(Time t1, Time t2) {
				return t1.getPontosTime().getClassificacao() - t2.getPontosTime().getClassificacao();
			}
		});
		addTitulo("CLASSIFICAÇÃO FINAL");
		addCelula("POSIÇÃO", true);
		add4Linha("TIME");
		for (int i = 0; i < listaTime2.size(); i++) {
			time = listaTime2.get(i);
			addCelula(String.valueOf(time.getPontosTime().getClassificacao()), true);
			add4Linha(time.getNome());
		}
	}

	private void add3Linha(String texto1) {
		Paragraph textoP = new Paragraph(texto1, heveltica10);
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(3);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void add4Linha(String texto1) {
		Paragraph textoP = new Paragraph(texto1, heveltica10);
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(4);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void addVencedores2(List<Time> listaTime) {
		Time timeVencedor = null;
		Time timeSegundo = null;
		List<Time> timeTerceiro = new ArrayList<>();
		for (int j = 0; j < listaTime.size(); j++) {
			time = listaTime.get(j);
			if (time.getPontosTime().getClassificacao() == 1) {
				timeVencedor = time;
			} else if (time.getPontosTime().getClassificacao() == 2) {
				timeSegundo = time;
			} else if (time.getPontosTime().getClassificacao() == 3) {
				timeTerceiro.add(time);
			}
		}
		addLinha("VENCEDOR: " + timeVencedor.getNome(), false);
		addLinha("2° LUGAR: " + timeSegundo.getNome(), false);
		addLinha("3° LUGAR: " + timeTerceiro.get(0).getNome(), false);
		addLinha("3° LUGAR: " + timeTerceiro.get(1).getNome(), false);
	}

	@SuppressWarnings("null")
	private void addVencedores(List<Time> listaTime) {
		Time timeVencedor = null;
		Time timeSegundo = null;
		Time timeTerceiro = null;
		for (int j = 0; j < listaTime.size(); j++) {
			time = listaTime.get(j);
			if (time.getPontosTime().getClassificacao() == 1) {
				timeVencedor = time;
			} else if (time.getPontosTime().getClassificacao() == 2) {
				timeSegundo = time;
			} else if (time.getPontosTime().getClassificacao() == 3) {
				timeTerceiro = time;
			}
		}
		if(timeVencedor != null){
			addLinha("VENCEDOR: " + timeVencedor.getNome(), false);
		}
		if(timeSegundo != null){
			addLinha("2° LUGAR: " + timeSegundo.getNome(), false);
		}
		if(timeTerceiro != null){
			addLinha("3° LUGAR: " + timeTerceiro.getNome(), false);
		}
	}

	private void addPartidas(List<Partida> listaPartida) {
		String titulo = "PARTIDAS:";
		addTitulo(titulo);
		Collections.sort(listaPartida, new Comparator<Partida>() {
			@Override
			public int compare(Partida p1, Partida p2) {
				return p1.getId() - p2.getId();
			}
		});
		for (int k = 0; k < listaPartida.size(); k++) {
			partida = listaPartida.get(k);
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			if (partida.isAcabado() == true) {
				addLinha(partida.getLocal().getNome() + " - " + dt.format(partida.getDataHora()).toString(), false);
				listaPartidaTime = partida.getPartidasTimesPlacares();
				int lado = 0;
				for (PartidaTime partidaTime : listaPartidaTime) {
					if (partidaTime.getTime() != null && partidaTime.getPlacar() >= 0) {
						if (lado == 0) {
							addCelula(partidaTime.getTime().getNome(), true);
							addCelula(String.valueOf(partidaTime.getPlacar()), false);
							addCelula("X", false);
							lado++;
						} else {
							addCelula(String.valueOf(partidaTime.getPlacar()), false);
							addCelula(partidaTime.getTime().getNome(), true);
						}
					}
				}
			}
		}
	}

	private void addDelegacoes() throws DocumentException {
		listaDelegacao = delegacaoDao.listDesc();
		String titulo = "DELEGAÇÕES";
		addTitulo(titulo);
		for (int i = 0; i < listaDelegacao.size(); i++) {
			delegacao = listaDelegacao.get(i);
			addLinha(delegacao.getNome() + "/" + delegacao.getCampus().getCidade(), true);
			add2Titulo("NOME:", "FUNÇÃO:");
			List<Pessoa> listaPessoa = delegacaoDao.listarPessoas(delegacao);
			Pessoa pessoa = new Pessoa();
			for (int j = 0; j < listaPessoa.size(); j++) {
				pessoa = listaPessoa.get(j);
				add2Linha(pessoa.getNome(), pessoa.getTipo().getLabel());
			}
		}
		separadorTabela();
	}

	private void addCampus() throws DocumentException {
		listaCampus = campusDao.listarAlfabetica();
		String titulo = "CÂMPUS";
		addTitulo(titulo);
		for (int i = 0; i < listaCampus.size(); i++) {
			campus = listaCampus.get(i);
			addLinha(campus.getCidade(), false);
		}
		separadorTabela();
	}

	private void separadorTabela() throws DocumentException {
		table.setWidthPercentage(90f);
		doc.add(table);
		table = new PdfPTable(TAMANHO_TABELA_PDF);
		doc.add(new Phrase(" "));
	}

	private void addModalidades() throws DocumentException {
		listaModalidade = modalidadeDao.listarAlfabetica();
		String titulo = "MODALIDADES";
		addTitulo(titulo);
		for (int i = 0; i < listaModalidade.size(); i++) {
			modalidade = listaModalidade.get(i);
			addLinha(modalidade.getNome(), false);
		}
		separadorTabela();
	}

	private void addTimes() throws DocumentException {
		listaTime = timeDao.listDesc();
		String titulo = "TIMES/CÂMPUS";
		addTitulo(titulo);
		for (int i = 0; i < listaTime.size(); i++) {
			time = listaTime.get(i);
			addLinha(time.getNome() + "/" + time.getCampus().getCidade(), true);
			add2Titulo("NOME:", "FUNÇÃO:");
			add2Linha(time.getTecnico().getNome(), time.getTecnico().getTipo().getLabel());
			List<Estudante> listaEstudante = timeDao.listarEstudantes(time);
			Estudante estudante = new Estudante();
			for (int j = 0; j < listaEstudante.size(); j++) {
				estudante = listaEstudante.get(j);
				add2Linha(estudante.getNome(), estudante.getTipo().getLabel());
			}
		}
		separadorTabela();
	}

	private void add2Linha(String texto1, String texto2) {
		Paragraph textoP = new Paragraph(texto1, heveltica10);
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(3);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
		textoP = new Paragraph(texto2, heveltica10);
		textoP.setAlignment(Element.ALIGN_CENTER);
		celula = new PdfPCell();
		celula.setColspan(2);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void add2Titulo(String titulo, String titulo2) {
		Paragraph textoP = new Paragraph(titulo, heveltica12Bold);
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(3);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
		textoP = new Paragraph(titulo2, heveltica12Bold);
		textoP.setAlignment(Element.ALIGN_CENTER);
		celula = new PdfPCell();
		celula.setColspan(2);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);

	}

	private void addLinha(String texto, boolean bold) {
		Paragraph textoP;
		if (bold == true) {
			textoP = new Paragraph(texto, heveltica12Bold);
		} else {
			textoP = new Paragraph(texto, heveltica10);
		}
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(TAMANHO_TABELA_PDF);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void addCelula(String texto, boolean bold) {
		Paragraph textoP;
		if (bold == true) {
			textoP = new Paragraph(texto, heveltica12Bold);
		} else {
			textoP = new Paragraph(texto, heveltica10);
		}
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void addTitulo(String texto) {
		Paragraph textoP = new Paragraph(texto, heveltica12Bold);
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(TAMANHO_TABELA_PDF);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void addHeader() throws DocumentException {
		header.scaleAbsolute(340, 120);
		PdfPCell cell = new PdfPCell(header);
		cell.setColspan(TAMANHO_TABELA_PDF);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.disableBorderSide(TAMANHO_TABELA_PDF);
		cell.setPadding(1);
		table.addCell(cell);
		separadorTabela();
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(Paths.CAMINHO_RELATORIO);
			arqStreamed = new DefaultStreamedContent(stream, null, Paths.RELATORIO);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return arqStreamed;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public PdfPTable getTable() {
		return table;
	}

	public void setTable(PdfPTable table) {
		this.table = table;
	}

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public Image getHeader() {
		return header;
	}

	public void setHeader(Image header) {
		this.header = header;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public CampusDao getCampusDao() {
		return campusDao;
	}

	public void setCampusDao(CampusDao campusDao) {
		this.campusDao = campusDao;
	}

	public void setArqStreamed(StreamedContent arqStreamed) {
		this.arqStreamed = arqStreamed;
	}

	public List<Campus> getListaCampus() {
		return listaCampus;
	}

	public void setListaCampus(List<Campus> listaCampus) {
		this.listaCampus = listaCampus;
	}

	public List<Modalidade> getListaModalidade() {
		return listaModalidade;
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

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	public TimeDao getTimeDao() {
		return timeDao;
	}

	public void setTimeDao(TimeDao timeDao) {
		this.timeDao = timeDao;
	}

	public List<Time> getListaTime() {
		return listaTime;
	}

	public void setListaTime(List<Time> listaTime) {
		this.listaTime = listaTime;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public DelegacaoDao getDelegacaoDao() {
		return delegacaoDao;
	}

	public void setDelegacaoDao(DelegacaoDao delegacaoDao) {
		this.delegacaoDao = delegacaoDao;
	}

	public List<Delegacao> getListaDelegacao() {
		return listaDelegacao;
	}

	public void setListaDelegacao(List<Delegacao> listaDelegacao) {
		this.listaDelegacao = listaDelegacao;
	}

	public Delegacao getDelegacao() {
		return delegacao;
	}

	public void setDelegacao(Delegacao delegacao) {
		this.delegacao = delegacao;
	}

	public ChaveDao getChaveDao() {
		return chaveDao;
	}

	public void setChaveDao(ChaveDao chaveDao) {
		this.chaveDao = chaveDao;
	}

	public List<Chave> getListaChave() {
		return listaChave;
	}

	public void setListaChave(List<Chave> listaChave) {
		this.listaChave = listaChave;
	}

	public List<Partida> getListaPartida() {
		return listaPartida;
	}

	public void setListaPartida(List<Partida> listaPartida) {
		this.listaPartida = listaPartida;
	}

	public List<PartidaTime> getListaPartidaTime() {
		return listaPartidaTime;
	}

	public void setListaPartidaTime(List<PartidaTime> listaPartidaTime) {
		this.listaPartidaTime = listaPartidaTime;
	}

	public Chave getChave() {
		return chave;
	}

	public void setChave(Chave chave) {
		this.chave = chave;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public GrupoChavesDao getGrupoDao() {
		return grupoDao;
	}

	public void setGrupoDao(GrupoChavesDao grupoDao) {
		this.grupoDao = grupoDao;
	}

	public List<GrupoChaves> getListaGrupoChaves() {
		return listaGrupoChaves;
	}

	public void setListaGrupoChaves(List<GrupoChaves> listaGrupoChaves) {
		this.listaGrupoChaves = listaGrupoChaves;
	}

	public GrupoChaves getGrupoChaves() {
		return grupoChaves;
	}

	public void setGrupoChaves(GrupoChaves grupoChaves) {
		this.grupoChaves = grupoChaves;
	}

}