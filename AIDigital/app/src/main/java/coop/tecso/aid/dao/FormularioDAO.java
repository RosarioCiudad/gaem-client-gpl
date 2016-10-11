package coop.tecso.aid.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.aid.entities.EstadoTipoFormulario;
import coop.tecso.aid.entities.Formulario;


/**
 * 
 * @author tecso.coop
 *
 */
public class FormularioDAO extends GenericDAO<Formulario> {

	/**
	 * 
	 */
	public FormularioDAO(Context context) {
		super(context);
	}

	/**
	 * 
	 * @return
	 */
	public List<Formulario> findAllOrdered() {
		Log.d(LOG_TAG, "findAllOrdered: enter");
		
		List<Formulario> resultList = new ArrayList<Formulario>();
		QueryBuilder<Formulario, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			// Build query
			queryBuilder.orderBy("estadoTipoFormulario_id", true)
			.orderBy("fechaUltMdf", false);
			
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			//Execute query
			resultList = queryBuilder.query();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
		for (Formulario formulario : resultList) {
			DAOFactory.getInstance().getEstadoTipoFormularioDAO().refresh(formulario.getEstadoTipoFormulario());
			DAOFactory.getInstance().getTipoFormularioDAO().refresh(formulario.getTipoFormulario());
		}

		Log.i(LOG_TAG, "findAllOrdered: exit");
		return resultList;
	}

	/**
	 * 
	 * @return
	 */
	public List<Formulario> findAllToSync() {
		Log.i(LOG_TAG, "findAllToSync: enter");
		
		List<Formulario> resultList = new ArrayList<Formulario>();
		QueryBuilder<Formulario, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			queryBuilder.orderBy("fechaUltMdf", true)
			.where().in("estadoTipoFormulario_id",
					EstadoTipoFormulario.ID_CERRADA_DEFINITIVA, EstadoTipoFormulario.ID_ANULADA)
			.and().eq("estado", 1);

			//Execute query
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			resultList = queryBuilder.query();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		Log.i(LOG_TAG, "findAllToSync: exit");
		return resultList;
	}
	

	
	/**
	 * 
	 * @return
	 */
	public int deleteSynchronized(int hours) throws Exception {
		Log.d(LOG_TAG, "deleteSynchronized: enter");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -hours);
		// Filters
		QueryBuilder<Formulario, Integer> queryBuilder = getDAO().queryBuilder();
		queryBuilder.where().eq("estado", 0).and().lt("fechaUltMdf", calendar.getTime());

		Collection<Formulario> collToDelete = queryBuilder.query();
		for (Formulario formulario : collToDelete) {
			//
			DAOFactory.getInstance().getTalonarioDAO().delete(formulario.getTalonario());
			// Delete related entities
			DAOFactory.getInstance().getFormularioDetalleDAO().delete(formulario.getListFormularioDetalle());
		}
		// Delete Collection
		int result = super.delete(collToDelete);
		
		Log.d(LOG_TAG, "deleteSynchronized: exit");
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public Long countOfPending() {
		Log.i(LOG_TAG, "countOfPending: enter");

		Long result = 0L;
		QueryBuilder<Formulario, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			queryBuilder.where()
			.eq("estadoTipoFormulario_id", EstadoTipoFormulario.ID_EN_PREPARACION).or()
			.eq("estadoTipoFormulario_id", EstadoTipoFormulario.ID_CERRADA_PROVISORIA);

			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			//Execute query
			result = queryBuilder.countOf();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
		Log.i(LOG_TAG, "countOfPending: exit");
		return result;
	}
}