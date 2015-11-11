package ifpr.projeto.model;

import ifpr.projeto.Projeto;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyProjetoSorter implements Comparator<Projeto> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyProjetoSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Projeto p1, Projeto p2) {
		try {
			Object value1 = Projeto.class.getField(this.sortField).get(p1);
			Object value2 = Projeto.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}