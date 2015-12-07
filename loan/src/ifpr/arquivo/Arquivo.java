package ifpr.arquivo;

import ifpr.pessoa.Pessoa;

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
@Table(name = "tbArquivo")
public class Arquivo {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_arquivo")
	private Integer id;

	@Column(name="caminho_arquivo")
	private String caminho;

	@Column(name="nome_arquivo")
	private String nome;

	@Column(name="isFoto_perfil")
	private boolean isFotoPerfil;

	@ManyToOne
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	private Pessoa uploader;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_upload")
	private Date dataUpload;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isFotoPerfil() {
		return isFotoPerfil;
	}

	public void setFotoPerfil(boolean isFotoPerfil) {
		this.isFotoPerfil = isFotoPerfil;
	}

	public Pessoa getUploader() {
		return uploader;
	}

	public void setUploader(Pessoa uploader) {
		this.uploader = uploader;
	}

	public Date getDataUpload() {
		return dataUpload;
	}

	public void setDataUpload(Date dataUpload) {
		this.dataUpload = dataUpload;
	}
	
	@Override
	public String toString() {
		return nome;
	}

}
