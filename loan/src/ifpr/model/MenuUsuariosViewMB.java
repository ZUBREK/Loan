package ifpr.model;

import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "menuUsuariosMB")
@ViewScoped
public class MenuUsuariosViewMB {
	private MenuModel model;

	@ManagedProperty(value = "#{breadCrumbController}")
	private BreadCrumbController breadCrumbController;

	private LoginControllerMB loginControllerMB;

	private DefaultSubMenu submenu;
	private DefaultMenuItem item;
	private Pessoa pessoaLogada;
	FacesContext context;

	public MenuUsuariosViewMB() {
		context = FacesContext.getCurrentInstance();
		loginControllerMB = context.getApplication().evaluateExpressionGet(context, "#{loginControllerMB}",
				LoginControllerMB.class);
		pessoaLogada = loginControllerMB.getPessoaLogada();
	}

	@PostConstruct
	public void init() {
		model = new DefaultMenuModel();
		adicionarItem("Página Principal", "/index.ifpr", "", "home16", false, 0);
		adicionarItem("Meu Perfil", "/user/home.ifpr", "#{breadCrumbController.navigateHome}", "home16", false, 0);
		if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN)) {
			adicionarItem("Gerenciar - Arquivos", "/adm/arquivo/arquivo.ifpr", "#{breadCrumbController.navigate(1)}",
					"attach16", false, 1);

			adicionarItem("Gerenciar - Campus", "/adm/campus.ifpr", "#{breadCrumbController.navigate(6)}", "attach16",
					false, 6);

			adicionarItem("Gerenciar - Notícias", "/adm/noticia.ifpr", "#{breadCrumbController.navigate(7)}",
					"attach16", false, 7);

			adicionarItem("Gerenciar - Projetos PEA", "/tec_adm/projeto.ifpr", "#{breadCrumbController.navigate(11)}",
					"attach16", false, 11);

			adicionarItem("Gerenciar - Horários de Assistências", "/adm/noticia.ifpr",
					"#{breadCrumbController.navigate(13)}", "attach16", false, 13);
			adicionarItensAdm();

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_COORDENADOR)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_SECRETARIO)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {

		}

	}

	private void adicionarItensAdm() {
		submenu = new DefaultSubMenu("Gerenciar - Usuários");
		adicionarItem("Tec. Administrativos", "/adm/tec_adm.ifpr", "#{breadCrumbController.navigate(2)}", "user16",
				true, 2);

		adicionarItem("Tec. Esportivos", "/tec_adm/tec_esp.ifpr", "#{breadCrumbController.navigate(3)}", "user16", true,
				3);

		adicionarItem("Coordenadores", "/tec_adm/coordenadorPea.ifpr", "#{breadCrumbController.navigate(4)}", "user16",
				true, 4);

		adicionarItem("Secretários", "/tec_adm/secretario.ifpr", "#{breadCrumbController.navigate(5)}", "user16", true,
				5);

		adicionarItem("Estudantes", "/tec_adm/estudante.ifpr", "#{breadCrumbController.navigate(10)}", "user16", true,
				10);
		model.addElement(submenu);
		submenu = new DefaultSubMenu("Gerenciar - Competição");

		adicionarItem("Modalidades", "/adm/modalidade.ifpr", "#{breadCrumbController.navigate(8)}", "user16", true, 8);

		adicionarItem("Times", "/tec_adm/time.ifpr", "#{breadCrumbController.navigate(9)}", "user16", true, 9);

		adicionarItem("Delegações", "/tec_adm/delegacao.ifpr", "#{breadCrumbController.navigate(12)}", "attach16", true,
				12);

		adicionarItem("Chaves", "/adm/chave.ifpr", "#{breadCrumbController.navigate(13)}", "attach16", true, 14);

		adicionarItem("Locais", "/adm/local.ifpr",
				"", "attach16", false, 15);
		model.addElement(submenu);
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public void adicionarItem(String value, String url, String command, String icon, boolean inSubmenu,
			int numeroItem) {
		item = new DefaultMenuItem();
		item.setValue(value);
		item.setUrl(url);
		// item.setCommand(command);
		item.setIcon(icon);
		String caminhoView = context.getViewRoot().getViewId();

		caminhoView = caminhoView.substring(0, caminhoView.lastIndexOf("."));
		String caminhoViewItem = url.substring(0, url.lastIndexOf("."));

		if (caminhoView.equals(caminhoViewItem)) {
			item.setStyleClass("menuItemAtual");
			if (numeroItem != 0) {
				breadCrumbController.navigate(numeroItem);
			} else {
				breadCrumbController.navigateHome();
			}

		}

		if (inSubmenu) {
			submenu.addElement(item);
		} else {
			model.addElement(item);
		}
		item.setUpdate(":breadCrumb");
	}

	public BreadCrumbController getBreadCrumbController() {
		return breadCrumbController;
	}

	public void setBreadCrumbController(BreadCrumbController breadCrumbController) {
		this.breadCrumbController = breadCrumbController;
	}

	public LoginControllerMB getLoginControllerMB() {
		return loginControllerMB;
	}

	public void setLoginControllerMB(LoginControllerMB loginControllerMB) {
		this.loginControllerMB = loginControllerMB;
	}

	public DefaultSubMenu getSubmenu() {
		return submenu;
	}

	public void setSubmenu(DefaultSubMenu submenu) {
		this.submenu = submenu;
	}

	public DefaultMenuItem getItem() {
		return item;
	}

	public void setItem(DefaultMenuItem item) {
		this.item = item;
	}

}
