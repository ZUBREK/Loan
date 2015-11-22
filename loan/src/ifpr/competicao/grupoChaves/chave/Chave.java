package ifpr.competicao.grupoChaves.chave;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ifpr.competicao.partida.Partida;

@Entity
@Table(name = "tbChave")
public class Chave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_chave")
	private Integer id;

	@Column(name = "nome_chave")
	private String nome;

	@Column(name = "tipo_chave")
	@Enumerated(EnumType.STRING)
	private TipoCompeticao tipo;

	@JoinColumn(name = "id_chave", referencedColumnName = "id_chave")
	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Partida> partidas;

	private boolean isAcabado;

	public Chave() {
		partidas = new ArrayList<>();
		isAcabado = false;
	}

	public boolean isAcabado() {
		return isAcabado;
	}



	public void setAcabado(boolean isAcabado) {
		this.isAcabado = isAcabado;
	}



	public List<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
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

	public TipoCompeticao getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompeticao tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
