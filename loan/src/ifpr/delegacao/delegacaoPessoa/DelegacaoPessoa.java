package ifpr.delegacao.delegacaoPessoa;

import ifpr.delegacao.Delegacao;
import ifpr.pessoa.Pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbPessoaDelegacao")
public class DelegacaoPessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_delegacao_pessoa")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_delegacao", referencedColumnName = "id_delegacao")
	private Delegacao delegacao;

	@ManyToOne
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
	private Pessoa pessoa;

	@Column(name="isChefe_delegacao")
	private boolean isChefe;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Delegacao getDelegacao() {
		return delegacao;
	}

	public void setDelegacao(Delegacao delegacao) {
		this.delegacao = delegacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public boolean isChefe() {
		return isChefe;
	}

	public void setChefe(boolean isChefe) {
		this.isChefe = isChefe;
	}

}
