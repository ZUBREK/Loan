package ifpr.competicao.jogos.jogosModalidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.competicao.jogos.Jogos;
import ifpr.modalidade.Modalidade;

@Entity
@Table(name = "tbJogosModalidade")
public class JogosModalidade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jogos_modalidade")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_modalidade", referencedColumnName = "id_modalidade")
	private Modalidade modalidade;

	@ManyToOne
	@JoinColumn(name = "id_jogos", referencedColumnName = "id_jogos")
	private Jogos jogos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public Jogos getJogos() {
		return jogos;
	}

	public void setJogos(Jogos jogos) {
		this.jogos = jogos;
	}
}
