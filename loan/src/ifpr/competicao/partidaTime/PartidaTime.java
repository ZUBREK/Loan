package ifpr.competicao.partidaTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.competicao.partida.Partida;
import ifpr.competicao.time.Time;

@Entity
@Table(name = "tbPartidaTime")
public class PartidaTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JoinColumn(name = "id_time", referencedColumnName = "id_time")
	@ManyToOne()
	private Time time;

	@JoinColumn(name = "id_partida", referencedColumnName = "id_partida")
	@ManyToOne(cascade = CascadeType.REMOVE)
	private Partida partida;
	
	@Column(name="placar_partida_time")
	private int placar;
	
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

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	@Override
	public String toString() {
		if(time!= null){
			return time.getNome();
		} else {
			return "";
		}
	}

	public int getPlacar() {
		return placar;
	}

	public void setPlacar(int placar) {
		this.placar = placar;
	}
}
