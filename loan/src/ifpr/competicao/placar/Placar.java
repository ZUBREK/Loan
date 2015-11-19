package ifpr.competicao.placar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tbPlacar")
public class Placar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_placar")
	private Integer id;
	
	@Column(name="pontos_placar")
	private int pontos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getPontos() {
		return pontos;
	}

	public void setPontos(int resultado) {
		this.pontos = resultado;
	}	

}
