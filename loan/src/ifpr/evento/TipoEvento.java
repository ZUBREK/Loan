package ifpr.evento;


public enum TipoEvento {

	REFEICAO("Refeição", 1), TREINO("Treino Esportivo", 2), MAPAMODALIDADE(
			"Mapa de Modalidades", 3);

	private String label;

	private int acesso;

	public int getAcesso() {
		return acesso;
	}

	public void setAcesso(int acesso) {
		this.acesso = acesso;
	}

	private TipoEvento(String label, int acesso) {
		this.label = label;
		this.acesso = acesso;
	}

	public String getLabel() {
		return this.label;
	}
}

