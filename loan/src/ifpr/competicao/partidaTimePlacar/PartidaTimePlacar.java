package ifpr.competicao.partidaTimePlacar;

import ifpr.competicao.partida.Partida;
import ifpr.competicao.placar.Placar;
import ifpr.competicao.time.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbPartidaTimePlacar")
public class PartidaTimePlacar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JoinColumn(name = "id_placar", referencedColumnName = "id_placar")
	@ManyToOne()
	private Placar placar;
	
	
	@JoinColumn(name = "id_time", referencedColumnName = "id_time")
	@ManyToOne()
	private Time time;
	
	@JoinColumn(name = "id_partida", referencedColumnName = "id_partida")
	@ManyToOne()
	private Partida partida;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Placar getPlacar() {
		return placar;
	}

	public void setPlacar(Placar placar) {
		this.placar = placar;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
}
