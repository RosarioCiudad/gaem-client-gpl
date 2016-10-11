package coop.tecso.daa.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import coop.tecso.daa.R;
import coop.tecso.daa.domain.notificacion.Notificacion;
import coop.tecso.daa.utils.Constants;

/**
 * 
 * @author tecso.coop
 *
 */
public class ReceiverHelper {

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private static final int NOTIFICATION = R.string.app_name;


	/**
	 * Show a notification while this service is running.     
	 */
	public static void showNotification(Context ctx, final Notificacion notificacion) {

		NotificationManager notificationManager = 
				(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification systemNotification = new Notification(android.R.drawable.ic_menu_info_details, 
				notificacion.getDescripcionAmpliada(), System.currentTimeMillis());

		systemNotification.vibrate  = new long [] {100,100,200,300};
		systemNotification.defaults = Notification.DEFAULT_ALL;

		Intent intent = new Intent();
		String defaultPkg =  "coop.tecso.udaa";
		String defaultApp =  "coop.tecso.udaa.activities.NotificacionesActivity";
		if (notificacion.getTipoNotificacion() != null) {
			int tipoNotificacionID = notificacion.getTipoNotificacion().getId();
			switch (tipoNotificacionID) {
			case Constants.TIPO_NOTIFICACION_APLICACION_ID:
				if (notificacion.getAplicacion().getId() == Constants.APLICACION_HCDIGITAL_ID) {
					defaultPkg =  "coop.tecso.hcd";
					defaultApp =  "coop.tecso.hcd.activities.MainActivity";
				}
				break;
			case Constants.TIPO_NOTIFICACION_ENTIDAD_APL_ID:
				if (notificacion.getAplicacion().getId() == Constants.APLICACION_HCDIGITAL_ID) {
					Intent i = new Intent();
					i.setAction("coop.tecso.udaa.custom.intent.action.NEW_NOTIFICATION");
					i.putExtra("notificacionID", notificacion.getId());
					ctx.sendBroadcast(i);
				}
				break;
			default:
				break;
			}
		}	    

		intent.setComponent(new ComponentName(defaultPkg, defaultApp));

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, 0);

		// Set the info for the views that show in the notification panel.
		systemNotification.setLatestEventInfo(ctx, 
				notificacion.getDescripcionReducida(), 
				notificacion.getDescripcionAmpliada(), 
				contentIntent);

		// Send the notification.
		notificationManager.notify(NOTIFICATION, systemNotification);
	}
}