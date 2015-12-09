package ifpr.delegacao;

import java.util.ArrayList;
import java.util.List;

import ifpr.campus.Campus;
import ifpr.delegacao.delegacaoPessoa.DelegacaoPessoa;

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
@Table(name = "tbDelegacao")
public class Delegacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_delegacao")
	private Integer id;

	@Column(name = "nome_delegacao", length = 50)
	private String nome;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne()
	private Campus campus;

	@OneToMany
	@JoinColumn(name = "id_delegacao", referencedColumnName = "id_delegacao")
	private List<DelegacaoPessoa> delegacaoPessoas;

	@Column(name = "ano_delegacao")
	private int ano;

	public Delegacao() {
		delegacaoPessoas = new ArrayList<DelegacaoPessoa>();
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

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}

	public List<DelegacaoPessoa> getDelegacaoPessoas() {
		return delegacaoPessoas;
	}

	public void setDelegacaoPessoas(List<DelegacaoPessoa> delegacaoPessoas) {
		this.delegacaoPessoas = delegacaoPessoas;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}
}
