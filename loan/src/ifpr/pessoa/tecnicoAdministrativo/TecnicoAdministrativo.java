package ifpr.pessoa.tecnicoAdministrativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ifpr.campus.Campus;
import ifpr.pessoa.Pessoa;

@Entity
@Table(name = "tbTecnicoAdm")
public class TecnicoAdministrativo extends Pessoa {

	@Column(name = "siape_tecnicoAdm", length = 20)
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
}
