package ifpr.campus.model;

import ifpr.campus.Campus;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyCampusSorter implements Comparator<Campus> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyCampusSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Campus p1, Campus  p2) {
		try {
			Object value1 = Campus.class.getField(this.sortField).get(p1);
			Object value2 = Campus.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}