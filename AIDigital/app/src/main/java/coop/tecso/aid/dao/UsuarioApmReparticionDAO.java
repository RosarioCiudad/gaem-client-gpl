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

package coop.tecso.aid.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.aid.entities.TipoFormulario;
import coop.tecso.aid.entities.UsuarioApmReparticion;

/**
 * 
 * @author tecso.coop
 *
 */
public class UsuarioApmReparticionDAO extends GenericDAO<UsuarioApmReparticion> {

	/**
	 * 
	 */
	public UsuarioApmReparticionDAO(Context context) {
		super(context);
	}

	/**
	 * Find the UsuarioApm by username.
	 * 
	 * @param username
	 * @return usuarioApm instance
	 */
	public UsuarioApmReparticion findByTipoFormulario(TipoFormulario tipoFormulario) {
		Log.d(LOG_TAG, "findByTipoFormulario: enter");
		UsuarioApmReparticion usuarioApmReparticion = null;
		
		QueryBuilder<UsuarioApmReparticion, Integer> usuarioApmQb = getDAO().queryBuilder();
		try {
			// add conditions
			usuarioApmQb.where().eq("estado", 1)
			.and().eq("usuarioApm_id", appState.getCurrentUser().getId())
			.and().eq("tipoFormulario_id", tipoFormulario.getId());
			// prepare query
			usuarioApmReparticion = usuarioApmQb.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "findByTipoFormulario: **ERROR**", e);
		}
		Log.d(LOG_TAG, "findByTipoFormulario: exit");
		
		return usuarioApmReparticion;
	}
}