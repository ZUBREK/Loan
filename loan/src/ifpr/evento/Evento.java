package ifpr.evento;

import ifpr.evento.eventoPessoa.EventoPessoa;
import ifpr.pessoa.Pessoa;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


//actionListener="#{buttonView.buttonAction}"


@Entity
@Table(name = "tbEvento")
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_evento")
	private Integer id;

	@Column(name = "nome_evento", length = 50)
	private String nome;

	@Column(name = "descricao_evento", length = 100)
	private String descricao;

	@Column(name = "tipo_evento")
	@Enumerated(EnumType.STRING)
	private TipoEvento tipo;

	@OneToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa responsavel;

	@OneToMany
	@JoinColumn(name = "id_evento", referencedColumnName = "id_evento")
	private List<EventoPessoa> eventoPessoas;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_inicial_evento")
	private Date dataHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_hora_final_evento")
	private Date dataHoraFinal;

	public Pessoa getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Pessoa responsavel) {
		this.responsavel = responsavel;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(Date dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public Date getDataHoraFinal() {
		return dataHoraFinal;
	}

	public void setDataHoraFinal(Date dataHoraFinal) {
		this.dataHoraFinal = dataHoraFinal;
	}

	public List<EventoPessoa> getEventoPessoas() {
		return eventoPessoas;
	}

	public void setEventoPessoas(List<EventoPessoa> eventoPessoas) {
		this.eventoPessoas = eventoPessoas;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public void setTipo(TipoEvento tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return getNome();
	}

}
