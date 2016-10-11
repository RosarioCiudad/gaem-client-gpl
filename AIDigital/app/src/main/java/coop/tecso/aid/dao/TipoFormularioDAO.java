package coop.tecso.aid.dao;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.aid.entities.TipoFormulario;

/**
 * 
 * @author leonardo.fagnano
 *
 */
public class TipoFormularioDAO extends GenericDAO<TipoFormulario> {

	public TipoFormularioDAO(Context context) {
		super(context);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public TipoFormulario findByCodigo(String codigo) {
		Log.i(LOG_TAG, "findByCodigo: enter");
		
		TipoFormulario tipoAfiliacion = null;
		QueryBuilder<TipoFormulario, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			queryBuilder.where().eq("estado", 1).and()
			.rawComparison("codigo", "COLLATE NOCASE LIKE", codigo);
			
			Log.d(LOG_TAG, "query: "+queryBuilder.prepareStatementString());
			// Return the first element
			tipoAfiliacion = getDAO().queryForFirst(queryBuilder.prepare());
		} catch (SQLException e) {
			Log.e(LOG_TAG, "findByCodigo: ***ERROR***", e);
		}

		Log.i(LOG_TAG, "findByCodigo: exit");
		return tipoAfiliacion;
	}

}