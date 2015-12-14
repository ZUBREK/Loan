package ifpr.arquivo;

public enum TipoArquivo {

	LOGO_IFPR("Logo do IFPR"), 
	LOGO_JIFPR("Logo dos JIFPR"), 
	MOLDURA_CRACHA("Moldura do Crachá"), 
	MOLDURA_DECLARACAO("Moldura da Declaração"), 
	ASSINATURA_DECLARACAO("Assinatura da Declaração");

	private String label;

	private TipoArquivo(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
