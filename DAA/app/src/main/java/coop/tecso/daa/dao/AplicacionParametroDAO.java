package coop.tecso.daa.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
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
			QueryBuilder<AplicacionParametro, Integer> aplicacionParametroQb;
			aplicacionParametroQb = getDAO().queryBuilder();
			aplicacionParametroQb.where().rawComparison(
					"codigo", "COLLATE NOCASE LIKE", codParametro).and().eq("estado", 1);
			
			// Aplicacion query
			QueryBuilder<Aplicacion, Integer> aplicacionQb;
			aplicacionQb = DAOFactory.getInstance().getAplicacionDAO().getDAO().queryBuilder();
			aplicacionQb.where().rawComparison(
					"codigo", "COLLATE NOCASE LIKE", codAplicacion).and().eq("estado", 1);

			// Join with the Aplicacion query
			aplicacionParametroQb.join(aplicacionQb);
			
			Log.d(LOG_TAG, aplicacionParametroQb.prepareStatementString());
			aplicacionParametro = aplicacionParametroQb.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "findByCodigo: **ERROR**", e);
		}
		
		Log.d(LOG_TAG, "findByCodigo: exit");
		return aplicacionParametro;
	}
}