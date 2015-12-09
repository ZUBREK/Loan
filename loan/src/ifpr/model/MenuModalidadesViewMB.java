package ifpr.model;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import ifpr.competicao.jogos.Jogos;
import ifpr.competicao.jogos.dao.JogosDao;
import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;

@ManagedBean(name = "menuModalidadeMB")
@ViewScoped
public class MenuModalidadesViewMB {
	private MenuModel model;

	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;

	@ManagedProperty(value = "#{jogosDao}")
	private JogosDao jogosDao;

	@PostConstruct
	public void init() {
		model = new DefaultMenuModel();

		DefaultSubMenu submenu = new DefaultSubMenu("Modalidades");
		List<Modalidade> modalidades = modalidadeDao.listarAlfabetica();
		List<Jogos> edicoesJogos = jogosDao.listDesc();
		if (!modalidades.isEmpty()) {
			for (Modalidade modalidade : modalidades) {
				DefaultMenuItem item = new DefaultMenuItem(modalidade.getNome());
				item.setUrl("#");
				submenu.addElement(item);
			}
		} else {
			DefaultMenuItem item = new DefaultMenuItem("Não há modalidades cadastradas");
			item.setUrl("#");
			submenu.addElement(item);
		}
		if (!modalidades.isEmpty()) {
			for (Jogos jogos : edicoesJogos) {
				DefaultMenuItem item = new DefaultMenuItem("Regulamento dos Jogos " + jogos.getAno());
				item.setUrl(jogos.getLink());
				submenu.addElement(item);
			}
		} else {
			DefaultMenuItem item = new DefaultMenuItem("Não há edições de jogos cadastradas!");
			item.setUrl("#");
			submenu.addElement(item);
		}

		model.addElement(submenu);

	}

	public MenuModel getModel() {
		return model;
	}

	public ModalidadeDao getModalidadeDao() {
		return modalidadeDao;
	}

	public void setModalidadeDao(ModalidadeDao modalidadeDao) {
		this.modalidadeDao = modalidadeDao;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public JogosDao getJogosDao() {
		return jogosDao;
	}

	public void setJogosDao(JogosDao jogosDao) {
		this.jogosDao = jogosDao;
	}
}
