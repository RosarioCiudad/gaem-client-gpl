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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import coop.tecso.daa.R;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.common.GUIHelper;
import coop.tecso.daa.dao.AplicacionDAO;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.NotificacionDAO;
import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.notificacion.Notificacion;
import coop.tecso.daa.services.SaveUbicacionService;
import coop.tecso.daa.services.SyncUbicacionService;
import coop.tecso.daa.utils.Constants;

/**
 * Listado de Notificaciones.
 * 
 *  @author tecso.coop
 */
public class NotificacionesActivity extends ListActivity {

	private DAAApplication appState;

	@Override
	public void onCreate(Bundle icicle) {
		Log.d(LOG_TAG, "onCreate init...");		
		super.onCreate(icicle);

		this.appState = (DAAApplication) getApplicationContext();

		Log.d(LOG_TAG, "Validating permissions...");

		if(canAccess()){
			//
			startService(new Intent(this, SaveUbicacionService.class));
			startService(new Intent(this, SyncUbicacionService.class));
			//
			setContentView(R.layout.notificaciones);
			this.chequearFechayHora();
			buildAplLinksArea();			
		}else{
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(appState.getCurrentUser() == null){
			finish();
		}else{
			Log.d(LOG_TAG, "Building ListView...");
			new NotificationTask().execute();
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Close the application on Back
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			moveTaskToBack(true);
		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------------------------------------
	// Main menu 
	// ------------------------------------------------------
	// Menu builder
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}

	// Handle click on Notifiacion item.
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {   
		// Show the detail
		Notificacion notificacion = adapter.getItem(position);

		final AlertDialog alertDialog = new AlertDialog.Builder(NotificacionesActivity.this).create();
		alertDialog.setTitle(notificacion.getDescripcionReducida());
		alertDialog.setMessage(notificacion.getDescripcionAmpliada());
		alertDialog.setCancelable(false);
		alertDialog.setIcon(android.R.drawable.ic_menu_info_details);
		alertDialog.setButton(getString(R.string.accept), 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				alertDialog.dismiss();
			}});
		alertDialog.show();

		super.onListItemClick(l, v, position, id);
	}

	// Reaction to the menu selection
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:			
			closeApplication();

			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}    


	@Override
	public void finish() {

		sendBroadcast(new Intent(Constants.ACTION_LOSE_SESSION));
		// 
		stopService(new Intent(NotificacionesActivity.this, SaveUbicacionService.class));
		stopService(new Intent(NotificacionesActivity.this, SyncUbicacionService.class));

		appState.reset();

		super.finish();
	}

	@Override
	public void onDestroy() {
		stopService(new Intent(NotificacionesActivity.this, SaveUbicacionService.class));
		stopService(new Intent(NotificacionesActivity.this, SyncUbicacionService.class));
		super.onDestroy();
	}

	/**
	 * 
	 */
	private void closeApplication(){
		// Verifica perdida de session
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.confirm_title)
		.setMessage(R.string.exit_confirm_msg)
		.setCancelable(false)
		.setPositiveButton(R.string.yes, 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//
				finish();
			}})
			.setNegativeButton(R.string.no, 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

		AlertDialog alert = builder.create();
		alert.show();
	}


	/**
	 * 
	 * @author tecso.coop
	 *
	 */
	private class NotificationTask extends AsyncTask<Void, CharSequence, List<Notificacion>> {
		private ProgressDialog mDialog;
		private NotificacionesActivity currentActivity = NotificacionesActivity.this;
		private NotificacionDAO notificacionDAO;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			notificacionDAO = DAOFactory.getInstance().getNotificacionDAO();
			// Show status message
			mDialog = GUIHelper.getIndetProgressDialog(currentActivity,"", getString(R.string.loading_msg));
		}

		@Override
		protected List<Notificacion> doInBackground(Void... arg0) {	    	
			try {
				// Prepare the listView
				return notificacionDAO.findAllActive();
			} catch(Exception e) {
				Log.e(LOG_TAG, "Error iniciando sesion", e);
				return null;
			}	     
		}   

		@Override
		protected void onProgressUpdate(CharSequence... values) {
			mDialog.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(List<Notificacion> result) {
			mDialog.dismiss();
			if (null == result)  {
				GUIHelper.showError(currentActivity, "Error al obtener notificaciones");
				currentActivity.finish();
			} else {
				//
				currentActivity.setTitle(appState.getFormattedTitle());
				adapter = new NotificacionArrayAdapter(getApplicationContext(), R.layout.list_item_notificacion, result);
				setListAdapter(adapter);
			}
		}
	}

	private boolean canAccess() {
		Log.d(LOG_TAG, "Validate user");
		if (appState.getCurrentUser() != null) { 
			return true;			
		}

		Log.d(LOG_TAG, "Ask for authentication...");
		// Start the Login Activity
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);        
		return false;
	}

	/**
	 * Se cargan los links a aplicaciones habilitadas en el perfil del usuario
	 * 
	 */
	private void buildAplLinksArea() {
		final Context context = this; 
		AplicacionDAO aplicacionDAO = DAOFactory.getInstance().getAplicacionDAO();

		List<Aplicacion> listAplicacion = aplicacionDAO.findAllByUsuarioId(appState.getCurrentUser().getId());
		if(!listAplicacion.isEmpty()){
			for (Aplicacion aplicacion : listAplicacion) {
				// Para cada aplicacion se crea link de acceso
				LayoutInflater inflater = this.getLayoutInflater();
				View aplLinkView = inflater.inflate(R.layout.apl_link, null);
				TextView  aplCode  = (TextView) aplLinkView.findViewById(R.id.apl_code);
				aplCode.setText(aplicacion.getCodigo());
				TextView  aplDescription  = (TextView) aplLinkView.findViewById(R.id.apl_description);
				aplDescription.setText(aplicacion.getDescripcion());

				final String aplPkg =  aplicacion.getPackageName()!=null?aplicacion.getPackageName():"";  
				final String aplClass = aplicacion.getClassName()!=null?aplicacion.getClassName():""; 
				aplLinkView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Verificar Lockeo
						//						appState.checkLock(NotificacionesActivity.this);
						//				    	if(appState.isBlocked()){
						//				    		return;
						//				    	}

						Intent intent;
						// Custom Application Launcher
						intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setComponent(new ComponentName(aplPkg, aplClass));

						if(GUIHelper.canHandleIntent(appState, intent)){
							appState.startActivity(intent);
							return;
						}

						// Acceso a Apl incorrecto
						String err = "No se puede ejecutar la aplicación. \nPor favor, contactese con el administrador.";
						final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("Error al ejecutar Aplicación");
						alertDialog.setMessage(err);
						alertDialog.setCancelable(false);
						alertDialog.setIcon(R.drawable.ic_error_default);
						alertDialog.setButton(appState.getString(R.string.accept), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								alertDialog.dismiss();
							}
						});
						alertDialog.show();
						return; 
					}
				});
				LinearLayout listAplLinksView = (LinearLayout) this.findViewById(R.id.listAplLink);
				listAplLinksView.addView(aplLinkView);
				// Separador de links
				View line = new View(this);
				line.setBackgroundColor(this.getResources().getColor(R.color.apl_link_line_color));
				listAplLinksView.addView(line, new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
			}
		}else{
			// Mensaje de lista de links vacia
			LinearLayout emptyLayout = new LinearLayout(this);
			emptyLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			TextView msg = new TextView(this);
			msg.setTextColor(this.getResources().getColor(R.color.white));
			msg.setText(this.getString(R.string.apl_links_empty));
			msg.setTextSize(10);
			msg.setGravity(Gravity.CENTER);
			emptyLayout.addView(msg);
			LinearLayout listAplLinksView = (LinearLayout) this.findViewById(R.id.listAplLink);
			listAplLinksView.addView(emptyLayout);
		}
	}

	// Implementation helpers
	private static final String LOG_TAG = NotificacionesActivity.class.getSimpleName();

	private NotificacionArrayAdapter adapter;

	private void chequearFechayHora() {
		Dialog alertDialog = this.dialogChequeoFecha();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	private String getFechayActual() {
		StringBuilder builder = new StringBuilder();
		Calendar calander = Calendar.getInstance(); 
		int cDay = calander.get(Calendar.DAY_OF_WEEK);

		switch (cDay) {
		case Calendar.SUNDAY:
			builder.append("Domingo");
			break;
		case Calendar.MONDAY:
			builder.append("Lunes");
			break;
		case Calendar.TUESDAY:
			builder.append("Martes");
			break;
		case Calendar.WEDNESDAY:
			builder.append("Miércoles");
			break;
		case Calendar.THURSDAY:
			builder.append("Jueves");
			break;
		case Calendar.FRIDAY:
			builder.append("Viernes");
			break;
		case Calendar.SATURDAY:
			builder.append("Sábado");
			break;
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
		builder.append("   ");
		builder.append(df.format(calander.getTime()));
		return builder.toString();
	}
	
	
	private Dialog dialogChequeoFecha(){
		final Dialog dialog  = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.chequeo_fecha);
		dialog.setCancelable(false);
		LinearLayout container = (LinearLayout) dialog.findViewById(R.id.main_layout);   
		container.setBackgroundColor(Color.RED);
		
		//Campos de Texto
		TextView fechaActual = (TextView) dialog.findViewById(R.id.fechaActual);   
		fechaActual.setText(getFechayActual());
		
		Calendar calander = Calendar.getInstance(); 
		int cHour = calander.get(Calendar.HOUR_OF_DAY);
		int cMinute = calander.get(Calendar.MINUTE);
		TextView horaActual = (TextView) dialog.findViewById(R.id.horaActual);   
		horaActual.setText(cHour+":"+cMinute);
		
		// Confirm
		Button botonConfirmarFecha = (Button) dialog.findViewById(R.id.aceptarFecha);
		botonConfirmarFecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		Button botonCancelarFecha = (Button) dialog.findViewById(R.id.cancelarFecha);
		botonCancelarFecha.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				dialog.cancel();
				NotificacionesActivity.this.finish();
			}
		});
		
		return dialog;
	}
	
}