package ifpr.evento.eventoPessoa.dao;

import ifpr.dao.GenericDao;
import ifpr.evento.eventoPessoa.EventoPessoa;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="eventoPessoaDao")
@ApplicationScoped
public class EventoPessoaDaoImpl  extends GenericDao<EventoPessoa> implements EventoPessoaDao {
	
	private static final long serialVersionUID = 1L;
	
	public EventoPessoaDaoImpl()
	{
		super(EventoPessoa.class);
	}
	
	
}
	