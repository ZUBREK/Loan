package ifpr.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "breadCrumbController")
@SessionScoped
public class BreadCrumbController {
	private MenuModel menuModel;
	private DefaultMenuItem item;
	private final int ARQUIVOS = 1;
	private final int TECADM = 2;
	private final int TECESP = 3;
	private final int COORDENADORES = 4;
	private final int SECRETARIOS = 5;
	private final int CAMPUS = 6;
	private final int NOTICIAS = 7;
	private final int MODALIDADES = 8;
	private final int TIMES = 9;
	private final int ESTUDANTES = 10;
	private final int PROJETOS = 11;
	private final int DELEGACOES = 12;
	private final int HORARIOASSISTENCIA = 13;
	private final int CHAVES = 14;

	public BreadCrumbController() {
		this.menuModel = new DefaultMenuModel();
		navigateHome();
	}

	public String navigateHome() {
		menuModel.getElements().clear();
		adicionarItem("Home", "/index.ifpr", "");
		adicionarItem("Meu Perfil", "/user/home.ifpr", "#{breadCrumbController.navigateHome}");

		return "home";
	}

	public String navigate(int opcao) {

		navigateHome();
		if (opcao == ARQUIVOS) {
			adicionarItem("Gerenciar - Arquivos", "/adm/arquivo/arquivo.loan", "#{breadCrumbController.navigate(1)}");
			return "usuarios";
		} else if (opcao == TECADM) {
			adicionarItem("Gerenciar - Tec. Administrativos", "/adm/tec_adm.loan",
					"#{breadCrumbController.navigate(2)}");
			return "turmas";
		} else if (opcao == TECESP) {
			adicionarItem("Gerenciar - Tec. Esportivos", "/tec_adm/tec_esp.loan",
					"#{breadCrumbController.navigate(3)}");
			return "usuarios";
		} else if (opcao == COORDENADORES) {
			adicionarItem("Gerenciar - Coordenadores", "/tec_adm/coordenadorPea.loan",
					"#{breadCrumbController.navigate(4)}");
			return "usuarios";
		} else if (opcao == SECRETARIOS) {
			adicionarItem("Gerenciar - Secretários", "/tec_adm/secretario.loan", "#{breadCrumbController.navigate(5)}");
			return "usuarios";
		} else if (opcao == CAMPUS) {
			adicionarItem("Gerenciar - Campus", "/adm/campus.loan", "#{breadCrumbController.navigate(6)}");
			return "usuarios";
		} else if (opcao == NOTICIAS) {
			adicionarItem("Gerenciar - Notícias", "/adm/noticia.loan", "#{breadCrumbController.navigate(7)}");
			return "usuarios";
		} else if (opcao == MODALIDADES) {
			adicionarItem("Gerenciar - Modalidades", "/adm/modalidade.loan", "#{breadCrumbController.navigate(8)}");
			return "usuarios";
		} else if (opcao == TIMES) {
			adicionarItem("Gerenciar - Times", "/tec_adm/time.loan", "#{breadCrumbController.navigate(9)}");
			return "usuarios";
		} else if (opcao == ESTUDANTES) {
			adicionarItem("Gerenciar - Estudantes", "/tec_adm/estudante.loan", "#{breadCrumbController.navigate(10)}");
			return "usuarios";
		} else if (opcao == PROJETOS) {
			adicionarItem("Gerenciar - Projetos PEA", "/tec_adm/projeto.loan", "#{breadCrumbController.navigate(11)}");
			return "usuarios";
		} else if (opcao == DELEGACOES) {
			adicionarItem("Gerenciar - Delegações", "/tec_adm/delegacao.loan", "#{breadCrumbController.navigate(12)}");
			return "usuarios";
		} else if (opcao == HORARIOASSISTENCIA) {
			adicionarItem("Gerenciar - Horários de Assistências", "/coord/horarioAssistencia.loan",
					"#{breadCrumbController.navigate(13)}");
			return "usuarios";
		}
		else if (opcao == CHAVES) {
			adicionarItem("Chaves", "/adm/chave.ifpr",
					"#{breadCrumbController.navigate(13)}");
			return "usuarios";
		}
		return "";
	}

	public void adicionarItem(String value, String url, String command) {
		item = new DefaultMenuItem();
		item.setValue(value);
		item.setUrl(url);
		item.setCommand(command);
		this.menuModel.addElement(item);
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}
}
