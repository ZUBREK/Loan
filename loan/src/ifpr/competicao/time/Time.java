package ifpr.competicao.time;

import ifpr.campus.Campus;
import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.competicao.time.pontos.PontosTime;
import ifpr.modalidade.Modalidade;
import ifpr.pessoa.Pessoa;

import java.util.ArrayList;
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
import javax.persistence.OneToOne;
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
	private Pessoa tecnico;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne()
	private Campus campus;

	@JoinColumn(name = "id_time", referencedColumnName = "id_time")
	@OneToMany()
	private List<TimeEstudante> timeEstudante;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "id_pontos_time", referencedColumnName = "id_pontos_time")
	private PontosTime pontosTime;
	
	public Time() {
		timeEstudante = new ArrayList<TimeEstudante>();
		pontosTime = new PontosTime();
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

	public Pessoa getTecnico() {
		return tecnico;
	}

	public void setTecnico(Pessoa tecnico) {
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

	public PontosTime getPontosTime() {
		return pontosTime;
	}

	public void setPontosTime(PontosTime pontosTime) {
		this.pontosTime = pontosTime;
	}

	@Override
	public String toString() {
		return nome;
	}

	public boolean equals(Object other) {
		return other instanceof Time && (id != null) ? id.equals(((Time) other).getId()) : (other == this);
	}

	public int hashCode() {
		return id != null ? this.getClass().hashCode() + id.hashCode() : super.hashCode();
	}
}
