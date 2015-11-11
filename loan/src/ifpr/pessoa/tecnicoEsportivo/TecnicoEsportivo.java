package ifpr.pessoa.tecnicoEsportivo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.campus.Campus;
import ifpr.pessoa.Pessoa;

@Entity
@Table(name = "tbTecnicoEsportivo")
public class TecnicoEsportivo extends Pessoa {

	@Column(name = "siape_tec_esp", length = 20)
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
		return other instanceof TecnicoEsportivo && (getId() != null) ? getId()
				.equals(((TecnicoEsportivo) other).getId()) : (other == this);
	}

	public int hashCode() {
		return getId() != null ? this.getClass().hashCode() + getId().hashCode() : super
				.hashCode();
	}
}
