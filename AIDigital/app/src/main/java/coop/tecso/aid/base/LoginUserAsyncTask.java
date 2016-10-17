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

package coop.tecso.aid.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.common.DAAServiceImpl;
import coop.tecso.aid.helpers.GUIHelper;
import coop.tecso.aid.utils.Constants;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author leonardo.fagnano
 *
 */
public class LoginUserAsyncTask extends AsyncTask<String, CharSequence, String>{
	
	private static final String TAG = LoginUserAsyncTask.class.getSimpleName();
	
	private AIDigitalApplication appState;
	private ProgressDialog progressDialog;
	private DAAServiceImpl udaaService;
	private Activity activity;
	
	public LoginUserAsyncTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		this.appState = (AIDigitalApplication) activity.getApplicationContext();
		this.progressDialog = ProgressDialog.show(activity, "", activity.getString(R.string.saving_msg), true);
		this.udaaService = new DAAServiceImpl(activity);
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			publishProgress("Validando Usuario…");
			Thread.sleep(1000);
			String userName = params[0];
			String passWord = params[1];

			// User and Password validation
			UsuarioApm userToValidate = udaaService.login(userName, passWord);
			
			// Invalid data
			if(null == userToValidate){
				return appState.getString(R.string.login_user_or_pass_error);
			}
			// Same User in Session
			if(userToValidate.getId() == appState.getCurrentUser().getId()){
				return "";
			}
			if(!udaaService.hasAccess(userToValidate.getId(), Constants.COD_APPLICATION)){
				// Validate User Permission
				return appState.getString(R.string.login_user_permission_error);
			}
			// Notify Change Session to UDAA
			udaaService.changeSession(userName, passWord);
			
			return "";
		} catch (Exception e) {
			Log.e(TAG, "doInBackground: ERROR", e);
			return "Error Inesperado al intentar login";
		}
	}

	@Override
	protected void onProgressUpdate(CharSequence... values) {
		progressDialog.setMessage(values[0]);
	}

	@Override
	protected void onPostExecute(String error) {
		progressDialog.dismiss();
		if(TextUtils.isEmpty(error)){
			// Send UnBlock Application Message
//			Intent msg = new Intent();
//			msg.setAction(Constants.ACTION_UNBLOCK_APPLICATION);
//			appState.sendBroadcast(msg);
		}else{
			GUIHelper.showError(activity, error);
		}
	}
}
