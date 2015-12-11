package ifpr.evento.model;

import ifpr.evento.Evento;
import ifpr.evento.dao.EventoDao;
import ifpr.model.LoginControllerMB;
import ifpr.pessoa.Pessoa;
import ifpr.pessoa.TipoPessoa;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "eventoLazyDataModel")
@ViewScoped
public class EventoLazyDataModel extends LazyDataModel<Evento> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{eventoDao}")
	private EventoDao eventoDao;

	private Pessoa pessoaLogada;

	private LoginControllerMB loginController;

	public EventoLazyDataModel() {

	}

	@PostConstruct
	public void post() {
		FacesContext context = FacesContext.getCurrentInstance();
		loginController = context.getApplication().evaluateExpressionGet(context, "#{loginControllerMB}",
				LoginControllerMB.class);
		pessoaLogada = loginController.getPessoaLogada();
	}

	@Override
	public Evento getRowData(String rowKey) {
		return eventoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Evento campus) {
		return campus.getId();
	}

	@Override
	public List<Evento> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		List<Evento> source = null;
		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = eventoDao.listByNomeEvPessoa(nomePesquisa, pessoaLogada);
		} else {
			if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ESP)
					|| pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_COORD)) {
				source = eventoDao.listByTecnico(first, pageSize, pessoaLogada);
				this.setRowCount(eventoDao.getRowCountTecnico(pessoaLogada));
			} else if (pessoaLogada.getTipo().equals(TipoPessoa.ROLE_TEC_ADM)) {
				source = eventoDao.listByTecnicoAdm(first, pageSize);
				this.setRowCount(eventoDao.getRowCountTecnicoAdm());
			} else {
				source = eventoDao.list(first, pageSize);
				this.setRowCount(eventoDao.getRowCount());
			}
		}
		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyEventoSorter(sortField, sortOrder));
		}

		return source;
	}

	public EventoDao getEventoDao() {
		return eventoDao;
	}

	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public LoginControllerMB getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginControllerMB loginController) {
		this.loginController = loginController;
	}

}
