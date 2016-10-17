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

package coop.tecso.aid.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import coop.tecso.aid.dao.AplicacionParametroDAO;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;

public class ParamHelper {

	private static final String LOG_TAG = ParamHelper.class.getSimpleName(); 

	private Map<String, String> values = new HashMap<String, String>();

	public static String DOMINIO = "CampoDominioID";
	public static String SESSION_TIMEOUT = "SessionTimeout";
	public static String UMBRAL_ELIMINACION = "UmbralEliminacion";
	
	public static String SEND_PANICO_PERIOD = "PanicoPeriod";
	
	public static String SEND_PANICO_MESSAGE = "PanicoMessage";
	public static String SEND_LOCATION_NOT_FOUND_MESSAGE = "LocationNotFoundMessage";
	
	public static String TIPO_DOCUMENTO = "CampoTipoDocumentoID";
	public static String SEXO = "CampoSexoID";
	public static String RESOLUCION = "ResolucionFoto";
	public static String CLASE_LICENCIA = "CampoClaseLicenciaID";
	
	/**
	 * Singleton instance.
	 */
	private static ParamHelper INSTANCE;

	private ParamHelper() {
		AplicacionParametroDAO aplicacionParametroDAO = 
				DAOFactory.getInstance().getAplicacionParametroDAO();

		List<AplicacionParametro> paramList = aplicacionParametroDAO.findAllActive();
		for (AplicacionParametro param : paramList) {
			Log.d(LOG_TAG, String.format("param: %s - value: %s",
					param.getCodigo(), param.getValor()));
			values.put(param.getCodigo(), param.getValor());
		}
	}


	/**
	 * Initialize the Param.
	 * 
	 * @param context The Android context.
	 */
	public static void initialize() {
		Log.d(LOG_TAG, "Initializing...");
		INSTANCE = new ParamHelper();
	}

	/**
	 * Get the singleton instance of {@link ParamHelper}.
	 * 
	 * @return The singleton instance.
	 */
	public static ParamHelper getInstance() {
		if (INSTANCE == null) {
			Log.e(LOG_TAG, "ParamHelper not initialized!");
			throw new RuntimeException("ParamHelper not initialized!");
		}
		return INSTANCE;
	}

	protected String getValue(String key){
		return values.get(key);
	}

	static public int getInteger(String key) throws Exception {
		String ret = ParamHelper.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException(String.format("No existe parámetro %s", key));
		return Integer.parseInt(ret);
	}

	static public Integer getInteger(String key, Integer defaultValue) {
		try {
			return getInteger(key);
		} catch (Exception e) {
			Log.e(LOG_TAG, String.format("**ERROR**: %s", e.getMessage()));
			return defaultValue;
		}
	}

	static public String getString(String key) throws Exception {
		String ret = ParamHelper.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException(String.format("No existe parámetro %s", key));
		return ret;
	}

	static public String getString(String key, String defaultValue) {
		try {
			return getString(key);
		} catch (Exception e) {
			Log.e(LOG_TAG, String.format("**ERROR**: %s", e.getMessage()));
			return defaultValue;
		}
	}

	static public Long getLong(String key) throws Exception {
		String ret = ParamHelper.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException(String.format("No existe parámetro %s", key));
		return Long.parseLong(ret);
	}

	static public Long getLong(String key, Long defaultValue) {
		try {
			return getLong(key);
		} catch (Exception e) {
			Log.e(LOG_TAG, String.format("**ERROR**: %s", e.getMessage()));
			return defaultValue;
		}
	}

	static public Double getDouble(String key) throws Exception {
		String ret = ParamHelper.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException(String.format("No existe parámetro %s", key));
		return Double.parseDouble(ret);
	}

	static public Double getDouble(String key, Double defaultValue) {
		try {
			return getDouble(key);
		} catch (Exception e) {
			Log.e(LOG_TAG, String.format("**ERROR**: %s", e.getMessage()));
			return defaultValue;
		}
	}
}