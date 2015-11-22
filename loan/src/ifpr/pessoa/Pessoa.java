package ifpr.pessoa;

import ifpr.evento.eventoPessoa.EventoPessoa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbPessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pessoa")
	private Integer id;

	@Column(name = "nome_pessoa")
	private String nome;

	@Column(name = "username")
	private String login;

	@Column(name = "password")
	private String senha;

	@Column(name = "authority", length=50)
	@Enumerated(EnumType.STRING)
	private TipoPessoa tipo;

	@OneToMany
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	private List<EventoPessoa> eventoPessoas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoPessoa getTipo() {
		return tipo;
	}

	public void setTipo(TipoPessoa tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public List<EventoPessoa> getEventoPessoas() {
		return eventoPessoas;
	}

	public void setEventoPessoas(List<EventoPessoa> eventoPessoas) {
		this.eventoPessoas = eventoPessoas;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return getNome();
	}

	public boolean equals(Object other) {
		return other instanceof Pessoa && (getId() != null) ? getId()
				.equals(((Pessoa) other).getId()) : (other == this);
	}

	public int hashCode() {
		return getId() != null ? this.getClass().hashCode() + getId().hashCode() : super
				.hashCode();
	}

}
