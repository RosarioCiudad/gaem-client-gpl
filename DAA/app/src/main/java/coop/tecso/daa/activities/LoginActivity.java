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

package coop.tecso.daa.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import coop.tecso.daa.R;
import coop.tecso.daa.activities.task.LoginAsyncTask;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.UsuarioApmDAO;
import coop.tecso.daa.ui.utils.ButtonEnabler;

/**
 * 
 * @author tecso.coop
 *
 */
public final class LoginActivity extends Activity { 

	private static final String LOG_TAG = LoginActivity.class.getSimpleName();

	private DAAApplication appState;

	// Views
	private LinearLayout mLoginModeView;
	private EditText mUserIDTextView;
	private EditText mPasswordTextView;
	private Button   mLoginButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);     
		// Setting content View from XML
		setContentView(R.layout.login);      

		// Load views from the XML definition
		mUserIDTextView = (EditText) findViewById(R.id.id_edit);
		mPasswordTextView = (EditText) findViewById(R.id.password_edit);
		mLoginButton = (Button) findViewById(R.id.login_button);
		mLoginModeView = (LinearLayout) findViewById(R.id.login_mode);
	

		// Set up the button enabler
		ButtonEnabler.register(mLoginButton, mUserIDTextView, mPasswordTextView);
	}

	@Override
	protected void onResume() {
		Log.i(LOG_TAG, "onResume");
		super.onResume();

		this.appState = (DAAApplication) getApplication();
		setTitle(appState.getTitle());

		// Si se perdio la session se vuelve a validar el modo de login
		if(appState.getCurrentUser() == null){
			UsuarioApmDAO usuarioApmDAO = DAOFactory.getInstance().getUsuarioApmDAO();
			if(usuarioApmDAO.countOfActive() == 0L){
				// Realizar login remoto
				appState.setLocalLogin(false);
			}else{
				// Realizar login local
				appState.setLocalLogin(true);
			}
		}
		this.refreshLoginIndicator();
	}

	// ------------------------------------------------------
	// Main menu 
	// ------------------------------------------------------

	// Menu builder
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return true;
	}

	// Reaction to the menu selection
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:			
			doChangeSettings();
			return true;
		case R.id.exit:			
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	// ------------------------------------------------------
	// Event handlers 
	// ------------------------------------------------------
	public void doChangeSettings() {
		Log.d(LOG_TAG, "doChangeSettings: enter");
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
	}

	public void DoLoginAction(View v) {
		Log.d(LOG_TAG, "DoLoginAction: enter");

		// Validate required fields
		String username = mUserIDTextView.getText().toString();
		String password = mPasswordTextView.getText().toString();

		boolean hasErrors = false;
		if (TextUtils.isEmpty(username)) {
			mUserIDTextView.setError(getString(R.string.login_id_error));
			hasErrors = true;
		}  

		if (TextUtils.isEmpty(password)) {
			mPasswordTextView.setError(getString(R.string.login_password_error));
			hasErrors = true;
		} 

		// Realizo flujo de Login
		if (!hasErrors) {
			// LoginAsyncTask >> InitAppAsyncTask
			new LoginAsyncTask(this).execute(username, password);
		}

		// Se limpia el campo de password
		mPasswordTextView.setText("");
		Log.d(LOG_TAG, "DoLoginAction: exit");
	}

	
	private void refreshLoginIndicator(){
		if(appState.isLocalLogin()){
			mLoginModeView.setBackgroundColor(this.getResources().getColor(R.color.local_login_color));
		}else{
			mLoginModeView.setBackgroundColor(this.getResources().getColor(R.color.remote_login_color));
		}
	}
}