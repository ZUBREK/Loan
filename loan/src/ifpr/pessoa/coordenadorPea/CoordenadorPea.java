package ifpr.pessoa.coordenadorPea;

import ifpr.campus.Campus;
import ifpr.pessoa.Pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbCoordenadorPEA")
public class CoordenadorPea extends Pessoa {

	
	@Column(name="siape_coordenador",length=20)
	private String siape;

	@JoinColumn(name = "id_campus", referencedColumnName = "id_campus")
	@ManyToOne()
	private Campus campus;

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}
	
	
	@Override
	public String toString() {
		return getNome();
	}
	
	
	public boolean equals(Object other) {
		return other instanceof CoordenadorPea && (getId() != null) ? getId()
				.equals(((CoordenadorPea) other).getId()) : (other == this);
	}

	public int hashCode() {
		return getId() != null ? this.getClass().hashCode() + getId().hashCode() : super
				.hashCode();
	}

}
