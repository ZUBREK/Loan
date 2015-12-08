package ifpr.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import ifpr.utils.Paths;

@ManagedBean(name = "breadCrumbController")
@ViewScoped
public class BreadCrumbController {
	private MenuModel menuModel;
	private DefaultMenuItem item;

	FacesContext context;

	public BreadCrumbController() {
		this.menuModel = new DefaultMenuModel();
		context = FacesContext.getCurrentInstance();
		String caminhoView = context.getViewRoot().getViewId();
		caminhoView = caminhoView.substring(0, caminhoView.lastIndexOf("."));
		navigateHome();

		if (caminhoView.equals((Paths.ARQUIVOS).substring(0, Paths.ARQUIVOS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Arquivos", Paths.ARQUIVOS);
		} else if (caminhoView.equals((Paths.CAMPUS).substring(0, Paths.CAMPUS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Campus", Paths.CAMPUS);

		} else if (caminhoView.equals((Paths.NOTICIAS).substring(0, Paths.NOTICIAS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Notícias", Paths.NOTICIAS);

		} else if (caminhoView.equals((Paths.PROJETOS).substring(0, Paths.PROJETOS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Projetos PEA", Paths.PROJETOS);

		} else if (caminhoView
				.equals((Paths.HORARIOASSISTENCIA).substring(0, Paths.HORARIOASSISTENCIA.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Horários de Assistências", Paths.HORARIOASSISTENCIA);

		} else if (caminhoView.equals((Paths.TECADM).substring(0, Paths.TECADM.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Téc. Administrativos", Paths.TECADM);

		} else if (caminhoView.equals((Paths.TECESP).substring(0, Paths.TECESP.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Téc. Esportivos", Paths.TECESP);

		} else if (caminhoView.equals((Paths.COORDENADORES).substring(0, Paths.COORDENADORES.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Coordenadores", Paths.COORDENADORES);

		} else if (caminhoView.equals((Paths.SECRETARIOS).substring(0, Paths.SECRETARIOS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Secretários", Paths.SECRETARIOS);

		} else if (caminhoView.equals((Paths.ESTUDANTES).substring(0, Paths.ESTUDANTES.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Estudantes", Paths.ESTUDANTES);

		} else if (caminhoView.equals((Paths.MODALIDADES).substring(0, Paths.MODALIDADES.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Modalidades", Paths.MODALIDADES);

		} else if (caminhoView.equals((Paths.TIMES).substring(0, Paths.TIMES.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Times", Paths.TIMES);

		} else if (caminhoView.equals((Paths.DELEGACOES).substring(0, Paths.DELEGACOES.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Delegações", Paths.DELEGACOES);

		} else if (caminhoView.equals((Paths.CHAVES).substring(0, Paths.CHAVES.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Chaves", Paths.CHAVES);

		} else if (caminhoView.equals((Paths.LOCAIS).substring(0, Paths.LOCAIS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Locais", Paths.LOCAIS);

		} else if (caminhoView.equals((Paths.EVENTOS).substring(0, Paths.EVENTOS.lastIndexOf(".")))) {
			adicionarItem("Gerenciar - Eventos", Paths.EVENTOS);
		}

	}

	public String navigateHome() {
		menuModel.getElements().clear();
		adicionarItem("Home", "/index.ifpr");
		adicionarItem("Meu Perfil", "/user/home.ifpr");

		return "home";
	}

	public void adicionarItem(String value, String url) {
		item = new DefaultMenuItem();
		item.setValue(value);
		item.setUrl(url);
		this.menuModel.addElement(item);
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}
}
