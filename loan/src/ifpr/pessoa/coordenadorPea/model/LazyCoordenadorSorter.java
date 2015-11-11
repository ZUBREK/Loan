package ifpr.pessoa.coordenadorPea.model;

import ifpr.pessoa.coordenadorPea.CoordenadorPea;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyCoordenadorSorter implements Comparator<CoordenadorPea> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyCoordenadorSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(CoordenadorPea p1, CoordenadorPea  p2) {
		try {
			Object value1 = CoordenadorPea.class.getField(this.sortField).get(p1);
			Object value2 = CoordenadorPea.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}