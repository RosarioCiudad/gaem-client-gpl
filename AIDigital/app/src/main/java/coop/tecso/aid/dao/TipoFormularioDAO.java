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

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.aid.entities.TipoFormulario;

/**
 * 
 * @author leonardo.fagnano
 *
 */
public class TipoFormularioDAO extends GenericDAO<TipoFormulario> {

	public TipoFormularioDAO(Context context) {
		super(context);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public TipoFormulario findByCodigo(String codigo) {
		Log.i(LOG_TAG, "findByCodigo: enter");
		
		TipoFormulario tipoAfiliacion = null;
		QueryBuilder<TipoFormulario, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			queryBuilder.where().eq("estado", 1).and()
			.rawComparison("codigo", "COLLATE NOCASE LIKE", codigo);
			
			Log.d(LOG_TAG, "query: "+queryBuilder.prepareStatementString());
			// Return the first element
			tipoAfiliacion = getDAO().queryForFirst(queryBuilder.prepare());
		} catch (SQLException e) {
			Log.e(LOG_TAG, "findByCodigo: ***ERROR***", e);
		}

		Log.i(LOG_TAG, "findByCodigo: exit");
		return tipoAfiliacion;
	}

}