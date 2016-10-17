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