package ifpr.pessoa.coordenadorPea.horarioAssistencia;

import ifpr.arquivo.Arquivo;
import ifpr.pessoa.estudante.Estudante;

import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name = "tbHorarioReposicao")
public class HorarioAssistencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_horario")
	private Integer id;

	@Column(name = "materia_horario_assistencia", length = 50)
	private String materia;

	@JoinColumn(name = "id_foto_assinatura_professor", referencedColumnName = "id_arquivo")
	@ManyToOne(cascade = CascadeType.REMOVE)
	private Arquivo fotoAssinatura;

	@JoinColumn(name = "id_estudante", referencedColumnName = "id_pessoa")
	@ManyToOne()
	private Estudante estudante;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_hora_assistencia")
	private Date dataHora;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public Arquivo getFotoAssinatura() {
		return fotoAssinatura;
	}

	public void setFotoAssinatura(Arquivo fotoAssinatura) {
		this.fotoAssinatura = fotoAssinatura;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

}
