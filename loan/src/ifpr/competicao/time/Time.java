package ifpr.competicao.time;

import java.util.ArrayList;
import java.util.List;

import ifpr.campus.Campus;
import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.modalidade.Modalidade;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbTime")
public class Time {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_time")
	private Integer id;

	@Column(name = "nome_time", length = 50)
	private String nome;

	@JoinColumn(name = "id_modalidade", referencedColumnName = "id_modalidade")
	@ManyToOne()
	private Modalidade modalidade;

	@JoinColumn(name = "id_tec_esp", referencedColumnName = "id_pessoa")
	@ManyToOne()
	private TecnicoEsportivo tecnico;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne()
	private Campus campus;

	@JoinColumn(name = "id_time", referencedColumnName = "id_time")
	@OneToMany()
	private List<TimeEstudante> timeEstudante;

	public Time(){
		timeEstudante = new ArrayList<TimeEstudante>();
	}
	
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

	public TecnicoEsportivo getTecnico() {
		return tecnico;
	}

	public void setTecnico(TecnicoEsportivo tecnico) {
		this.tecnico = tecnico;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public List<TimeEstudante> getTimeEstudante() {
		return timeEstudante;
	}

	public void setTimeEstudante(List<TimeEstudante> timeEstudante) {
		this.timeEstudante = timeEstudante;
	}

}
