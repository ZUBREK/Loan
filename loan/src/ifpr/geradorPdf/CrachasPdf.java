package ifpr.geradorPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.pessoa.estudante.Estudante;

@ManagedBean(name = "crachasPdf")
@ViewScoped
public class CrachasPdf {
	private final String CAMINHO_PASTA_CRACHAS = "C:/home/loan_docs/cracha";
	private BaseColor black;
	private Font fonte;
	private Document doc;
	private File arquivo;
	private Paragraph paragrafo;
	private PdfPTable tabela;
	private final int COLUNAS = 2;
	private PdfPCell celula;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private StreamedContent arqStreamed;

	public CrachasPdf() {
		doc = new Document();
		black = new BaseColor(1, 1, 1);
		arquivo = new File(CAMINHO_PASTA_CRACHAS);
		arquivo.mkdirs();
	}

	public void gerarPdf(List<Estudante> listDesc) {
		arquivo = new File(CAMINHO_PASTA_CRACHAS + "/CrachasGerados.pdf");
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();

			for (int i = 0; i < listDesc.size(); i++) {
				tabela = new PdfPTable(COLUNAS);
				tabela.setTotalWidth(500);
				tabela.setWidthPercentage(110f);
				celula = new PdfPCell();
				celula.setBackgroundColor(BaseColor.WHITE);
				celula.setBorder(Rectangle.NO_BORDER);
				Image img = Image.getInstance("Moldura.png");
				img.setAlignment(Element.ALIGN_LEFT);
				celula.addElement(img);
				Estudante estudante = listDesc.get(i);
				Arquivo fotoPerfil = arquivoDao.pesquisarFotoPerfil(estudante);
				Image fotoPerfilImg = Image.getInstance(fotoPerfil.getCaminho());
				fotoPerfilImg.setAlignment(Element.ALIGN_MIDDLE);
				fotoPerfilImg.scaleAbsolute(150, 100);
				adicionarCelulaTabela("Nome:", true);
				adicionarCelulaTabela(estudante.getNome(), false);
				tabela.addCell(celula);
				celula = new PdfPCell();
				celula.setBackgroundColor(BaseColor.WHITE);
				celula.setBorder(Rectangle.NO_BORDER);
				Image img2 = Image.getInstance("Moldura.png");
				img.setAlignment(Element.ALIGN_RIGHT);
				celula.addElement(img2);
				tabela.addCell(celula);
				doc.add(tabela);
				/*adicionarCelulaTabela("Data de nascimento: ", true);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				adicionarCelulaTabela(sdf.format(estudante.getDataNascimento()), false);
				adicionarCelulaTabela("Matricula: ", true);
				adicionarCelulaTabela(estudante.getMatricula(), false);
				adicionarCelulaTabela("Câmpus: ", true);
				adicionarCelulaTabela(estudante.getCampus().toString(), false);*/
				doc.add(tabela);
				doc.newPage();
			}

			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void adicionarCelulaTabela(String string, boolean bold) {
		if (bold) {
			fonte = FontFactory.getFont(FontFactory.COURIER, (float) 12, Font.BOLD, black);
		} else {
			fonte = FontFactory.getFont(FontFactory.COURIER, (float) 12, Font.NORMAL, black);
		}
		paragrafo = new Paragraph(string, fonte);
		celula.addElement(paragrafo);
	}

	public BaseColor getBlack() {
		return black;
	}

	public void setBlack(BaseColor black) {
		this.black = black;
	}

	public Font getFonte() {
		return fonte;
	}

	public void setFonte(Font fonte) {
		this.fonte = fonte;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public Paragraph getParagrafo() {
		return paragrafo;
	}

	public void setParagrafo(Paragraph paragrafo) {
		this.paragrafo = paragrafo;
	}

	public PdfPTable getTabela() {
		return tabela;
	}

	public void setTabela(PdfPTable tabela) {
		this.tabela = tabela;
	}

	public PdfPCell getCelula() {
		return celula;
	}

	public void setCelula(PdfPCell celula) {
		this.celula = celula;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(arquivo.getAbsolutePath());
			arqStreamed = new DefaultStreamedContent(stream, null, "CrachasGerados.pdf");
		} catch (FileNotFoundException e) {
			System.out.println("Erro no download de imagem");
		}

		return arqStreamed;
	}

	public void setArqStreamed(StreamedContent arqStreamed) {
		this.arqStreamed = arqStreamed;
	}

}
