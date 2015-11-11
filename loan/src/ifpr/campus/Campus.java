package ifpr.campus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbCampus")
public class Campus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_campus")
	private Integer id;

	@Column(name = "cidade_campus", length = 50)
	private String cidade;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@Override
	public String toString() {
		return cidade;
	}

	public boolean equals(Object other) {
		return other instanceof Campus && (id != null) ? id
				.equals(((Campus) other).getId()) : (other == this);
	}

	public int hashCode() {
		return id != null ? this.getClass().hashCode() + id.hashCode() : super
				.hashCode();
	}

}
