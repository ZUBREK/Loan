package ifpr.pessoa.tecnicoAdministrativo.model;

import ifpr.pessoa.tecnicoAdministrativo.TecnicoAdministrativo;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyTecnicoAdministrativoSorter implements Comparator<TecnicoAdministrativo> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyTecnicoAdministrativoSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(TecnicoAdministrativo p1, TecnicoAdministrativo p2) {
		try {
			Object value1 = TecnicoAdministrativo.class.getField(this.sortField).get(p1);
			Object value2 = TecnicoAdministrativo.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}