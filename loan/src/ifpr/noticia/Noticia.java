package ifpr.noticia;

import ifpr.arquivo.Arquivo;

import java.util.Date;

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

@Entity
@Table(name="tbNoticia")
public class Noticia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_noticia")
	private Integer id;
	
	
	@Column(name="texto_noticia", columnDefinition="MEDIUMTEXT")
	private String texto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_publicacao")
	private Date data;
	
	@Column(name="titulo_noticia",length=100)
	private String titulo;
	
	@JoinColumn(name = "id_arquivo", referencedColumnName = "id_arquivo")
	@ManyToOne()
	private Arquivo foto;

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
	
	
}
