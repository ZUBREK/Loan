package ifpr.noticia.model;

import ifpr.noticia.Noticia;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyNoticiaSorter implements Comparator<Noticia> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyNoticiaSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Noticia p1, Noticia  p2) {
		try {
			Object value1 = Noticia.class.getField(this.sortField).get(p1);
			Object value2 = Noticia.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}