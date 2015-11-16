package ifpr.competicao.partida.local.model;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import ifpr.competicao.partida.local.Local;

public class LazyLocalSorter implements Comparator<Local> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyLocalSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Local p1, Local  p2) {
		try {
			Object value1 = Local.class.getField(this.sortField).get(p1);
			Object value2 = Local.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}