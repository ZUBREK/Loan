package ifpr.competicao.time.model;

import ifpr.competicao.time.Time;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyTimeSorter implements Comparator<Time> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyTimeSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Time p1, Time p2) {
		try {
			Object value1 = Time.class.getField(this.sortField).get(p1);
			Object value2 = Time.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}