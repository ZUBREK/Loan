package ifpr.projeto.relatorio;

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

import ifpr.pessoa.Pessoa;


@Entity
@Table(name = "tbRelatorioProjero")
public class RelatorioProjeto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_relatorio")
	private Integer id;

	@Column(name="caminho_relatorio")
	private String caminho;

	@Column(name="nome_relatorio",length=50)
	private String nome;


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
}
