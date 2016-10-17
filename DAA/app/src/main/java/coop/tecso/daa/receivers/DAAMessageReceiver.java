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