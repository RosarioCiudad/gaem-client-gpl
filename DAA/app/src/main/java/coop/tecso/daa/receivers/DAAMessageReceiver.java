package coop.tecso.daa.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.utils.Constants;

public class DAAMessageReceiver extends BroadcastReceiver {
	
	private static final String TAG = DAAMessageReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		DAAApplication appState = (DAAApplication) context.getApplicationContext();
		String action = intent.getAction();
		Log.i(TAG, "onReceive: " + action);
		
		if(action.equals(Constants.ACTION_LOSE_SESSION)){
			appState.reset();
		}
		
//		// UnLock application
//		if (action.equals(Constants.ACTION_UNBLOCK_APPLICATION)) {
//			appState.setLocked(false);
//			return;
//		}
//		
//		// UnLock application
//		if (action.equals(Constants.ACTION_BLOCK_APPLICATION)) {
//			appState.setLocked(true);
//			return;
//		}
	}
}