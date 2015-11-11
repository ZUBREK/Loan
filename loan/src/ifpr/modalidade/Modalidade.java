package ifpr.modalidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbModalidade")
public class Modalidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_modalidade")
	private Integer id;

	@Column(name = "nome_modalidade", length = 50)
	private String nome;

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
	
	@Override
	public String toString() {
		return nome;
	}

	public boolean equals(Object other) {
		return other instanceof Modalidade && (id != null) ? id
				.equals(((Modalidade) other).getId()) : (other == this);
	}

	public int hashCode() {
		return id != null ? this.getClass().hashCode() + id.hashCode() : super
				.hashCode();
	}

}
