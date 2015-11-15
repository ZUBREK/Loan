package ifpr.competicao.partida;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ifpr.arquivo.Arquivo;
import ifpr.competicao.partida.local.Local;
import ifpr.competicao.partidaTimePlacar.PartidaTimePlacar;

@Entity
@Table(name = "tbPartida")
public class Partida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_partida")
	private Integer id;

	@JoinColumn(name = "id_sumula_partida", referencedColumnName = "id_arquivo")
	@ManyToOne()
	private Arquivo sumula;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_hora_partida")
	private Date dataHora;
	
	@JoinColumn(name = "id_local", referencedColumnName = "id_local")
	@ManyToOne()
	private Local local;
	
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name = "id_partida", referencedColumnName = "id_partida")
	private List<PartidaTimePlacar> partidasTimesPlacares;

	public Partida() {
		partidasTimesPlacares = new ArrayList<>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Arquivo getSumula() {
		return sumula;
	}

	public void setSumula(Arquivo sumula) {
		this.sumula = sumula;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public List<PartidaTimePlacar> getPartidasTimesPlacares() {
		return partidasTimesPlacares;
	}

	public void setPartidasTimesPlacares(List<PartidaTimePlacar> partidasTimesPlacares) {
		this.partidasTimesPlacares = partidasTimesPlacares;
	}
	
}
