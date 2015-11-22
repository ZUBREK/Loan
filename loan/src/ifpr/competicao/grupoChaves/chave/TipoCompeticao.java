package ifpr.competicao.grupoChaves.chave;

public enum TipoCompeticao {

	MATA_MATA("Mata-Mata"),
	PONTOS_CORRIDOS("Pontos Corridos"),
	CLASSIFICATORIO("Classificatório");

	private String label;

	private TipoCompeticao(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
