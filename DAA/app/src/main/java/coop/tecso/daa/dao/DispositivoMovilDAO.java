package coop.tecso.daa.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.daa.domain.seguridad.DispositivoMovil;

/**
 * 
 * @author tecso.coop
 *
 */
public class DispositivoMovilDAO extends GenericDAO<DispositivoMovil> {

	/**
	 * 
	 */
	public DispositivoMovilDAO(Context context) {
		super(context);
	}
	

	/**
	 * 
	 * @return
	 */
	public DispositivoMovil findByIMEI(String deviceId){
		Log.d(LOG_TAG, "findByIMEI: enter");
		DispositivoMovil dispositivoMovil = null;
		QueryBuilder<DispositivoMovil, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			// add filters
			queryBuilder.orderBy("id", false).where().eq("estado", 1).and().eq("numeroIMEI", deviceId);
			// prepare statement
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			// execute query
			dispositivoMovil = queryBuilder.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
		Log.d(LOG_TAG, "findByIMEI: exit");
		return dispositivoMovil;
	}
	
	/**
	 * 
	 * @return
	 */
	public DispositivoMovil findByEmail(String email){
		Log.d(LOG_TAG, "findByEmail: enter");
		DispositivoMovil dispositivoMovil = null;
		QueryBuilder<DispositivoMovil, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			// add filters
			queryBuilder.orderBy("id", false).where().eq("estado", 1).and().eq("emailAddress", email);
			// prepare statement
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			// execute query
			dispositivoMovil = queryBuilder.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
		Log.d(LOG_TAG, "findByEmail: exit");
		return dispositivoMovil;
	}

}