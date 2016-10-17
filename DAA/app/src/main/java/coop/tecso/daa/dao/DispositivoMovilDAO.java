/*
 * Copyright (c) 2016 Municipalidad de Rosario, Coop. de Trabajo Tecso Ltda.
 *
 * This file is part of GAEM.
 *
 * GAEM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * GAEM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GAEM.  If not, see <http://www.gnu.org/licenses/>.
 */

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