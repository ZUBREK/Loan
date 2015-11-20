package ifpr.competicao.time.pontos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbPontosTime")
public class PontosTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pontos_time")
	private Integer id;

	@Column(name = "vitorias_time")
	private int vitorias;

	@Column(name = "derrotas_time")
	private int derrotas;

	@Column(name = "empates_time")
	private int empates;
	
	@Column(name = "classificacao_time")
	private int classificacao;
	
	@Column(name="pontos_time")
	private int pontos;
	
	public PontosTime() {
		vitorias = 0;
		derrotas = 0;
		empates = 0;
		pontos = 0;
		classificacao = 0;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getVitorias() {
		return vitorias;
	}

	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}

	public int getDerrotas() {
		return derrotas;
	}

	public void setDerrotas(int derrotas) {
		this.derrotas = derrotas;
	}

	public int getEmpates() {
		return empates;
	}

	public void setEmpates(int empates) {
		this.empates = empates;
	}

	public int getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(int classificacao) {
		this.classificacao = classificacao;
	}

	public int getPontos() {
		return pontos;
	}

	public void setPontos(int pontos) {
		this.pontos = pontos;
	}

}
