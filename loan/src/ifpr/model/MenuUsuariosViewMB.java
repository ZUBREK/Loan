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
		adicionarItem("Página Principal", "/index.ifpr", "home16", false);
		adicionarItem("Meu Perfil", "/user/home.ifpr", "home16", false);
		if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN)) {
			adicionarItem("Gerenciar - Arquivos", "/adm/arquivo/arquivo.ifpr", "attach16", false);

			adicionarItem("Gerenciar - Campus", "/adm/campus.ifpr", "attach16", false);

			adicionarItem("Gerenciar - Notícias", "/adm/noticia.ifpr", "attach16", false);

			adicionarItem("Gerenciar - Projetos PEA", "/tec_adm/projeto.ifpr", "attach16", false);

			adicionarItem("Gerenciar - Horários de Assistências", "/adm/noticia.ifpr", "attach16", false);
			adicionarItensAdm();

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_COORDENADOR)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_SECRETARIO)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {

		}

	}

	private void adicionarItensAdm() {
		submenu = new DefaultSubMenu("Gerenciar - Usuários");
		adicionarItem("Tec. Administrativos", "/adm/tec_adm.ifpr", "user16", true);

		adicionarItem("Tec. Esportivos", "/tec_adm/tec_esp.ifpr", "user16", true);

		adicionarItem("Coordenadores", "/tec_adm/coordenadorPea.ifpr", "user16", true);

		adicionarItem("Secretários", "/tec_adm/secretario.ifpr", "user16", true);

		adicionarItem("Estudantes", "/tec_adm/estudante.ifpr", "user16", true);
		model.addElement(submenu);
		submenu = new DefaultSubMenu("Gerenciar - Competição");

		adicionarItem("Modalidades", "/adm/modalidade.ifpr", "user16", true);

		adicionarItem("Times", "/tec_adm/time.ifpr", "user16", true);

		adicionarItem("Delegações", "/tec_adm/delegacao.ifpr", "attach16", true);

		adicionarItem("Chaves", "/adm/chave.ifpr", "attach16", true);

		adicionarItem("Locais", "/adm/local.ifpr", "attach16", true);
		
		model.addElement(submenu);
	}

	public void adicionarItem(String value, String url, String icon, boolean colocarNoSubmenu) {
		item = new DefaultMenuItem();
		item.setValue(value);
		item.setUrl(url);
		item.setIcon(icon);
		String caminhoView = context.getViewRoot().getViewId();

		caminhoView = caminhoView.substring(0, caminhoView.lastIndexOf("."));
		String caminhoViewItem = url.substring(0, url.lastIndexOf("."));

		if (caminhoView.equals(caminhoViewItem)) {
			item.setStyleClass("menuItemAtual");
		}

		if (colocarNoSubmenu) {
			submenu.addElement(item);
		} else {
			model.addElement(item);
		}
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
	
	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

}
