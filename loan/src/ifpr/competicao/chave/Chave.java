package ifpr.competicao.chave;

import ifpr.modalidade.Modalidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbChave")
public class Chave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_chave")
	private Integer id;

	@Column(name = "nome_chave", length = 20)
	private String nome;

	@JoinColumn(name = "id_modalidade", referencedColumnName = "id_modalidade")
	@ManyToOne()
	private Modalidade modalidade;

	@Column(name = "tipo_chave", length = 20)
	@Enumerated(EnumType.STRING)
	private TipoCompeticao tipo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public TipoCompeticao getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompeticao tipo) {
		this.tipo = tipo;
	}

}