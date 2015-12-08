package ifpr.competicao.jogos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ifpr.competicao.grupoChaves.GrupoChaves;

@Entity
@Table(name = "tbJogos")
public class Jogos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jogos")
	private Integer id;
	
	@Column(name = "ano_jogos")
	private int ano;
	
	@Column(name = "link_regulamento")
	private String link;
	
	@JoinColumn(name = "id_jogos", referencedColumnName = "id_jogos")
	@OneToMany(cascade = CascadeType.REMOVE)
	private List<GrupoChaves> grupoChaves;

	public Jogos(){
		grupoChaves = new ArrayList<>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<GrupoChaves> getGrupoChaves() {
		return grupoChaves;
	}

	public void setGrupoChaves(List<GrupoChaves> grupoChaves) {
		this.grupoChaves = grupoChaves;
	}
	
}
