package ifpr.noticia.mb;

import ifpr.noticia.Noticia;
import ifpr.noticia.dao.NoticiaDao;
import ifpr.noticia.model.NoticiaLazyDataModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "noticiaMB")
@ViewScoped
public class NoticiaMB {

	private Noticia noticia;

	@ManagedProperty(value = "#{noticiaDao}")
	private NoticiaDao noticiaDao;

	@ManagedProperty(value = "#{noticiaLazyDataModel}")
	private NoticiaLazyDataModel noticiaLazyDataModel;

	private List<Noticia> noticiaFiltered;

	public NoticiaMB() {

		noticiaFiltered = new ArrayList<Noticia>();
	}

	public void criar() {
		noticia = new Noticia();

	}

	public void remover() {
		noticiaDao.remover(noticia);
	}

	public void cancelar() {
		noticia = null;
	}

	public void salvar() {
		if (noticia.getId() != null) {
			noticia.setData(new Date());
			noticiaDao.update(noticia);
		} else {
			noticia.setData(new Date());
			noticiaDao.salvar(noticia);
		}
	}

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}

	public NoticiaDao getNoticiaDao() {
		return noticiaDao;
	}

	public void setNoticiaDao(NoticiaDao noticiaDao) {
		this.noticiaDao = noticiaDao;
	}

	public NoticiaLazyDataModel getNoticiaLazyDataModel() {
		return noticiaLazyDataModel;
	}

	public void setNoticiaLazyDataModel(NoticiaLazyDataModel noticiaLazyDataModel) {
		this.noticiaLazyDataModel = noticiaLazyDataModel;
	}

	public List<Noticia> getNoticiaFiltered() {
		return noticiaFiltered;
	}

	public void setNoticiaFiltered(List<Noticia> noticiaFiltered) {
		this.noticiaFiltered = noticiaFiltered;
	}


}
