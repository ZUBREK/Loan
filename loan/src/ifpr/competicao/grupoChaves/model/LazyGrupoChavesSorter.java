package ifpr.competicao.grupoChaves.model;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import ifpr.competicao.grupoChaves.GrupoChaves;

public class LazyGrupoChavesSorter implements Comparator<GrupoChaves> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyGrupoChavesSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(GrupoChaves p1, GrupoChaves  p2) {
		try {
			Object value1 = GrupoChaves.class.getField(this.sortField).get(p1);
			Object value2 = GrupoChaves.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}