package ifpr.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ifpr.arquivo.Arquivo;
import ifpr.noticia.Noticia;
import ifpr.noticia.dao.NoticiaDao;

@ManagedBean(name = "galleriaControllerMB")
@ViewScoped
public class GalleriaControllerMB {

	@ManagedProperty(value = "#{noticiaDao}")
	private NoticiaDao noticiaDao;

	private Noticia noticia;

	private List<Noticia> noticias;

	private StreamedContent imagemStreamed;

	private Arquivo arquivo;

	@PostConstruct
	public void init() {
		noticias = noticiaDao.listDesc();
	}

	public NoticiaDao getNoticiaDao() {
		return noticiaDao;
	}

	public void setNoticiaDao(NoticiaDao noticiaDao) {
		this.noticiaDao = noticiaDao;
	}

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}

	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	public StreamedContent getImagemStreamed() {
		String noticiaId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("id_noticia");
		if (noticiaId != null) {
			noticia = noticiaDao.findById(Integer.valueOf(noticiaId));
			arquivo = noticia.getFoto();
			InputStream stream;
			File file = new File(arquivo.getCaminho());
			if (file.exists()) {
				stream = null;
				try {
					stream = new FileInputStream(file);
					imagemStreamed = new DefaultStreamedContent(stream);//
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return imagemStreamed;
	}

}
