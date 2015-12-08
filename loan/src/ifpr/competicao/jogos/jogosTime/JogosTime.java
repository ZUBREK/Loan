package ifpr.competicao.jogos.jogosTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.competicao.jogos.Jogos;
import ifpr.competicao.time.Time;

@Entity
@Table(name = "tbJogosTime")
public class JogosTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jogos_time")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_time", referencedColumnName = "id_time")
	private Time time;

	@ManyToOne
	@JoinColumn(name = "id_jogos", referencedColumnName = "id_jogos")
	private Jogos jogos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Jogos getJogos() {
		return jogos;
	}

	public void setJogos(Jogos jogos) {
		this.jogos = jogos;
	}

}
