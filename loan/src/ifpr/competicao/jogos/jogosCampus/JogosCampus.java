package ifpr.competicao.jogos.jogosCampus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.campus.Campus;
import ifpr.competicao.jogos.Jogos;

@Entity
@Table(name = "tbJogosCampus")
public class JogosCampus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jogos_campus")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	private Campus campus;

	@ManyToOne
	@JoinColumn(name = "id_jogos", referencedColumnName = "id_jogos")
	private Jogos jogos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public Jogos getJogos() {
		return jogos;
	}

	public void setJogos(Jogos jogos) {
		this.jogos = jogos;
	}

}
