package ifpr.model;

import ifpr.modalidade.Modalidade;
import ifpr.modalidade.dao.ModalidadeDao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "menuModalidadeMB")
@ViewScoped
public class MenuModalidadesViewMB {
	private MenuModel model;
	
	@ManagedProperty(value = "#{modalidadeDao}")
	private ModalidadeDao modalidadeDao;
	@PostConstruct
    public void init() {
        model = new DefaultMenuModel();
         
        //First submenu
        DefaultSubMenu submenu = new DefaultSubMenu("Modalidades");
        List<Modalidade> modalidades = modalidadeDao.listarAlfabetica();
        if(!modalidades.isEmpty()){
        for (Modalidade modalidade : modalidades) {
        	DefaultMenuItem item = new DefaultMenuItem(modalidade.getNome());
            item.setUrl("#");
            submenu.addElement(item);
		}
        }
        else{
        	DefaultMenuItem item = new DefaultMenuItem("Não há modalidades cadastradas");
            item.setUrl("#");
            submenu.addElement(item);
        }
        DefaultMenuItem item = new DefaultMenuItem("Regulamento dos Jogos 2014");
        item.setUrl(" http://reitoria.ifpr.edu.br/wp-content/uploads/2014/10/REGULAMENTO_JIFPR_2014.pdf");
        submenu.addElement(item);
       
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
    
    
}
