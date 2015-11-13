package ifpr.evento.model;



import ifpr.evento.Evento;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyEventoSorter implements Comparator<Evento> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyEventoSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Evento p1, Evento  p2) {
		try {
			Object value1 = Evento.class.getField(this.sortField).get(p1);
			Object value2 = Evento.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}