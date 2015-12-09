package ifpr.competicao.grupoChaves;

public enum TipoChaveamento {

	MATA_MATA("Mata-Mata"),
	PONTOS_CORRIDOS("Pontos Corridos"),
	CLASSIFICATORIO("Classificatório"),
	GRUPOS("Grupos");

	private String label;

	private TipoChaveamento(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
