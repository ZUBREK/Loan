package ifpr.geradorPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.campus.Campus;
import ifpr.campus.dao.CampusDao;
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

	private Modalidade modalidade;
	private Campus campus;
	private Time time;
	private Delegacao delegacao;

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
			table = new PdfPTable(TAMANHO_TABELA_PDF);
			doc = new Document();
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(caminho));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();
			addHeader();
			addCampus();
			addModalidades();
			addTimes();
			addDelegacoes();
			doc.add(table);
			doc.close();
			writer.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private void addDelegacoes() {
		listaDelegacao = delegacaoDao.listDesc();
		String titulo = "DELEGAÇÕES";
		addTitulo(titulo);
		for (int i = 0; i < listaDelegacao.size(); i++) {
			delegacao = listaDelegacao.get(i);
			addLinha(delegacao.getNome() + "/" + delegacao.getCampus().getCidade(), true);
			add2Titulo("NOME:","FUNÇÃO:");
			List<Pessoa> listaPessoa = delegacaoDao.listarPessoas(delegacao);
			Pessoa pessoa = new Pessoa();
			for (int j = 0; j < listaPessoa.size(); j++) {
				pessoa = listaPessoa.get(j);
				add2Linha(pessoa.getNome(), pessoa.getTipo().getLabel());
			}
		}
	}

	private void addCampus() {
		listaCampus = campusDao.listarAlfabetica();
		String titulo = "CÂMPUS";
		addTitulo(titulo);
		for (int i = 0; i < listaCampus.size(); i++) {
			campus = listaCampus.get(i);
			addLinha(campus.getCidade(), false);
		}
	}

	private void addModalidades() {
		listaModalidade = modalidadeDao.listarAlfabetica();
		String titulo = "MODALIDADES";
		addTitulo(titulo);
		for (int i = 0; i < listaModalidade.size(); i++) {
			modalidade = listaModalidade.get(i);
			addLinha(modalidade.getNome(), false);
		}
	}

	private void addTimes() {
		listaTime = timeDao.listDesc();
		String titulo = "TIMES/CÂMPUS";
		addTitulo(titulo);
		for (int i = 0; i < listaTime.size(); i++) {
			time = listaTime.get(i);
			addLinha(time.getNome()+"/"+time.getCampus().getCidade(),true);
			add2Titulo("NOME:","FUNÇÃO:");
			add2Linha(time.getTecnico().getNome(), time.getTecnico().getTipo().getLabel());
			List<Estudante> listaEstudante = timeDao.listarEstudantes(time);
			Estudante estudante = new Estudante();
			for (int j = 0; j < listaEstudante.size(); j++) {
				estudante = listaEstudante.get(j);
				add2Linha(estudante.getNome(), estudante.getTipo().getLabel());
			}
		}
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

	private void addTitulo(String texto) {
		Paragraph textoP = new Paragraph(texto, heveltica12Bold);
		textoP.setAlignment(Element.ALIGN_CENTER);
		PdfPCell celula = new PdfPCell();
		celula.setColspan(TAMANHO_TABELA_PDF);
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celula.addElement(textoP);
		table.addCell(celula);
	}

	private void addHeader() {
		header.scaleAbsolute(380, 150);
		PdfPCell cell = new PdfPCell(header);
		cell.setColspan(TAMANHO_TABELA_PDF);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(82);
		cell.disableBorderSide(TAMANHO_TABELA_PDF);
		cell.setPadding(1);
		table.addCell(cell);
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
	
}
