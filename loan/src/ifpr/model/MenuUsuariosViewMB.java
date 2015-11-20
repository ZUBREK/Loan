package ifpr.model;

import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;
import ifpr.utils.Paths;

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
		adicionarItem("P�gina Principal", Paths.INDEX, "home16", false);
		adicionarItem("Meu Perfil", Paths.HOME, "home16", false);
		if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ADMIN)) {
			adicionarItem("Gerenciar - Arquivos", Paths.ARQUIVOS, "attach16", false);

			adicionarItem("Gerenciar - Campus", Paths.CAMPUS, "attach16", false);

			adicionarItem("Gerenciar - Not�cias", Paths.NOTICIAS, "attach16", false);

			adicionarItem("Gerenciar - Projetos PEA", Paths.PROJETOS, "attach16", false);

			adicionarItem("Gerenciar - Hor�rios de Assist�ncias", Paths.HORARIOASSISTENCIA, "attach16", false);
			adicionarItensAdm();

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_COORDENADOR)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_SECRETARIO)) {

		} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_ESTUDANTE)) {

		}
		else if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ADM)){
			
		}
		else if(pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_COORD)){
			
		}

	}

	private void adicionarItensAdm() {
		submenu = new DefaultSubMenu("Gerenciar - Usu�rios");
		adicionarItem("Tec. Administrativos", Paths.TECADM, "user16", true);

		adicionarItem("Tec. Esportivos", Paths.TECESP, "user16", true);

		adicionarItem("Coordenadores", Paths.COORDENADORES, "user16", true);

		adicionarItem("Secret�rios", Paths.SECRETARIOS, "user16", true);

		adicionarItem("Estudantes", Paths.ESTUDANTES, "user16", true);
		model.addElement(submenu);
		submenu = new DefaultSubMenu("Gerenciar - Competi��o");

		adicionarItem("Modalidades", Paths.MODALIDADES, "user16", true);

		adicionarItem("Times", Paths.TIMES, "user16", true);

		adicionarItem("Delega��es", Paths.DELEGACOES, "attach16", true);

		adicionarItem("Chaves", Paths.CHAVES, "attach16", true);

		adicionarItem("Locais", Paths.LOCAIS, "attach16", true);

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
