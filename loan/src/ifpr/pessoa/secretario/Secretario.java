package ifpr.pessoa.secretario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ifpr.pessoa.Pessoa;

@Entity
@Table(name = "tbSecretario")
public class Secretario extends Pessoa {

	@Column(name = "rg_secretario")
	private String rg;

	@Column(name = "cpf_secretario")
	private String cpf;

	@Column(name = "telefone_secretario")
	private String telefone;

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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

	@Override
	public String toString() {
		return getNome();
	}

	public boolean equals(Object other) {
		return other instanceof Secretario && (getId() != null) ? getId().equals(((Secretario) other).getId())
				: (other == this);
	}

	public int hashCode() {
		return getId() != null ? this.getClass().hashCode() + getId().hashCode() : super.hashCode();
	}

}
