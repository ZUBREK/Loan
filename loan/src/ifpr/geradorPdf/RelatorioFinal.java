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
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;

@ManagedBean(name = "relatorioFinal")
@ViewScoped
public class RelatorioFinal {
	private Document doc;
	private final String CAMINHO_PASTA_RELATORIO = "C:/home/loan_docs/relatorioFinal";
	private final String CAMINHO_ARQ_DEFAULT = "C:/home/loan_docs";
	private final String CAMINHO_RELATORIO = CAMINHO_PASTA_RELATORIO + "/RelatorioFinalJIFPR.pdf";
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

	private Modalidade modalidade;
	private Campus campus;

	public RelatorioFinal() throws BadElementException, MalformedURLException, IOException {
		doc = new Document();
		arquivo = new File(CAMINHO_PASTA_RELATORIO);
		arquivo.mkdirs();
		header = Image.getInstance(CAMINHO_ARQ_DEFAULT + "/logoJIFPR.png");
	}

	public void gerarRelatorio() {
		File caminho = new File(CAMINHO_RELATORIO);
		if (caminho.exists()) {
			caminho.delete();
		}
		try {
			doc = new Document();
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(caminho));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();
			addHeader();
			addCampus();
			addModalidade();
			doc.add(table);
			doc.close();
			writer.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private void addCampus() {
		listaCampus = campusDao.listarAlfabetica();
		String titulo = "CÂMPUS";
		addTitulo(titulo);
		for (int i = 0; i < listaCampus.size(); i++) {
			campus = listaCampus.get(i);
			addLinha(campus.getCidade());
		}
	}

	private void addModalidade() {
		listaModalidade = modalidadeDao.listarAlfabetica();
		String titulo = "MODALIDADE";
		table = new PdfPTable(TAMANHO_TABELA_PDF);
		addTitulo(titulo);
		for (int i = 0; i < listaModalidade.size(); i++) {
			modalidade = listaModalidade.get(i);
			addLinha(modalidade.getNome());
		}
	}

	private void addLinha(String texto) {
		Paragraph textoP = new Paragraph(texto, heveltica10);
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
			stream = new FileInputStream(CAMINHO_RELATORIO);
			arqStreamed = new DefaultStreamedContent(stream, null, "RelatorioFinalJIFPR.pdf");
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

}
