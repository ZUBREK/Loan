package ifpr.evento.arquivo;

import ifpr.evento.eventoPessoa.EventoPessoa;
import ifpr.pessoa.Pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbArquivoEvento")
public class ArquivoEvento {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_arquivo_evento")
	private Integer id;

	@Column(name="caminho_arquivo_evento")
	private String caminho;

	@Column(name="nome_arquivo_evento",length=50)
	private String nome;

	@ManyToOne
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	private Pessoa uploader;
	
	
	@OneToOne
	@JoinColumn(name = "id_pessoa_evento", referencedColumnName = "id_pessoa_evento")
	private EventoPessoa eventoPessoa;
	
	
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


	public EventoPessoa getEventoPessoa() {
		return eventoPessoa;
	}

	public void setEventoPessoa(EventoPessoa eventoPessoa) {
		this.eventoPessoa = eventoPessoa;
	}

	
}
