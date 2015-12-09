package ifpr.pessoa;

public enum TipoPessoa {

	ROLE_ADMIN("Administrador", 1), 
	ROLE_TEC_ADM("Técnico Administrativo", 2), 
	ROLE_SECRETARIO("Secret�rio", 3), 
	ROLE_TEC_ESP("Técnico Esportivo", 4), 
	ROLE_COORDENADOR("Coordenador PEA", 5), 
	ROLE_ESTUDANTE("Estudante", 6),
	ROLE_TEC_COORD("Técnico Esportivo & Coordenador",7);

	private String label;

	private int acesso;

	public int getAcesso() {
		return acesso;
	}

	public void setAcesso(int acesso) {
		this.acesso = acesso;
	}

	private TipoPessoa(String label, int acesso) {
		this.label = label;
		this.acesso = acesso;
	}

	public String getLabel() {
		return this.label;
	}
}
