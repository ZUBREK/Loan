package ifpr.competicao.chave.model;

import ifpr.competicao.chave.Chave;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyChaveSorter implements Comparator<Chave> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyChaveSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Chave p1, Chave  p2) {
		try {
			Object value1 = Chave.class.getField(this.sortField).get(p1);
			Object value2 = Chave.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}