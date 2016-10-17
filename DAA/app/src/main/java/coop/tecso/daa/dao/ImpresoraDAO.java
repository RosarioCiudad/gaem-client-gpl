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

import coop.tecso.daa.domain.seguridad.Impresora;
import coop.tecso.daa.domain.seguridad.UsuarioApm;
import coop.tecso.daa.domain.seguridad.UsuarioApmImpresora;


/**
 * 
 * @author tecso.coop
 *
 */
public final class ImpresoraDAO extends GenericDAO<Impresora> {

	/**
	 * The NotificacionDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public ImpresoraDAO(Context context) {
		super(context);
	}

	/**
	 * Find Impresora by usuarioApm.
	 * 
	 * @param usuarioApm
	 * @return
	 */
	public Impresora findByUsuarioApm(UsuarioApm usuarioApm) {
		Log.d(LOG_TAG, "findByUsuarioApm: enter");
		Impresora result = null;
		try {
			// Impresora QueryBuilder
			QueryBuilder<Impresora, Integer> impresoraQuery = getDAO().queryBuilder();
			impresoraQuery.orderBy("fechaUltMdf", false).where().eq("estado", 1);
			
			// UsuarioImpresora QueryBuilder
			QueryBuilder<UsuarioApmImpresora, Integer> usuarioImpresoraQuery;
			usuarioImpresoraQuery =  DAOFactory.getInstance().
					getUsuarioApmImpresoraDAO().getDAO().queryBuilder();
			usuarioImpresoraQuery.where().eq("usuarioApm_id", usuarioApm.getId());
			
			// Join with the Aplicacion query
			usuarioImpresoraQuery.join(impresoraQuery);
			
			Log.d(LOG_TAG, impresoraQuery.prepareStatementString());
			result = impresoraQuery.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
		Log.d(LOG_TAG, "findByUsuarioApm: exit");
		return result;
	}
}