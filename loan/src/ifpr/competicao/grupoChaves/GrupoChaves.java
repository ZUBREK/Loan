package ifpr.competicao.grupoChaves;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ifpr.competicao.grupoChaves.chave.Chave;
import ifpr.modalidade.Modalidade;

@Entity
@Table(name = "tbGrupoChaves")
public class GrupoChaves {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_grupo_chaves")
	private Integer id;

	@Column(name = "nome_chave")
	private String nome;

	@JoinColumn(name = "id_modalidade", referencedColumnName = "id_modalidade")
	@ManyToOne()
	private Modalidade modalidade;

	@JoinColumn(name = "id_grupo_chaves", referencedColumnName = "id_grupo_chaves")
	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Chave> chaves;
	
	@Column(name = "tipo_grupo_chave")
	@Enumerated(EnumType.STRING)
	private TipoChaveamento tipo;

	public GrupoChaves() {
		chaves = new ArrayList<Chave>();
	}
	
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

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public List<Chave> getChaves() {
		return chaves;
	}

	public void setChaves(List<Chave> chaves) {
		this.chaves = chaves;
	}

	public TipoChaveamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoChaveamento tipo) {
		this.tipo = tipo;
	}

}
