package ifpr.pessoa.coordenadorPea.horarioReposicao.model;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import ifpr.pessoa.coordenadorPea.horarioReposicao.HorarioAssistencia;

public class LazyHorarioAssistenciaSorter implements Comparator<HorarioAssistencia> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyHorarioAssistenciaSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(HorarioAssistencia p1, HorarioAssistencia p2) {
		try {
			Object value1 = HorarioAssistencia.class.getField(this.sortField).get(p1);
			Object value2 = HorarioAssistencia.class.getField(this.sortField).get(p2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.DESCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}