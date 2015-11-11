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
	
	//Daew meus amigos
	
	public BreadCrumbController() {
		this.menuModel = new DefaultMenuModel();
		navigateHome();
	}

	public String navigateHome() {
		menuModel.getElements().clear();
		adicionarItem("Home", "/index.ifpr", "");
		adicionarItem("Meu Perfil", "/user/home.ifpr",
				"#{breadCrumbController.navigateHome}");

		return "home";
	}

	public String navigate(int opcao) {

		navigateHome();
		if (opcao == ARQUIVOS) {
			adicionarItem("Gerenciar - Arquivos", "/adm/arquivo/arquivo.ifpr",
					"#{breadCrumbController.navigate(1)}");
			return "usuarios";
		} else if (opcao == TECADM) {
			adicionarItem("Gerenciar - Tec. Administrativos", "/adm/tec_adm.ifpr",
					"#{breadCrumbController.navigate(2)}");
			return "turmas";
		} else if (opcao == TECESP) {
			adicionarItem("Gerenciar - Tec. Esportivos", "/tec_adm/tec_esp.ifpr",
					"#{breadCrumbController.navigate(3)}");
			return "usuarios";
		} else if (opcao == COORDENADORES) {
			adicionarItem("Gerenciar - Coordenadores", "/tec_adm/coordenadorPea.ifpr",
					"#{breadCrumbController.navigate(4)}");
			return "usuarios";
		}
		else if (opcao == SECRETARIOS) {
			adicionarItem("Gerenciar - Secretários", "/tec_adm/secretario.ifpr",
					"#{breadCrumbController.navigate(5)}");
			return "usuarios";
		}
		else if (opcao == CAMPUS) {
			adicionarItem("Gerenciar - Campus", "/adm/campus.ifpr",
					"#{breadCrumbController.navigate(6)}");
			return "usuarios";
		}
		else if (opcao == NOTICIAS) {
			adicionarItem("Gerenciar - Notícias", "/adm/noticia.ifpr",
					"#{breadCrumbController.navigate(7)}");
			return "usuarios";
		}
		else if (opcao == MODALIDADES) {
			adicionarItem("Gerenciar - Modalidades", "/adm/modalidade.ifpr",
					"#{breadCrumbController.navigate(8)}");
			return "usuarios";
		}
		else if (opcao == TIMES) {
			adicionarItem("Gerenciar - Times", "/tec_adm/time.ifpr",
					"#{breadCrumbController.navigate(9)}");
			return "usuarios";
		}
		else if (opcao == ESTUDANTES) {
			adicionarItem("Gerenciar - Estudantes", "/tec_adm/estudante.ifpr",
					"#{breadCrumbController.navigate(10)}");
			return "usuarios";
		}
		else if (opcao == PROJETOS) {
			adicionarItem("Gerenciar - Projetos PEA", "/tec_adm/projeto.ifpr",
					"#{breadCrumbController.navigate(11)}");
			return "usuarios";
		}
		else if (opcao == DELEGACOES) {
			adicionarItem("Gerenciar - Delegações", "/tec_adm/delegacao.ifpr",
					"#{breadCrumbController.navigate(12)}");
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
