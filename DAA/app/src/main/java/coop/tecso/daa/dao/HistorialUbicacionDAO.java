package coop.tecso.daa.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.daa.domain.gps.HistorialUbicacion;

/**
 * 
 * @author tecso.coop
 *
 */
public final class HistorialUbicacionDAO extends GenericDAO<HistorialUbicacion> {

	/**
	 * The UbicacionGPSDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public HistorialUbicacionDAO(Context context) {
		super(context);
	}

	/**
	 * Retrieves the count of active UbicacionGPS.
	 * 
	 * @return count of rows
	 */
	public long countOfActive(){
		Log.d(LOG_TAG, "countOfActive: enter");
		long countOf = 0;
		QueryBuilder<HistorialUbicacion, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			// add conditions
			queryBuilder.setCountOf(true).where().eq("estado", 1);
			// prepare query
			countOf = queryBuilder.countOf();
		} catch (Exception e) {
			Log.e(LOG_TAG, "countOfActive: **ERROR**", e);
		}
		Log.d(LOG_TAG, "countOfActive: exit");
		return countOf;
	}

	/**
	 * Delete the oldest HistorialUbicacionDAO considering fechaUltMdf.
	 * 
	 * @return count of affected rows
	 */
	public int deleteOldest() {
		Log.d(LOG_TAG, "deleteOldest: enter");
		int countRow = 0;
		QueryBuilder<HistorialUbicacion, Integer>  queryBuilder = getDAO().queryBuilder();
		try {
			HistorialUbicacion ubicacionGPS = queryBuilder.orderBy("fechaUltMdf", true).queryForFirst();
			countRow = delete(ubicacionGPS);
		} catch (Exception e) {
			Log.e(LOG_TAG, "deleteOldest: **ERROR", e);
		}
		Log.d(LOG_TAG, "deleteOldest: exit");
		return countRow;
	}
}