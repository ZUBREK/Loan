package ifpr.projeto.projetoEstudante;

import ifpr.pessoa.estudante.Estudante;
import ifpr.projeto.Projeto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbProjetoEstudante")
public class ProjetoEstudante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_projetoEstudante")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_projeto", referencedColumnName = "id_projeto")
	private Projeto projeto;

	@ManyToOne
	@JoinColumn(name = "id_estudante", referencedColumnName = "id_pessoa")
	private Estudante estudante;

	public ProjetoEstudante() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

}
