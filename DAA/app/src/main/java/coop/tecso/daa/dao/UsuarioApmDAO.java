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

import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author tecso.coop
 *
 */
public class UsuarioApmDAO extends GenericDAO<UsuarioApm> {

	/**
	 * 
	 */
	public UsuarioApmDAO(Context context) {
		super(context);
	}

	/**
	 * Find the UsuarioApm by username.
	 * 
	 * @param username
	 * @return usuarioApm instance
	 */
	public UsuarioApm findByUserName(String username) {
		Log.d(LOG_TAG, "findByUserName: enter");
		UsuarioApm usuarioApm = null;
		
		QueryBuilder<UsuarioApm, Integer> usuarioApmQb = getDAO().queryBuilder();
		try {
			// add conditions
			usuarioApmQb.where().eq("estado", 1).and().eq("username", username);
			// prepare query
			usuarioApm = usuarioApmQb.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "findByUserName: **ERROR**", e);
		}
		Log.d(LOG_TAG, "findByUserName: exit");
		
		return usuarioApm;
	}


	/**
	 * Retrieves the count of active UsuarioApm.
	 * 
	 * @return count of rows
	 */
	public long countOfActive(){
		Log.d(LOG_TAG, "countOfActive: enter");
		long countOf = 0;
		QueryBuilder<UsuarioApm, Integer> usuarioApmQb = getDAO().queryBuilder();
		try {
			// add conditions
			usuarioApmQb.setCountOf(true).where().eq("estado", 1);
			// prepare query
			countOf = usuarioApmQb.countOf();
		} catch (Exception e) {
			Log.e(LOG_TAG, "countOfActive: **ERROR**", e);
		}
		Log.d(LOG_TAG, "countOfActive: exit");
		return countOf;
	}
}