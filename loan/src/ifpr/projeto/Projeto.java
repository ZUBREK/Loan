package ifpr.projeto;

import ifpr.campus.Campus;
import ifpr.modalidade.Modalidade;
import ifpr.pessoa.Pessoa;
import ifpr.projeto.projetoEstudante.ProjetoEstudante;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "tbProjeto")
public class Projeto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_projeto")
	private Integer id;

	@Column(name = "nome_projeto")
	private String nome;

	@OneToOne
	private Pessoa coordenador;

	@JoinColumn(name = "id_modalidade", referencedColumnName = "id_modalidade")
	@ManyToOne()
	private Modalidade modalidade;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne()
	private Campus campus;
	
	@JoinColumn(name = "id_projeto", referencedColumnName = "id_projeto")
	@OneToMany()
	private List<ProjetoEstudante> projetoEstudante;

	public Projeto(){
		projetoEstudante = new ArrayList<ProjetoEstudante>();
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

	public Pessoa getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(Pessoa coordenador) {
		this.coordenador = coordenador;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
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
	

}
