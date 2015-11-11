package ifpr.pessoa.estudante.model;

import ifpr.pessoa.estudante.Estudante;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyEstudanteSorter implements Comparator<Estudante> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyEstudanteSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Estudante p1, Estudante p2) {
		try {
			Object value1 = Estudante.class.getField(this.sortField).get(p1);
			Object value2 = Estudante.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}