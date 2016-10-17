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

package coop.tecso.aid.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.helpers.ParamHelper;
import coop.tecso.aid.services.SyncFormularioService;
import coop.tecso.aid.services.SyncPanicoService;
import coop.tecso.aid.utils.Constants;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * Singleton manejado por Android que extiende de clase Application.
 * Contiene instancias comunes a toda la aplicacion.
 * 
 * @author tecso.coop
 *
 */
public class AIDigitalApplication extends Application {

	private static final String LOG_TAG = AIDigitalApplication.class.getSimpleName();

	private int orientation;
	private UsuarioApm currentUser;
	private DispositivoMovil dispositivoMovil;

	public boolean hasSynchronized;

	private long lastInteactionTime;
	//
	private String serverURL;

	@Override
	public void onCreate() {
		super.onCreate();
		//
		DAOFactory.init(this);

		this.hasSynchronized = false;
		// Default Orientation: Label / Campo
		SharedPreferences myPrefs = getSharedPreferences(AIDigitalApplication.class.getName(), MODE_PRIVATE);
		this.orientation = myPrefs.getInt(Constants.PREF_DEFAULT_ORIENTATION, LinearLayout.VERTICAL);
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public UsuarioApm getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UsuarioApm currentUser) {
		this.currentUser = currentUser;
	}

	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public boolean canAccess() {
		if (getCurrentUser() != null) 
			return true;
		else 
			return false;
	}
	
	/**
	 * 
	 */
	public void initLockTime(){
		this.lastInteactionTime = System.currentTimeMillis();
	}
	
	/**
	 * 
	 */
	public void checkLock(){
		long currentTime = System.currentTimeMillis();
		// TimeOut de Session en milisegundos
		long sessionTimeOut = ParamHelper.getLong(ParamHelper.SESSION_TIMEOUT,  6L) * 3600000;
		if(currentTime - lastInteactionTime > sessionTimeOut) {
			sendBroadcast(new Intent(Constants.ACTION_LOSE_SESSION));
			stopService(new Intent(this, SyncFormularioService.class));
			stopService(new Intent(this, SyncPanicoService.class));
			return;
		}
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
}