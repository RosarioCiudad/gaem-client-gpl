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

import com.j256.ormlite.stmt.DeleteBuilder;

import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.FormularioDetalle;

public class FormularioDetalleDAO extends GenericDAO<FormularioDetalle> {

	public FormularioDetalleDAO(Context context) {
		super(context);
	}
	
	/**
	 * 
	 */
	public int deleteByAfiliacion(Formulario afiliacion){
		Log.d(LOG_TAG, "deleteByAfiliacion: enter");
		DeleteBuilder<FormularioDetalle, Integer> db = getDAO().deleteBuilder();
		try {
			db.where().eq("afiliacion_id", afiliacion.getId());
			Log.d(LOG_TAG, "deleteByAfiliacion: exit");
			return delete(db.prepare());
		} catch (Exception e) {
			Log.e(LOG_TAG, "deleteByAfiliacion: ***ERROR***", e);
			return 0;
		}
	}

}
