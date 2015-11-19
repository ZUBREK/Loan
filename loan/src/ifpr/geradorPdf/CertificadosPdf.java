package ifpr.geradorPdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.zxing.Writer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.pessoa.Pessoa;

@ManagedBean(name = "certificadosPdf")
@ViewScoped
public class CertificadosPdf {
	private Document doc;
	private final String CAMINHO_PASTA_CERTIFICADOS = "C:/home/loan_docs/certificados";
	private File arquivo;
	private Image moldura;
	private StreamedContent arqStreamed;

	public CertificadosPdf() {
		doc = new Document();
		arquivo = new File(CAMINHO_PASTA_CERTIFICADOS);
		arquivo.mkdirs();
		doc.setPageSize(PageSize.A4.rotate());
	}

	public void gerarCertificados(List<Pessoa> lista) {
		arquivo = new File(CAMINHO_PASTA_CERTIFICADOS + "/CertificadosJIFPR.pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();
			addMoldura();
			for (int i = 0; i < lista.size(); i++) {

			}
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private Image addMoldura() throws DocumentException {
		moldura.scaleAbsolute(300, 380);
		moldura.setAbsolutePosition(10, 450);
		doc.add(moldura);
		return moldura;
	}

	public StreamedContent getArqStreamed() {
		InputStream stream;
		try {
			stream = new FileInputStream(arquivo.getAbsolutePath());
			arqStreamed = new DefaultStreamedContent(stream, null, "CrachasJIFPR.pdf");
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
