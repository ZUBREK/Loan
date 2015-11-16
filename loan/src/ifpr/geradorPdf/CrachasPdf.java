package ifpr.geradorPdf;

import java.awt.image.BufferedImage;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import ifpr.arquivo.Arquivo;
import ifpr.arquivo.dao.ArquivoDao;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.pessoa.coordenadorPea.CoordenadorPea;
import ifpr.pessoa.estudante.Estudante;
import ifpr.pessoa.secretario.Secretario;
import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

@ManagedBean(name = "crachasPdf")
@ViewScoped
public class CrachasPdf {
	private final String CAMINHO_PASTA_CRACHAS = "C:/home/loan_docs/cracha";
	private final String CAMINHO_ARQ_DEFAULT = "C:/home/loan_docs";
	private Document doc;
	private File arquivo;
	private PdfContentByte cb;
	private Image moldura;
	private Image logo;
	private QRCode qrCode;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private StreamedContent arqStreamed;

	public CrachasPdf() throws BadElementException, MalformedURLException, IOException {
		doc = new Document();
		arquivo = new File(CAMINHO_PASTA_CRACHAS);
		arquivo.mkdirs();
		moldura = Image.getInstance(CAMINHO_ARQ_DEFAULT + "/molduraCracha.png");
		logo = Image.getInstance(CAMINHO_ARQ_DEFAULT + "/logoJIFPR.png");
		qrCode = new QRCode();
	}

	public void gerarPdfDelegacao(List<Pessoa> listDesc) {
		arquivo = new File(CAMINHO_PASTA_CRACHAS + "/CrachasJIFPR.pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();

			for (int i = 0; i < listDesc.size(); i += 2) {
				addMoldura(1);
				Pessoa pessoa = (Pessoa) listDesc.get(i);
				addLogo(1);
				addFotoPerfil(pessoa, 1);
				addLinhaCracha(writer, "Nome:", 605, true);
				if (pessoa.getNome().length() > 31) {
					String nome = pessoa.getNome();
					String texto = nome.substring(0, 31);
					int particao = texto.lastIndexOf(" ");
					String primeiroNome = nome.substring(0, particao);
					String segundoNome = nome.substring(particao, nome.length());
					addLinhaCracha(writer, primeiroNome, 593, false);
					addLinhaCracha(writer, segundoNome, 580, false);
				} else {
					addLinhaCracha(writer, pessoa.getNome(), 585, false);
				}
				addLinhaCracha(writer, "Função:", 565, true);
				addLinhaCracha(writer, pessoa.getTipo().getLabel(), 545, false);
				addLinhaCracha(writer, "Câmpus:", 525, true);
				addLinhaCracha(writer, retornaCampus(pessoa), 505, false);
				addMoldura(2);
				String codeText = "id-" + pessoa.getId() + "-nome-" + pessoa.getNome();
				Image imagem = addQRCode(writer, codeText, 1);
				doc.add(imagem);

				if (i + 1 < listDesc.size()) {
					addMoldura(3);
					addLogo(2);
					Pessoa pessoa2 = (Pessoa) listDesc.get(i + 1);
					addFotoPerfil(pessoa2, 2);
					addLinhaCracha(writer, "Nome:", 235, true);
					if (pessoa2.getNome().length() > 31) {
						String nome = pessoa2.getNome();
						String texto = nome.substring(0, 31);
						int particao = texto.lastIndexOf(" ");
						String primeiroNome = nome.substring(0, particao);
						String segundoNome = nome.substring(particao, nome.length());
						addLinhaCracha(writer, primeiroNome, 223, false);
						addLinhaCracha(writer, segundoNome, 210, false);
					} else {
						addLinhaCracha(writer, pessoa2.getNome(), 215, false);
					}
					addLinhaCracha(writer, "Função:", 195, true);
					addLinhaCracha(writer, pessoa2.getTipo().getLabel(), 175, false);
					addLinhaCracha(writer, "Câmpus:", 155, true);
					addLinhaCracha(writer, retornaCampus(pessoa2), 135, false);
					addMoldura(4);
					String codeText2 = "id-" + pessoa2.getId() + "-nome-" + pessoa2.getNome();
					Image imagem2 = addQRCode(writer, codeText2, 2);
					doc.add(imagem2);
				}
				doc.newPage();
			}
			doc.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gerarPdfSecretario(List<Secretario> listDesc) {
		arquivo = new File(CAMINHO_PASTA_CRACHAS + "/CrachasGerados.pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
			doc.open();
			doc.addAuthor("SISTEMA_LOAN");
			doc.addCreationDate();

			for (int i = 0; i < listDesc.size(); i++) {
				addMoldura(1);
				addLogo(1);
				Secretario secretario = (Secretario) listDesc.get(i);
				addFotoPerfil(secretario, 1);
				addLinhaCracha(writer, "Nome:", 605, true);
				if (secretario.getNome().length() > 31) {
					String nome = secretario.getNome();
					String texto = nome.substring(0, 31);
					int particao = texto.lastIndexOf(" ");
					String primeiroNome = nome.substring(0, particao);
					String segundoNome = nome.substring(particao, nome.length());
					addLinhaCracha(writer, primeiroNome, 593, false);
					addLinhaCracha(writer, segundoNome, 580, false);
				} else {
					addLinhaCracha(writer, secretario.getNome(), 585, false);
				}
				addLinhaCracha(writer, "Função:", 565, true);
				addLinhaCracha(writer, secretario.getTipo().getLabel(), 545, false);
				addMoldura(2);
				String codeText = "id-" + secretario.getId() + "-nome-" + secretario.getNome();
				Image imagem = addQRCode(writer, codeText, 1);
				doc.add(imagem);
				if (i + 1 < listDesc.size()) {
					addMoldura(3);
					addLogo(2);
					Secretario secretario2 = (Secretario) listDesc.get(i + 1);
					addFotoPerfil(secretario2, 2);
					addLinhaCracha(writer, "Nome:", 235, true);
					if (secretario2.getNome().length() > 31) {
						String nome = secretario2.getNome();
						String texto = nome.substring(0, 31);
						int particao = texto.lastIndexOf(" ");
						String primeiroNome = nome.substring(0, particao);
						String segundoNome = nome.substring(particao, nome.length());
						addLinhaCracha(writer, primeiroNome, 223, false);
						addLinhaCracha(writer, segundoNome, 210, false);
					} else {
						addLinhaCracha(writer, secretario2.getNome(), 215, false);
					}
					addLinhaCracha(writer, "Função:", 195, true);
					addLinhaCracha(writer, secretario2.getTipo().getLabel(), 175, false);
					addMoldura(4);
					String codeText2 = "id-" + secretario2.getId() + "-nome-" + secretario2.getNome();
					Image imagem2 = addQRCode(writer, codeText2, 2);
					doc.add(imagem2);
				}
				doc.newPage();
			}
			doc.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Image addQRCode(PdfWriter writer, String codeText, int posicao) throws BadElementException, IOException {
		BufferedImage imagemCode = qrCode.gerarQRCode(codeText);
		PdfContentByte pdfCB = new PdfContentByte(writer);
		Image image = Image.getInstance(pdfCB, imagemCode, 1);
		image.scaleAbsolute(170, 170);
		if (posicao == 1) {
			image.setAbsolutePosition(360, 580);
		} else {
			image.setAbsolutePosition(360, 210);
		}
		return image;
	}

	private String retornaCampus(Pessoa pessoa) {
		String campus = null;
		if (pessoa.getTipo().equals(TipoPessoa.ROLE_TEC_ADM)) {
			TecnicoAdministrativo tecAdm = (TecnicoAdministrativo) pessoa;
			campus = tecAdm.getCampus().getCidade();
		} else if (pessoa.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)) {
			TecnicoEsportivo tecEsp = (TecnicoEsportivo) pessoa;
			campus = tecEsp.getCampus().getCidade();
		} else if (pessoa.getTipo().equals(TipoPessoa.ROLE_COORDENADOR)) {
			CoordenadorPea coordPea = (CoordenadorPea) pessoa;
			campus = coordPea.getCampus().getCidade();
		} else if (pessoa.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {
			Estudante estudante = (Estudante) pessoa;
			campus = estudante.getCampus().getCidade();
		}
		return campus;
	}

	private void addFotoPerfil(Pessoa pessoa, int fotoNumero)
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		Arquivo fotoPerfil = arquivoDao.pesquisarFotoPerfil(pessoa);
		Image fotoPerfilImg = Image.getInstance(fotoPerfil.getCaminho());
		fotoPerfilImg.setAlignment(Element.ALIGN_MIDDLE);
		fotoPerfilImg.scaleAbsolute(110, 100);
		if (fotoNumero == 1) {
			fotoPerfilImg.setAbsolutePosition(100, 630);
		} else {
			fotoPerfilImg.setAbsolutePosition(100, 260);
		}
		doc.add(fotoPerfilImg);
	}

	private void addLogo(int posicao) throws DocumentException {
		logo.scalePercent(5f);
		if (posicao == 1) {
			logo.setAbsolutePosition(110, 740);

		} else {
			logo.setAbsolutePosition(110, 370);
		}
		doc.add(logo);
	}

	private Image addMoldura(int numero)
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		if (numero == 1) {
			moldura.scaleAbsolute(300, 380);
			moldura.setAbsolutePosition(10, 450);
			doc.add(moldura);
		} else if (numero == 2) {
			moldura.scaleAbsolute(300, 380);
			moldura.setAbsolutePosition(300, 450);
			doc.add(moldura);
		} else if (numero == 3) {
			moldura.scaleAbsolute(300, 380);
			moldura.setAbsolutePosition(10, 80);
			doc.add(moldura);
		} else if (numero == 4) {
			moldura.scaleAbsolute(300, 380);
			moldura.setAbsolutePosition(300, 80);
			doc.add(moldura);
		}
		return moldura;
	}

	private void addLinhaCracha(PdfWriter writer, String texto, int posicao, boolean bold)
			throws DocumentException, IOException {
		cb = writer.getDirectContent();
		BaseFont bf;
		if (bold == true) {
			bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} else {
			bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		}
		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(bf, 12);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, texto, 150, posicao, 0);
		cb.endText();
		cb.restoreState();
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
			arqStreamed = new DefaultStreamedContent(stream, null, "CrachasJIFPR.pdf");
		} catch (FileNotFoundException e) {
			System.out.println("Erro no download do arquivo!");
		}
		return arqStreamed;
	}

	public void setArqStreamed(StreamedContent arqStreamed) {
		this.arqStreamed = arqStreamed;
	}

	public PdfContentByte getCb() {
		return cb;
	}

	public void setCb(PdfContentByte cb) {
		this.cb = cb;
	}

	public Image getMoldura() {
		return moldura;
	}

	public void setMoldura(Image moldura) {
		this.moldura = moldura;
	}

	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}

	public QRCode getQrCode() {
		return qrCode;
	}

	public void setQrCode(QRCode qrCode) {
		this.qrCode = qrCode;
	}

}
