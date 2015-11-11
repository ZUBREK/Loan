package ifpr.pessoa.secretario.model;

import ifpr.pessoa.secretario.Secretario;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazySecretarioSorter implements Comparator<Secretario> {

	private String sortField;

	private SortOrder sortOrder;

	public LazySecretarioSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Secretario p1, Secretario  p2) {
		try {
			Object value1 = Secretario.class.getField(this.sortField).get(p1);
			Object value2 = Secretario.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}