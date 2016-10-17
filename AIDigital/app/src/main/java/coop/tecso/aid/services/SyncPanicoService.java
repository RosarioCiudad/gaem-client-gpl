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

package coop.tecso.aid.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import coop.tecso.aid.common.WebServiceController;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.dao.PanicoDAO;
import coop.tecso.aid.entities.Panico;
import coop.tecso.aid.helpers.ParamHelper;
import coop.tecso.daa.domain.util.DeviceContext;

/**
 * 
 * @author tecso.coop
 *
 */
public class SyncPanicoService extends Service {

	private static final String LOG_TAG = SyncPanicoService.class.getSimpleName();

	private Timer timer = null; 
	private Context context;
	private PanicoDAO panicoDAO;
	private WebServiceController webService;

	@Override
	public IBinder onBind(Intent intent) {
		//
		return null;
	}

	@Override
	public void onCreate() {
		context = this;
		panicoDAO = DAOFactory.getInstance().getPanicoDAO();
		webService = WebServiceController.getInstance(this);
		
		Long period = ParamHelper.getLong(ParamHelper.SEND_PANICO_PERIOD, 15000L);
		timer = new Timer();
		timer.schedule(new MainTask(), 0, period);
		

		Log.i(LOG_TAG, String.format("**STARTING SERVICE** - period: %s seg. ", period/1000));
		super.onCreate();
	}

	/**
	 * 
	 * @author tecso.coop
	 *
	 */
	private class MainTask extends TimerTask { 
		@Override
		public synchronized void run(){
			Log.d(LOG_TAG, String.format("Running process... %s", Thread.currentThread().getName()));
			// Check Internet connection 
			if(!DeviceContext.hasConnectivity(context)){
				Log.e(LOG_TAG, "**Hasn't internet connectivity**");
				return;
			}

			List<Panico> listToSync = panicoDAO.findAllActive();
			if(listToSync.isEmpty()){
				Log.d(LOG_TAG, "**EMPTY SYNC LIST**");
				stopSelf();
				return;
			}

			boolean status = false;
			for (Panico panico : listToSync) {
				try {
					status = webService.syncPanico(panico);
				} catch (Exception e) {
					Log.d(LOG_TAG, "**ERROR**", e);
					status = false;
				}
				if(status == false) return;

				panicoDAO.delete(panico);
			}
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
		super.onDestroy();
	}
}