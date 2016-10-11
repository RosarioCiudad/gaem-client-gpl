package coop.tecso.daa.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;

import coop.tecso.daa.domain.notificacion.Notificacion;


/**
 * 
 * @author tecso.coop
 *
 */
public final class NotificacionDAO extends GenericDAO<Notificacion> {

	/**
	 * The NotificacionDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public NotificacionDAO(Context context) {
		super(context);
	}

	/**
	 * Delete all Notificacion instances.
	 * 
	 * @return number of rows updated in the database.
	 */
	public int deleteAll() {
		Log.d(LOG_TAG, "deleteAll: enter");

		int rows = 0;
		DeleteBuilder<Notificacion, Integer> notificacionDb = getDAO().deleteBuilder();
		try {
			rows = notificacionDb.delete();
		} catch (Exception e) {
			Log.e(LOG_TAG, "deleteAll: **ERROR**", e);
		}

		Log.d(LOG_TAG, "deleteAll: exit");
		return rows;
	}

}