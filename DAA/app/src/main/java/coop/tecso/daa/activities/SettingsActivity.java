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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import coop.tecso.daa.R;
import coop.tecso.daa.domain.util.DeviceContext;
import coop.tecso.daa.ui.MenuSectionAdapter;
import coop.tecso.daa.ui.SectionedListAdapter;

/**
 * Settings activity.
 */
public final class SettingsActivity extends Activity 
									implements OnItemClickListener {

	private static final int DIALOG_EDIT_URL = 1;
	private static final int DIALOG_EDIT_EMAIL = 2;
	
	private ListView mOptionsListView;
	private SectionedListAdapter optionsAdapter;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);
					
		// create our list and custom adapter
		optionsAdapter = new SectionedListAdapter(this);
		
		MenuSectionAdapter section1Adapter = new MenuSectionAdapter(this);
//		section1Adapter.addTextView("Servidor Web", "Configure la dirección del servidor web de la aplicación.");
		
    	String emailAccount = DeviceContext.getEmail(getApplicationContext());			
		section1Adapter.addTextView(getApplicationContext().getString(R.string.settings_correo, emailAccount),
				getString(R.string.email_account_msg));
		
    	String imeiNumber = DeviceContext.getDeviceId(getApplicationContext());			
		section1Adapter.addTextView(getApplicationContext().getString(R.string.settings_imei, imeiNumber),
				getString(R.string.imei_number_msg));
		
		//MenuSectionAdapter section2Adapter = new MenuSectionAdapter(this);
		//section2Adapter.addCheckBoxView("Recordar contraseñas", "Guarda las contraseñas en la memoria del dispositivo");		

		optionsAdapter.addSection("Conexión", section1Adapter);
		//optionsAdapter.addSection("Seguridad", section2Adapter);

		mOptionsListView = (ListView) findViewById(R.id.optionsListView);
		mOptionsListView.setAdapter(optionsAdapter);
		mOptionsListView.setOnItemClickListener(this);
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {		
		showDialog(position);
	}

	@Override
    protected Dialog onCreateDialog(int id) {
		// Inflate views
		LayoutInflater factory = LayoutInflater.from(this);
		View textEntryView = factory.inflate(R.layout.dialog_edit_text, null);
		final TextView textView = (TextView) textEntryView.findViewById(R.id.dialogtextview);
		final EditText editText = (EditText) textEntryView.findViewById(R.id.dialogedittext);
		final SharedPreferences myPrefs = getSharedPreferences("settings", MODE_PRIVATE);		

		switch (id) {
    	case DIALOG_EDIT_URL:
	        editText.setText(myPrefs.getString("URL", getString(R.string.server_url)).replace("https://", ""));
	        return new AlertDialog.Builder(SettingsActivity.this)            
	            .setTitle("Servidor Web")
	            .setView(textEntryView)
	            .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
					@Override
	            	public void onClick(DialogInterface dialog, int which) {
	                	SharedPreferences.Editor e = myPrefs.edit();
	                	e.putString("URL", "https://" + editText.getText().toString()); 
	                	e.commit();
	                }
	            })
	            .create();    
    	case DIALOG_EDIT_EMAIL:
    		//none
		}    	
    	return null;
    }
}