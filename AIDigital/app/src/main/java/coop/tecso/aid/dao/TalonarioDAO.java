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

import coop.tecso.aid.entities.Talonario;
import coop.tecso.aid.entities.TipoFormulario;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;

public class TalonarioDAO extends GenericDAO<Talonario> {

	public TalonarioDAO(Context context) {
		super(context);
	}

	/**
	 * 
	 * @return
	 */
	public Integer countOfDisponible(TipoFormulario tipoFormulario) {
		Log.i(LOG_TAG, "countOfDisponible: enter");

		Integer result;
		QueryBuilder<Talonario, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			queryBuilder.selectRaw(" SUM(valorhasta - valor) ")
			.where().eq("estado", 1).and().eq("tipoFormulario_id", tipoFormulario.getId());
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			result = Integer.valueOf(queryBuilder.queryRawFirst()[0]);
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			result = 0;
		}
		Log.i(LOG_TAG, "countOfDisponible: exit");
		return result;
	}
	
	
	/**
	 * Find Talonario by codigoAplicacion.
	 * 
	 * @param codAplicacion
	 * @return
	 */
	public Talonario findBy(DispositivoMovil dispositivomovil, TipoFormulario tipoFormulario) {
		Log.i(LOG_TAG, "findBy: enter");

		Talonario talonario = null;
		try {
			// Talonario query
			QueryBuilder<Talonario, Integer> queryBuilder =  getDAO().queryBuilder();
			queryBuilder.orderBy("valor", true).where().eq("estado", 1)
			.and().eq("dispositivoMovil_id", dispositivomovil.getId())
			.and().eq("tipoFormulario_id", tipoFormulario.getId());
			
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			talonario = queryBuilder.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
		
		Log.i(LOG_TAG, "findBy: exit");
		return talonario;
	}

}