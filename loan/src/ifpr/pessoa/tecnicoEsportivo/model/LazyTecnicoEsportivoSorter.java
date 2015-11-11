package ifpr.pessoa.tecnicoEsportivo.model;

import ifpr.pessoa.tecnicoEsportivo.TecnicoEsportivo;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyTecnicoEsportivoSorter implements Comparator<TecnicoEsportivo> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyTecnicoEsportivoSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(TecnicoEsportivo p1, TecnicoEsportivo p2) {
		try {
			Object value1 = TecnicoEsportivo.class.getField(this.sortField).get(p1);
			Object value2 = TecnicoEsportivo.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}