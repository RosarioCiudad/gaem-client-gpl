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

package coop.tecso.aid.helpers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import coop.tecso.aid.R;
import coop.tecso.aid.activities.AnularFormActivity;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.entities.TipoDocumento;
import coop.tecso.aid.utils.ButtonEnabler;

/**
 * 
 * @author tecso.coop
 *
 */
public class AuthManager {
	private static final String LOG_TAG = AuthManager.class.getSimpleName();
	private Context context;
	private AIDigitalApplication appState;

	/**
	 * 
	 * @param context
	 */
	public AuthManager(Context context) {
		this.context = context;
		this.appState = (AIDigitalApplication) context.getApplicationContext();
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String encodePIN(String input){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(input.getBytes("ISO-8859-1"), 0, input.length());

			return Base64.encodeToString(md.digest(), Base64.DEFAULT);
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			return input;
		}
	}
	
	/**
	 * 
	 * Construye un dialog para el seteo de PIN
	 * 
	 * @return 
	 */
	public Dialog dialogChangePIN(){

		final Dialog dialog  = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.dialog_set_pin);
		dialog.setTitle(context.getString(R.string.set_pin_title));
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_login_mode);
		dialog.setCancelable(true);

		// ID Usuario
		final TextView userTextView = (TextView) dialog.findViewById(R.id.username_edit_text);
		userTextView.setText(appState.getCurrentUser().getUsername());
		// Password
		final EditText passEditText = (EditText) dialog.findViewById(R.id.password_edit_text);
		// PIN
		final EditText pinEditText = (EditText) dialog.findViewById(R.id.pin_edit_text);

		// Cancel
		Button buttonCancel = (Button) dialog.findViewById(R.id.set_pin_button_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// Confirm
		Button buttonConfirm = (Button) dialog.findViewById(R.id.set_pin_button_confirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = userTextView.getText().toString();
				String password = passEditText.getText().toString();
				String inputpin = pinEditText.getText().toString();

				String base64Cert = appState.getCurrentUser().getUsercert();
				// Password
				passEditText.setError(null);
				if(!cerficateLogin(base64Cert, password)){
					passEditText.setError(context.getString(R.string.set_pin_password_label) +" incorrecta");
					passEditText.requestFocus();
					return;
				}
				// PIN 
				pinEditText.setError(null);
				if(inputpin.length() < 4){
					pinEditText.setError(context.getString(R.string.set_pin_pin_label) +" debe ser de 4 digitos");
					pinEditText.requestFocus();
					return;
				}
				SharedPreferences.Editor prefEditor = 
						context.getSharedPreferences(appState.getClass().getName(), Context.MODE_PRIVATE).edit();
				prefEditor.clear();
				//prefEditor.putString(username, encodePIN(inputpin));
				prefEditor.putString(username, inputpin);

				prefEditor.commit();

				Toast.makeText(context, String.format("Seteo de %s exitoso",
						context.getString(R.string.set_pin_pin_label)), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		ButtonEnabler.register(buttonConfirm, passEditText, pinEditText);

		return dialog;
	}

	/**
	 *  Construye un dialog para el chequeo de PIN
	 *  
	 * @return
	 */
	public Dialog dialogVerifyPIN(){
		final Dialog dialog  = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.dialog_verify_pin);
		dialog.setTitle(context.getString(R.string.verify_pin_title));
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_login_mode);
		dialog.setCancelable(true);

		// ID Usuario
		final TextView userTextView = (TextView) dialog.findViewById(R.id.username_edit_text);
		userTextView.setText(appState.getCurrentUser().getUsername());
		// PIN
		final EditText pinEditText = (EditText) dialog.findViewById(R.id.pin_edit_text);

		// Confirm
		Button buttonConfirm = (Button) dialog.findViewById(R.id.verify_pin_confirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String username = userTextView.getText().toString();
				String inputpin = pinEditText.getText().toString();

				// PIN
				pinEditText.setError(null);
				if(inputpin.length() < 4){
					pinEditText.setError(context.getString(R.string.set_pin_pin_label) +" debe ser de 4 digitos");
					return;
				}
				// Encodeo el PIN ingresado
				//String encodedPIN = encodePIN(inputpin);
				// Recupero el PIN almacenado localmente
				SharedPreferences sharedPreferences = 
						context.getSharedPreferences(appState.getClass().getName(), Context.MODE_PRIVATE);
				String storedPIN = sharedPreferences.getString(username, "");
				// Comparo 
				//if(encodedPIN.equals(storedPIN)){
				if(inputpin.equals(storedPIN)){
					dialog.dismiss();
				}else{
					pinEditText.setError(context.getString(R.string.set_pin_pin_label) +" incorrecto");
				}
			}
		});
		ButtonEnabler.register(buttonConfirm, pinEditText);

		return dialog;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPINSaved(){
		SharedPreferences sharedPreferences = 
				context.getSharedPreferences(appState.getClass().getName(), Context.MODE_PRIVATE);
		String pin = sharedPreferences.getString(appState.getCurrentUser().getUsername(), "");
		return !TextUtils.isEmpty(pin);
	}

	/**
	 * 
	 * @param encBase64Cert - Encrypted certificate in Base64
	 * @param password - to open the certificate
	 * @return
	 */
	private boolean cerficateLogin(String encBase64Cert, String password){
		Log.d(LOG_TAG, "cerficateLogin: enter");
		boolean isValid;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12","BC");
			InputStream certStream = new ByteArrayInputStream(Base64.decode(encBase64Cert, Base64.DEFAULT));
			keyStore.load(certStream, password.toCharArray());
			Log.i(LOG_TAG, "validating credentials... OK");
			isValid = true;
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**: " + e.getMessage());
			isValid = false;
		}
		Log.d(LOG_TAG, "cerficateLogin: exit");
		return isValid;
	}
}