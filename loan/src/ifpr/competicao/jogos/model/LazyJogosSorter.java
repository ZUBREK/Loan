package ifpr.competicao.jogos.model;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import ifpr.competicao.jogos.Jogos;

public class LazyJogosSorter implements Comparator<Jogos> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyJogosSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Jogos p1, Jogos p2) {
		try {
			Object value1 = Jogos.class.getField(this.sortField).get(p1);
			Object value2 = Jogos.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}