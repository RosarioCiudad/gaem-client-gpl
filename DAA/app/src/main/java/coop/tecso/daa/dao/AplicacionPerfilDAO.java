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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;


/**
 * 
 * @author tecso.coop
 *
 */
public final class AplicacionPerfilDAO extends GenericDAO<AplicacionPerfil> {

	/**
	 * The AplicacionDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public AplicacionPerfilDAO(Context context) {
		super(context);
	}
	
	
	/**
	 * Find All AplicacionPerfil by codigoAplicacion.
	 * 
	 * @param codAplicacion
	 * @return
	 */
	public List<AplicacionPerfil> findAllByAplicacion(String codigoAplicacion) {
		Log.d(LOG_TAG, "findAllByAplicacion: enter");

		List<AplicacionPerfil> result = null;
		try {
			// AplicacionPerfil query
			QueryBuilder<AplicacionPerfil, Integer> aplicacionPerfilQb;
			aplicacionPerfilQb = getDAO().queryBuilder();
			aplicacionPerfilQb.where().eq("estado", 1);
			
			// Aplicacion query
			QueryBuilder<Aplicacion, Integer> aplicacionQb;
			aplicacionQb =  DAOFactory.getInstance().getAplicacionDAO().getDAO().queryBuilder();
			aplicacionQb.where().rawComparison(
					"codigo", "COLLATE NOCASE LIKE", codigoAplicacion).and().eq("estado", 1);
			
			// Join with the Aplicacion query
			aplicacionPerfilQb.join(aplicacionQb);
			
			Log.d(LOG_TAG, aplicacionQb.prepareStatementString());
			result = aplicacionPerfilQb.query();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			result = new ArrayList<AplicacionPerfil>();
		}
		
		Log.d(LOG_TAG, "findAllByAplicacion: exit");
		return result;
	}
	
	
	/**
	 * Find Default AplicacionPerfil by codigoAplicacion.
	 * 
	 * @param codAplicacion
	 * @return
	 */
	public AplicacionPerfil findDefaultByAplicacion(String codAplicacion) {
		Log.d(LOG_TAG, "findDefaultByAplicacion: enter");

		AplicacionPerfil aplicacionPerfil = null;
		try {
			// AplicacionParametro query
			QueryBuilder<AplicacionPerfil, Integer> aplicacionPerfilQb;
			aplicacionPerfilQb = getDAO().queryBuilder();
			aplicacionPerfilQb.where().eq("esPerfilPorDefecto", true).and().eq("estado", 1);
			
			// Aplicacion query
			QueryBuilder<Aplicacion, Integer> aplicacionQb;
			aplicacionQb =  DAOFactory.getInstance().getAplicacionDAO().getDAO().queryBuilder();
			aplicacionQb.where().rawComparison(
					"codigo", "COLLATE NOCASE LIKE", codAplicacion).and().eq("estado", 1);
			
			// Join with the Aplicacion query
			aplicacionPerfilQb.join(aplicacionQb);
			
			Log.d(LOG_TAG, aplicacionQb.prepareStatementString());
			aplicacionPerfil = aplicacionPerfilQb.queryForFirst();
		} catch (Exception e) {
			Log.e(LOG_TAG, "findDefaultByAplicacion: **ERROR**", e);
		}
		
		Log.d(LOG_TAG, "findDefaultByAplicacion: exit");
		return aplicacionPerfil;
	}
	
	public List<AplicacionPerfil> getListByUsuarioAndCodAplicacion(String codigoAplicacion, UsuarioApm usuario) {
		Log.d(LOG_TAG, "getListByUsuarioAndCodAplicacion: enter");
		List<AplicacionPerfil> result = null;
		try {
			String query = "SELECT aplicacionperfil.* " +
					" FROM " +
					" apm_aplicacionperfil aplicacionperfil, " +
					" def_areaaplicacionperfil area, " +
					" apm_perfilacceso perfilacceso," +
					" apm_perfilaccesousuario perfilaccesousuario, " +
					" apm_aplicacion aplicacion" +
					" WHERE 1=1  " +
					" and perfilaccesousuario.usuarioapm_id  = "+ usuario.getId() +
					" and perfilacceso.id = perfilaccesousuario.perfilacceso_id " +
					" and area.perfilacceso_id = perfilacceso.id " +
					" and aplicacionperfil.id = area.aplicacionperfil_id " +
					" and aplicacion.id = aplicacionperfil.aplicacion_id" +
					" and aplicacion.codigo COLLATE NOCASE LIKE '"+ codigoAplicacion+"'"; //peligro de sqlinyection
			
			Log.d(LOG_TAG, query);

			GenericRawResults<AplicacionPerfil> rawResults = getDAO().queryRaw(query, getDAO().getRawRowMapper());
			result = rawResults.getResults();
			rawResults.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			result = new ArrayList<AplicacionPerfil>();
		}
		Log.d(LOG_TAG, "getListByUsuarioAndCodAplicacion: exit");
		return result;
	}
}