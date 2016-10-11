package coop.tecso.aid.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.daa.domain.aplicacion.AplicacionParametro;


/**
 * 
 * @author tecso.coop
 *
 */
public final class AplicacionParametroDAO extends GenericDAO<AplicacionParametro> {

	/**
	 * The AplicacionParametroDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public AplicacionParametroDAO(Context context) {
		super(context);
	}


	/**
	 * Find AplicacionParametro by codigoParametro and codigoAplicacion.
	 * 
	 * @param codParametro
	 * @param codAplicacion
	 * @return
	 */
	public AplicacionParametro findByCodigo(String codParametro, String codAplicacion) {
		Log.d(LOG_TAG, "findByCodigo: enter");

		AplicacionParametro aplicacionParametro = null;
		try {
			// AplicacionParametro query
			QueryBuilder<AplicacionParametro, Integer> queryBuilder;
			queryBuilder = getDAO().queryBuilder();
			queryBuilder.where().rawComparison(
					"codigo", "COLLATE NOCASE LIKE", codParametro).and().eq("estado", 1);
			
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			aplicacionParametro = queryBuilder.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "findByCodigo: **ERROR**", e);
		}
		
		Log.d(LOG_TAG, "findByCodigo: exit");
		return aplicacionParametro;
	}
}