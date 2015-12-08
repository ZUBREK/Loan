
package ifpr.noticia;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.primefaces.model.StreamedContent;

import ifpr.arquivo.Arquivo;

@Entity
@Table(name = "tbNoticia")
public class Noticia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_noticia")
	private Integer id;

	@Column(name = "texto_noticia", columnDefinition = "MEDIUMTEXT")
	private String texto;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_publicacao")
	private Date data;

	@Column(name = "titulo_noticia", length = 100)
	private String titulo;

	@JoinColumn(name = "id_arquivo", referencedColumnName = "id_arquivo")
	@ManyToOne(cascade = CascadeType.REMOVE)
	private Arquivo foto;

	@Transient
	private StreamedContent imagemStreamed;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Arquivo getFoto() {
		return foto;
	}

	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}
/*
	public StreamedContent getImagemStreamed() {
		InputStream stream;
		imagemStreamed = null;
		FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
        	File file = new File(foto.getCaminho());
    		if (file.exists()) {
    			stream = null;
    			try {
    				stream = new FileInputStream(file);
    				imagemStreamed = new DefaultStreamedContent(stream);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
        }
		return imagemStreamed;
	}

	public void setImagemStreamed(StreamedContent imagemStreamed) {
		this.imagemStreamed = imagemStreamed;
	}
*/
}
