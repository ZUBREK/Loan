package ifpr.modalidade.model;

import ifpr.modalidade.Modalidade;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyModalidadeSorter implements Comparator<Modalidade> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyModalidadeSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Modalidade p1, Modalidade  p2) {
		try {
			Object value1 = Modalidade.class.getField(this.sortField).get(p1);
			Object value2 = Modalidade.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}