package coop.tecso.aid.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import coop.tecso.aid.common.WebServiceController;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.dao.FormularioDAO;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.daa.domain.util.DeviceContext;

/**
 * 
 * @author tecso.coop
 *
 */
public class SyncFormularioService extends Service {

	private static final String LOG_TAG = SyncFormularioService.class.getSimpleName();

	private Timer timer = null; 
	private Context context;
	private FormularioDAO formularioDAO;
	private WebServiceController webService;

	@Override
	public IBinder onBind(Intent intent) {
		//
		return null;
	}

	@Override
	public void onCreate() {
		context = this;
		formularioDAO = DAOFactory.getInstance().getFormularioDAO();
		webService = WebServiceController.getInstance(this);
		super.onCreate();
	}

	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		if(timer != null){
			Log.e(LOG_TAG, "The service is already running");
			return START_STICKY;
		}
		//
		Long period = intent.getExtras().getLong("period", 15000L);

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
			if(!DeviceContext.hasConnectivity(context)){
				Log.e(LOG_TAG, "**Hasn't internet connectivity**");
				return;
			}

			List<Formulario> formularioList = formularioDAO.findAllToSync();
			if(formularioList.isEmpty()){
				Log.d(LOG_TAG, "**EMPTY SYNC LIST**");
				stopSelf();
				return;
			}

			boolean status = false;
			for (Formulario formulario : formularioList) {
				try {
					status = webService.syncFormulario(formulario);
				} catch (Exception e) {
					Log.d(LOG_TAG, "**ERROR**", e);
					status = false;
				}
				if(status == false) return;

				// Enviamos el broadcast de "Formulario Enviado", para que se actualice el estado en la vista
				Intent intent = new Intent();
				intent.setAction("coop.tecso.aid.FormEnviado");
				Bundle bundle = new Bundle();
				bundle.putInt("ID_FORM", formulario.getId());
				intent.putExtras(bundle);
				sendBroadcast(intent);

				formulario.setEstado(0);
				formularioDAO.update(formulario);
			}

			toastHandler.sendEmptyMessage(0);
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
		//		Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
	}

	private final Handler toastHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			//			Toast.makeText(getApplicationContext(), "Sincronismo OK", Toast.LENGTH_SHORT).show();
		}
	};    
}