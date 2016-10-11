package coop.tecso.aid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.utils.Constants;

public class DAAMessageReceiver extends BroadcastReceiver {

	private static final String TAG = DAAMessageReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "onReceive: " + action);
		
		// 
		if (action.equals(Constants.ACTION_NEW_NOTIFICATION)) {
			Integer notificacionID = intent.getExtras().getInt("notificacionID");
			Log.d(TAG, "Procesando evento desde UDAA. Nueva Notificacion. ID: "+notificacionID);
			if(notificacionID != null){
				Log.d(TAG, "Ejecutando AsyncTask de Notificacion.");
//				SADigitalApplication appState = (SADigitalApplication) context.getApplicationContext();
//				appState.getNotificationManager().procesarUDAANotification(notificacionID);
			}else{
				Log.d(TAG, "Procesando evento desde UDAA. FAIL.");
			}
			return;
		}
		
		// End Session from DAA
		if(action.equals(Constants.ACTION_LOSE_SESSION)) {
			// Se elimina referencia a sesion
			AIDigitalApplication appState = (AIDigitalApplication) context.getApplicationContext();
			appState.setCurrentUser(null);
			// BugFix Error 24 – No se limpian los registros
			// Se fuerza el sincronismo de HCDigital
			appState.hasSynchronized = false;
			return;
		}
	}
}
