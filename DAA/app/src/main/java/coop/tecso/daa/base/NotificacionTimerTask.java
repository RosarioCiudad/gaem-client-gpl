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

package coop.tecso.daa.base;

import java.util.List;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;
import coop.tecso.daa.common.WebServiceController;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.EstadoNotificacionDAO;
import coop.tecso.daa.domain.notificacion.EstadoNotificacion;
import coop.tecso.daa.domain.notificacion.Notificacion;
import coop.tecso.daa.domain.util.DeviceContext;
import coop.tecso.daa.receivers.ReceiverHelper;
import coop.tecso.daa.utils.Constants;

/**
 * 
 * @author tecso.coop
 *
 */
public class NotificacionTimerTask extends TimerTask {
	
	private static final String LOG_TAG = NotificacionTimerTask.class.getSimpleName();

	private DAAApplication appState;
	private DAOFactory daaDao;
	private WebServiceController wService;
	
	public NotificacionTimerTask(Context context) {
		this.appState = (DAAApplication) context.getApplicationContext();
		this.daaDao  = DAOFactory.getInstance();
		this.wService = WebServiceController.getInstance(context);
	}

	@Override
	public void run() {
		if(null == appState.getCurrentUser()){
			Log.d(LOG_TAG, "UDAA sin login, se omite la verificacion del servicio C2DM");
			return;
		}
		// Check internet connection
		if(!DeviceContext.hasConnectivity(appState)){
			Log.e(LOG_TAG, "**Has not internet connectivity**");
			return;
		}
		
		//
		try {
			List<Notificacion> listPending = wService.getNotificationsPending(this.appState.getDispositivoMovil().getId());
			Log.i(LOG_TAG, ">>>Total de Notificaciones a procesar>>> " + listPending.size());
			
			// Estado Notificacion : Enviada
			EstadoNotificacionDAO estadoNotificacionDAO = daaDao.getEstadoNotificacionDAO();
			EstadoNotificacion enviada = estadoNotificacionDAO.findById(Constants.ESTADO_NOTIFICACION_ENVIADA_ID);
			for (Notificacion notificacion : listPending) {
				int id = notificacion.getId();
				boolean response = wService.confirmReceiptNotificacion(id);
				if(response){
					Log.i(LOG_TAG, "Guardando notificacion obtenida. ID: " +id);
					// Modifico el estado de la notificacion
					notificacion.setEstadoNotificacion(enviada);
					daaDao.getNotificacionDAO().create(notificacion);
					
					Log.i(LOG_TAG, "Procesando notificacion...");
					ReceiverHelper.showNotification(appState, notificacion); 
				}else{
					Log.i(LOG_TAG, "Error al obtener Notificacion. ID: " + id);
				}
			}
			Log.i(LOG_TAG, "Finalizando correctamente sincronismo por canal de contingencias...");
		} catch (Exception e) {
			Log.i(LOG_TAG, "No se pudo sincronizar por canal de contingencias. Causa:", e);
		}
	}
}