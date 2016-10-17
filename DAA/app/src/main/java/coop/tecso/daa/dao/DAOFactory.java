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

/**
 * A factory which produces Data-Access-Objects for Device
 * Administrator Application entities.
 * 
 * @author tecso.coop
 *
 */
public final class DAOFactory {

	private static final String LOG_TAG = DAOFactory.class.getSimpleName();

	/**
	 * Singleton instance.
	 */
	private static DAOFactory INSTANCE;

	private AplicacionDAO aplicacionDAO;
	private AplicacionParametroDAO aplicacionParametroDAO;
	private AplicacionPerfilDAO aplicacionPerfilDAO;
	private TablaVersionDAO tablaVersionDAO;
	private NotificacionDAO notificacionDAO;
	private EstadoNotificacionDAO estadoNotificacionDAO;
	private UsuarioApmDAO usuarioApmDAO;
	private DispositivoMovilDAO dispositivoMovilDAO;
	private HistorialUbicacionDAO historialUbicacionDAO;
	private AplicacionBinarioVersionDAO aplicacionBinarioVersionDAO;
	private ImpresoraDAO impresoraDAO;
	private UsuarioApmImpresoraDAO usuarioApmImpresoraDAO;
	private AreaDAO areaDAO;

	/**
	 * The DaoFactory constructor.
	 * 
	 * @param context
	 */
	private DAOFactory(Context context) {
		this.aplicacionDAO = new AplicacionDAO(context);
		this.aplicacionParametroDAO = new AplicacionParametroDAO(context);
		this.aplicacionPerfilDAO = new AplicacionPerfilDAO(context);
		this.tablaVersionDAO = new TablaVersionDAO(context);
		this.notificacionDAO = new NotificacionDAO(context);
		this.estadoNotificacionDAO = new EstadoNotificacionDAO(context);
		this.usuarioApmDAO = new  UsuarioApmDAO(context);
		this.dispositivoMovilDAO = new DispositivoMovilDAO(context);
		this.historialUbicacionDAO = new HistorialUbicacionDAO(context);
		this.aplicacionBinarioVersionDAO = new AplicacionBinarioVersionDAO(context);
		this.impresoraDAO = new ImpresoraDAO(context);
		this.usuarioApmImpresoraDAO = new UsuarioApmImpresoraDAO(context);
		this.areaDAO = new AreaDAO(context);
	}

	/**
	 * Initialize the DAOFactory.
	 * 
	 * @param context The Android context.
	 */
	public static void init(Context context) {
		Log.d(LOG_TAG, "Initializing...");
		DAOFactory.INSTANCE = new DAOFactory(context);
	}

	/**
	 * Get the singleton instance of {@link DAOFactory}.
	 * 
	 * @return The singleton instance.
	 */
	public static DAOFactory getInstance() {
		if (INSTANCE == null) {
			Log.e(LOG_TAG, "DaoFactory not initialized!");
			throw new RuntimeException("DaoFactory not initialized!");
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @return aplicacionDAO instance.
	 */
	public AplicacionDAO getAplicacionDAO() {
		return INSTANCE.aplicacionDAO;
	}

	/**
	 * 
	 * @return aplicacionParametroDAO instance.
	 */
	public AplicacionParametroDAO getAplicacionParametroDAO() {
		return INSTANCE.aplicacionParametroDAO;
	}

	/**
	 * 
	 * @return aplicacionPerfilDAO instance.
	 */
	public AplicacionPerfilDAO getAplicacionPerfilDAO() {
		return INSTANCE.aplicacionPerfilDAO;
	}

	/**
	 * 
	 * @return tablaVersionDAO instance.
	 */
	public TablaVersionDAO getTablaVersionDAO() {
		return INSTANCE.tablaVersionDAO;
	}

	/**
	 * 
	 * @return notificacionDAO instance.
	 */
	public NotificacionDAO getNotificacionDAO() {
		return INSTANCE.notificacionDAO;
	}

	/**
	 * 
	 * @return estadoNotificacionDAO instance.
	 */
	public EstadoNotificacionDAO getEstadoNotificacionDAO() {
		return INSTANCE.estadoNotificacionDAO;
	}

	/**
	 * 
	 * @return usuarioApmDAO instance.
	 */
	public UsuarioApmDAO getUsuarioApmDAO() {
		return INSTANCE.usuarioApmDAO;
	}

	/**
	 * 
	 * @return dispositivoMovilDAO instance.
	 */
	public DispositivoMovilDAO getDispositivoMovilDAO() {
		return INSTANCE.dispositivoMovilDAO;
	}

	/**
	 * 
	 * @return historialUbicacionDAO instance.
	 */
	public HistorialUbicacionDAO getHistorialUbicacionDAO() {
		return INSTANCE.historialUbicacionDAO;
	}

	/**
	 * 
	 * @return aplicacionBinarioVersionDAO instance.
	 */
	public AplicacionBinarioVersionDAO getAplicacionBinarioVersionDAO() {
		return aplicacionBinarioVersionDAO;
	}
	
	/**
	 * 
	 * @return impresoraDAO instance.
	 */
	public ImpresoraDAO getImpresoraDAO() {
		return INSTANCE.impresoraDAO;
	}
	
	/**
	 * 
	 * @return usuarioApmImpresoraDAO instance.
	 */
	public UsuarioApmImpresoraDAO getUsuarioApmImpresoraDAO() {
		return INSTANCE.usuarioApmImpresoraDAO;
	}
	
	/**
	 * 
	 * @return areaDAO instance.
	 */
	public AreaDAO getAreaDAO() {
		return INSTANCE.areaDAO;
	}
}