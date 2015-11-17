package ifpr.geradorPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.pessoa.Pessoa;

@ManagedBean(name = "certificadosPdf")
@ViewScoped
public class CertificadosPdf {
	private Document doc;
	private final String CAMINHO_PASTA_CERTIFICADOS = "C:/home/loan_docs/certificados";
	private final String CAMINHO_ARQ_DEFAULT = "C:/home/loan_docs";
	private File arquivo;
	private Image moldura;
	private Image logo;
	private Image assinatura;
	private PdfContentByte cb;
	private StreamedContent arqStreamed;

	public CertificadosPdf() throws BadElementException, MalformedURLException, IOException {
		doc = new Document();
		arquivo = new File(CAMINHO_PASTA_CERTIFICADOS);
		arquivo.mkdirs();
		moldura = Image.getInstance(CAMINHO_ARQ_DEFAULT + "/molduraCertificados.png");
		logo = Image.getInstance(CAMINHO_ARQ_DEFAULT + "/logoJIFPR.png");
		assinatura = Image.getInstance(CAMINHO_ARQ_DEFAULT + "/assinaturaCertificado.png");
		doc.setPageSize(PageSize.A4.rotate());
	}

	public void gerarCertificados(List<Pessoa> lista) {
		arquivo = new File(CAMINHO_PASTA_CERTIFICADOS + "/CertificadosJIFPR.pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();
			for (int i = 0; i < lista.size(); i++) {
				addMoldura();
				addLogo();
				addTituloCertificado(writer);
				Date data = new Date();  
				SimpleDateFormat formatador = new SimpleDateFormat("yyyy");
				Pessoa pessoa = (Pessoa) lista.get(i);
				String texto = "Certificamos que o(a) " + pessoa.getTipo().getLabel() + " " + 
						pessoa.getNome() + " participou dos Jogos do Instituto Federal " +
						"do Paraná (JIFPR), promovido pela Pró-Reitoria de Ensino (PROENS)" +
						" e pela Diretoria de Assuntos Estudantis e Atividades Especiais " + 
						"(DAES) no ano de " + formatador.format(data) + ".";
				int linha = 373;
				while(texto.length()>60){
					String substring = texto.substring(0, 60);
					int particao = substring.lastIndexOf(" ");
					String primeiroTexto = texto.substring(0, particao);
					String segundoTexto = texto.substring(particao, texto.length());
					texto = segundoTexto;
					addLinhaCertificado(writer, primeiroTexto,linha-=20);
				}
				addLinhaCertificado(writer, texto,linha-=20);
				addAssinatura();
				doc.newPage();
			}
			doc.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addMoldura() throws DocumentException {
		moldura.scaleAbsolute(150f, 630f);
		moldura.setAlignment(Element.ALIGN_RIGHT);
		moldura.setAbsolutePosition(695, 0);
		doc.add(moldura);
	}

	private void addLogo() throws DocumentException {
		logo.scalePercent(10f);
		logo.setAlignment(Element.ALIGN_LEFT);
		logo.setAbsolutePosition(65, 470);
		doc.add(logo);
	}
	
	private void addAssinatura() throws DocumentException {
		assinatura.scalePercent(80f);
		assinatura.setAlignment(Element.ALIGN_LEFT);
		assinatura.setAbsolutePosition(250, 110);
		doc.add(assinatura);
	}
	
	private void addTituloCertificado(PdfWriter writer) throws DocumentException, IOException{
		cb = writer.getDirectContent();
		BaseFont bf;
		bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 20);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER,"CERTIFICADO", 380, 410, 0);
		cb.endText();
		cb.restoreState();
	}
	
	private void addLinhaCertificado(PdfWriter writer,String texto,int linha) throws DocumentException, IOException{
		cb = writer.getDirectContent();
		BaseFont bf;
		bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 15);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, texto, 380, linha, 0);
		cb.endText();
		cb.restoreState();
	}


	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(arquivo.getAbsolutePath());
			arqStreamed = new DefaultStreamedContent(stream, null, "CertificadosJIFPR.pdf");
		} catch (FileNotFoundException e) {
			System.out.println("Erro no download de imagem");
		}

		return arqStreamed;
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

	public Image getMoldura() {
		return moldura;
	}

	public void setMoldura(Image moldura) {
		this.moldura = moldura;
	}

	public void setArqStreamed(StreamedContent arqStreamed) {
		this.arqStreamed = arqStreamed;
	}

}
