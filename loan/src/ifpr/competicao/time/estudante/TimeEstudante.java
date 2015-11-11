package ifpr.competicao.time.estudante;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.competicao.time.Time;
import ifpr.pessoa.estudante.Estudante;

@Entity
@Table(name = "tbTimeEstudante")
public class TimeEstudante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_timeEstudante")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_time", referencedColumnName = "id_time")
	private Time time;

	@ManyToOne
	@JoinColumn(name = "id_estudante", referencedColumnName = "id_pessoa")
	private Estudante estudante;

	public TimeEstudante() {
	}

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

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

}
