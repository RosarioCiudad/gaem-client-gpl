package coop.tecso.daa.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import coop.tecso.daa.common.WebServiceController;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.HistorialUbicacionDAO;
import coop.tecso.daa.domain.gps.HistorialUbicacion;
import coop.tecso.daa.domain.util.DeviceContext;

/**
 * 
 * @author tecso.coop
 *
 */
public class SyncUbicacionService extends Service {

	private static final String LOG_TAG = SyncUbicacionService.class.getSimpleName();

	private Timer timer; 
	private HistorialUbicacionDAO historialUbicacionDAO;
	private WebServiceController webService;

	@Override
	public IBinder onBind(Intent intent) {
		//
		return null;
	}

	@Override
	public void onCreate() {
		this.historialUbicacionDAO =
				DAOFactory.getInstance().getHistorialUbicacionDAO();
		this.webService = WebServiceController.getInstance(this);
		super.onCreate();
	}

	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		if(timer != null){
			Log.e(LOG_TAG, "The service is already running");
			return START_STICKY;
		}
		//
		Long period = 120000L;
		if(intent != null && intent.getExtras() != null){
			period = intent.getExtras().getLong("period", 120000L);
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
			// Check Internet connection 
			if(!DeviceContext.hasConnectivity(SyncUbicacionService.this)){
				Log.e(LOG_TAG, "**Hasn't internet connectivity**");
				return;
			}

			List<HistorialUbicacion> listToSync = historialUbicacionDAO.findAllActive();
			if(listToSync.isEmpty()){
				Log.d(LOG_TAG, "**EMPTY SYNC LIST**");
				return;
			}

			boolean status = false;
			for (HistorialUbicacion historialUbicacion : listToSync) {
				try {
					status = webService.syncUbicacion(historialUbicacion);
				} catch (Exception e) {
					Log.d(LOG_TAG, "**ERROR**", e);
					status = false;
				}
				if(status == false) return;
				Log.d(LOG_TAG, "Deleting sync entity...");
				historialUbicacionDAO.delete(historialUbicacion);
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
		//TODO: 
		//		OpenHelperManager.releaseHelper();
		super.onDestroy();
	}
}