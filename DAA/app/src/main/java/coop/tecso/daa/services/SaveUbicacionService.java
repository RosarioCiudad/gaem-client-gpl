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

package coop.tecso.daa.services;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.base.LocationHelper;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.HistorialUbicacionDAO;
import coop.tecso.daa.domain.gps.HistorialUbicacion;
import coop.tecso.daa.domain.util.DeviceContext;

/**
 * 
 * @author tecso.coop
 *
 */
public class SaveUbicacionService extends Service {

	private static final String LOG_TAG = SaveUbicacionService.class.getSimpleName();

	private Timer timer; 
	private DAAApplication appState;
	private HistorialUbicacionDAO historialUbicacionDAO;

	private int MAX_REGISTROS; //TODO: parametro

	@Override
	public IBinder onBind(Intent intent) {
		//
		return null;
	}

	@Override
	public void onCreate() {
		this.historialUbicacionDAO =
				DAOFactory.getInstance().getHistorialUbicacionDAO();
		this.appState = (DAAApplication) getApplicationContext();
		MAX_REGISTROS = 20;
		super.onCreate();
	}

	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		if(timer != null){
			Log.e(LOG_TAG, "The service is already running");
			return START_STICKY;
		}
		//
		Long period = 60000L;
		if(intent.getExtras() != null){
			period = intent.getExtras().getLong("period", 60000L);
		}
		timer = new Timer();
		timer.schedule(new MainTask(), 0, period);

		Log.i(LOG_TAG, String.format("**STARTING SERVICE** - period: %s seg. ", period/1000));

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 
	 * @author tecso.coop
	 *
	 */
	private class MainTask extends TimerTask { 
		@Override
		public synchronized void run(){
			Log.d(LOG_TAG, "Running process..."+Thread.currentThread().getName());

			Location location = LocationHelper.getInstance().getCurrentLocation();
			if(location == null){
				Log.e(LOG_TAG, "**ERROR** : CURRENT LOCATION IS NULL");
				return;
			}

			HistorialUbicacion historialUbicacion =  new HistorialUbicacion();

			historialUbicacion.setAltitud(location.getAltitude());
			historialUbicacion.setDispositivoMovil(appState.getDispositivoMovil());
			historialUbicacion.setUsuarioApm(appState.getCurrentUser());
			historialUbicacion.setFechaPosicion(new Date(location.getTime()));
			historialUbicacion.setFechaUbicacion(new Date());
			historialUbicacion.setLatitud(location.getLatitude());
			historialUbicacion.setLongitud(location.getLongitude());
			historialUbicacion.setOrigen(location.getProvider());
			historialUbicacion.setPrecision(location.getAccuracy());
			historialUbicacion.setRadio(location.getBearing());
			historialUbicacion.setVelocidad(location.getSpeed());
			historialUbicacion.setUsuario(appState.getCurrentUser().getUsername());
			//
			historialUbicacion.setNivelBateria(DeviceContext.getBatteryLevel(appState));
			historialUbicacion.setNivelSenial(DeviceContext.getSignalLevel());

			if (historialUbicacionDAO.countOfActive() >= MAX_REGISTROS) {
				Log.d(LOG_TAG, "Deleting oldest position...");
				historialUbicacionDAO.deleteOldest();
			}
			historialUbicacionDAO.create(historialUbicacion);
		}
	}    

	@Override
	public void onDestroy(){
		if(timer != null){
			Log.i(LOG_TAG, "**SERVICE STOPPED**");
			timer.cancel();
			timer.purge();
			timer = null;
		}
		//
		LocationHelper.getInstance().finish();
		//TODO: 
		//		OpenHelperManager.releaseHelper();
		super.onDestroy();
	}
}