package ifpr.utils;

public abstract class Paths {
	
	public static final String SISTEMA_LOAN = "http://localhost:8080/loan";
	
	
	public static final String ARQUIVOS = "/adm/arquivo/arquivo.ifpr";
	public static final String TECADM = "/adm/tec_adm.ifpr";
	public static final String TECESP = "/tec_adm/tec_esp.ifpr";
	public static final String COORDENADORES = "/tec_adm/coordenadorPea.ifpr";
	public static final String SECRETARIOS = "/tec_adm/secretario.ifpr";
	public static final String CAMPUS = "/adm/campus.ifpr";
	public static final String NOTICIAS = "/tec_adm/noticia.ifpr";
	public static final String MODALIDADES = "/adm/modalidade.ifpr";
	public static final String TIMES = "/tec_adm/time.ifpr";
	public static final String ESTUDANTES = "/tec_adm/estudante.ifpr";
	public static final String PROJETOS = "/tec_adm/projeto.ifpr";
	public static final String DELEGACOES = "/tec_adm/delegacao.ifpr";
	public static final String HORARIOASSISTENCIA = "/tec_adm/tec_coord/coord/horarioAssistencia.ifpr";
	public static final String CHAVES = "/sec/chave.ifpr";
	public static final String LOCAIS = "/adm/local.ifpr";
	public static final String EVENTOS = "/tec_esp/evento.ifpr";
	
	public static final String INDEX = "/index.ifpr";
	public static final String HOME = "/user/home.ifpr";
	
	public static final String LOAN_DOCS = "C:/home/loan_docs";
	
	public static final String PASTA_CRACHAS = LOAN_DOCS + "/cracha";
	public static final String MOLDURA_CRACHA = LOAN_DOCS + "/molduraCracha.png";
	public static final String LOGO_JOGOS = LOAN_DOCS + "/logoJIFPR.png";
	public static final String CRACHAS = "CrachasJIFPR.pdf";
	public static final String CAMINHO_CRACHAS = PASTA_CRACHAS + "/" + CRACHAS;
	public static final String CRACHAS_SECRETARIOS = PASTA_CRACHAS + "/CrachasSecretarios.pdf";
	
	public static final String PASTA_DECLARACOES =  LOAN_DOCS + "/declaracoes";
	public static final String MOLDURA_DECLARACAO = LOAN_DOCS + "/molduraDeclaracoes.png";
	public static final String ASSINATURA_DECLARAO = LOAN_DOCS + "/assinaturaDeclaracoes.png";
	public static final String ARQUIVO_DECLARACOES = "DeclaraçõesJIFPR.pdf";
	public static final String CAMINHO_ARQUIVO_DECLARACOES = PASTA_DECLARACOES + "/" + ARQUIVO_DECLARACOES;
	
	
	public static final String PASTA_RELATORIO = LOAN_DOCS + "/relatorioFinal";
	public static final String RELATORIO = "RelatórioFinalJIFPR.pdf";
	public static final String CAMINHO_RELATORIO = PASTA_RELATORIO + "/" + RELATORIO;
	
	
	public static final String PASTA_ARQUIVO_EVENTO =  LOAN_DOCS + "/evento";
	public static final String PASTA_ARQUIVO_RELATORIO = LOAN_DOCS + "/relatorios";
	
	public static final String CAMINHO_FOTO_PERFIL =  LOAN_DOCS + "/foto";
	public static final String FOTO_DEFAULT = CAMINHO_FOTO_PERFIL + "/default.png";
	
	public static final String PASTA_IMAGEM_NOTICIA =  LOAN_DOCS + "/noticia";

	public static final String PASTA_ASSISTENCIA = LOAN_DOCS + "/assinaturas";
}
