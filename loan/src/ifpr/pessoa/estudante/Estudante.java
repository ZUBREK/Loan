package ifpr.pessoa.estudante;

import ifpr.campus.Campus;
import ifpr.competicao.time.estudante.TimeEstudante;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbEstudante")
public class Estudante extends Pessoa {

	private boolean isBolsista;

	@Column(name = "matricula_estudante", length = 20)
	private String matricula;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne()
	private Campus campus;

	@JoinColumn(name = "id_estudante", referencedColumnName = "id_pessoa")
	@OneToMany()
	private List<ProjetoEstudante> projetoEstudante;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento_estudante")
	private Date dataNascimento;

	@Column(name = "rg_estudante")
	private String rg;

	@Column(name = "cpf_estudante")
	private String cpf;

	@JoinColumn(name = "id_estudante", referencedColumnName = "id_pessoa")
	@OneToMany()
	private List<TimeEstudante> timeEstudante;

	public boolean isBolsista() {
		return isBolsista;
	}

	public void setBolsista(boolean isBolsista) {
		this.isBolsista = isBolsista;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}
	
	public List<ProjetoEstudante> getProjetoEstudante() {
		return projetoEstudante;
	}

	public void setProjetoEstudante(List<ProjetoEstudante> projetoEstudante) {
		this.projetoEstudante = projetoEstudante;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<TimeEstudante> getTimeEstudante() {
		return timeEstudante;
	}

	public void setTimeEstudante(List<TimeEstudante> timeEstudante) {
		this.timeEstudante = timeEstudante;
	}

	@Override
	public String toString() {
		return getNome();
	}

	public boolean equals(Object other) {
		return other instanceof Estudante && (getId() != null) ? getId()
				.equals(((Estudante) other).getId()) : (other == this);
	}

	public int hashCode() {
		return getId() != null ? this.getClass().hashCode() + getId().hashCode() : super
				.hashCode();
	}	
}
