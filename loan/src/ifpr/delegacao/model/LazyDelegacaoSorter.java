package ifpr.delegacao.model;

import ifpr.delegacao.Delegacao;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyDelegacaoSorter implements Comparator<Delegacao> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyDelegacaoSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Delegacao p1, Delegacao p2) {
		try {
			Object value1 = Delegacao.class.getField(this.sortField).get(p1);
			Object value2 = Delegacao.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}