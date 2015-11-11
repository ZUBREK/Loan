package ifpr.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "galleriaControllerMB")
@ViewScoped
public class GalleriaControllerMB {

	private List<String> images;

	@PostConstruct
	public void init() {
		images = new ArrayList<String>();
		for (int i = 1; i <= 3; i++) {
			images.add("nature" + i + ".png");
		}
	}

	public List<String> getImages() {
		return images;
	}
	
}
