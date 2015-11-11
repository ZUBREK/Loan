package ifpr.competicao.partida;

import ifpr.arquivo.Arquivo;
import ifpr.competicao.chave.Chave;
import ifpr.competicao.partida.local.Local;

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

	@JoinColumn(name = "id_chave", referencedColumnName = "id_chave")
	@ManyToOne()
	private Chave chave;

	@JoinColumn(name = "id_local", referencedColumnName = "id_local")
	@ManyToOne()
	private Local local;

}
