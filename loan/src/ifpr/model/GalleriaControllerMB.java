package ifpr.model;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

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
	
	private List<Noticia> noticiasGalleria;

	private Arquivo arquivo;

	@PostConstruct
	public void init() {
		noticiasGalleria = new ArrayList<>();
		noticias = noticiaDao.listDesc();
		noticiasGalleria = noticias;
		/*for (Noticia noticia : noticias) {
			arquivo = noticia.getFoto();
			Path path = Paths.get(arquivo.getCaminho());
			FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
            String nomeArquivo = noticia.getId().toString() + ".jpg";
            String arquivo = scontext.getRealPath("/temp/" + nomeArquivo);
            try {
				criaArquivo(Files.readAllBytes(path), arquivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

	public void criaArquivo(byte[] bytes, String arquivo) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
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

	public List<Noticia> getNoticiasGalleria() {
		return noticiasGalleria;
	}

	public void setNoticiasGalleria(List<Noticia> noticiasGalleria) {
		this.noticiasGalleria = noticiasGalleria;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

}
