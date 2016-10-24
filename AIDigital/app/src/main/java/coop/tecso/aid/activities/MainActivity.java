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

package coop.tecso.aid.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.common.DAAServiceImpl;
import coop.tecso.aid.common.WebServiceController;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.entities.Alcoholimetro;
import coop.tecso.aid.entities.ClaseLicencia;
import coop.tecso.aid.entities.EstadoTipoFormulario;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.Infraccion;
import coop.tecso.aid.entities.LineaTUP;
import coop.tecso.aid.entities.MarcaVehiculo;
import coop.tecso.aid.entities.Medico;
import coop.tecso.aid.entities.MotivoAnulacionTipoFormulario;
import coop.tecso.aid.entities.MotivoCierreTipoFormulario;
import coop.tecso.aid.entities.Serie;
import coop.tecso.aid.entities.TelefonoPanico;
import coop.tecso.aid.entities.TipoDocumento;
import coop.tecso.aid.entities.TipoFormulario;
import coop.tecso.aid.entities.TipoVehiculo;
import coop.tecso.aid.gui.utils.Utils;
import coop.tecso.aid.helpers.AuthManager;
import coop.tecso.aid.helpers.GUIHelper;
import coop.tecso.aid.helpers.ParamHelper;
import coop.tecso.aid.helpers.SearchPageAdapter;
import coop.tecso.aid.helpers.SearchPageAdapter.ViewHolder;
import coop.tecso.aid.services.AIDigitalManager;
import coop.tecso.aid.services.SyncFormularioService;
import coop.tecso.aid.services.SyncPanicoService;
import coop.tecso.aid.utils.Constants;
import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.base.TablaVersion;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;
import coop.tecso.daa.domain.util.DeviceContext;
 
/**
 * 
 * @author tecso.coop
 *
 */
public class MainActivity extends ListActivity {

	private static final String LOG_TAG = MainActivity.class.getSimpleName();
	
	private List<AplicacionPerfil> aplicacionPerfilList = new ArrayList<AplicacionPerfil>();
	//Declaramos un intentFilter para escuchar un brodcast de formulario enviado
	private IntentFilter filter = new IntentFilter("coop.tecso.aid.FormEnviado"); 

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		this.appState = (AIDigitalApplication) getApplicationContext();
		//
		this.appState.initLockTime();
		this.setContentView(R.layout.main);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(getIntent().getBooleanExtra(Constants.CHECK_PRINTER, false)){
			getIntent().putExtra(Constants.CHECK_PRINTER, false);
			return;
		}
		this.registerReceiver(broadCastReceiver, filter);
		
		new BuildSearchPageTask().execute();
	}

	@Override
	public void onUserInteraction(){
		super.onUserInteraction();
		this.appState.checkLock();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// verify access
		if (!appState.canAccess()){
			GUIHelper.showErrorLoseSession(this);
			return true;
		}

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.changeOrientation:
			changeOrientationForm();
			return true;
		case R.id.closeApplication:
			closeApplication();
			return true;
		case R.id.registerForm:
			buildSelectPerfilDialog();
			return true;
		case R.id.changePin:
			changePINForm();
			return true;
		case R.id.licencia:
			dialogLicencia();
			return true;
		case R.id.checkPrinter:
			DeviceContext.disableBluetooth();
			checkPrinterStatus();
			return true;
		case R.id.sendAlert:
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle(R.string.confirm_title);
			alertDialog.setMessage(R.string.alert_confirm_msg);
			alertDialog.setCancelable(false);
			alertDialog.setIcon(R.drawable.ic_error_default);
			alertDialog.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							new AIDigitalManager(MainActivity.this).sendAlertaPanico();
							GUIHelper.showMessage(MainActivity.this, "¡Alerta disparada!");
							dialog.dismiss();
						}
					});
			alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			alertDialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOG_TAG, "onActivityResult: enter");
		// 
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case RegisterActivity.BT_STATUS:
				getIntent().putExtra(Constants.CHECK_PRINTER, true);
				checkPrinterStatus();
				break;
			}
		}
		// Return key is pressed
		if(resultCode == RESULT_CANCELED){

		}
		Log.i(LOG_TAG, "onActivityResult: exit");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(LOG_TAG, "onListItemClick: enter");
		// Verifica perdida de session
		if (!appState.canAccess()) {
			GUIHelper.showErrorLoseSession(this);
			return;
		}

		Formulario formulario = (Formulario) getListAdapter().getItem(position);

		Bundle bundle = new Bundle();
		bundle.putString(Constants.ACTION, Constants.ACTION_UPDATE);
		bundle.putInt(Constants.ENTITY_ID, formulario.getId());

		Intent intent = new Intent(this, RegisterActivity.class);
		intent.putExtras(bundle);

		startActivity(intent);
	}
	
	@Override
	protected void finalize() throws Throwable {
		stopService(new Intent(this, SyncFormularioService.class));
		stopService(new Intent(this, SyncPanicoService.class));
		super.finalize();
	}

	/**
	 * 
	 * @param view
	 */
	private void changePINForm(){
		// Verifica perdida de session
		if (!appState.canAccess()) {
			GUIHelper.showErrorLoseSession(this);
			return;
		}
		//
		new AuthManager(this).dialogChangePIN().show();
	}

	/**
	 * 
	 * @param view
	 */
	private void dialogLicencia(){
		// Verifica perdida de session
		if (!appState.canAccess()) {
			GUIHelper.showErrorLoseSession(this);
			return;
		}
		//
		new AIDigitalManager(this).dialogLicencia().show();
	}
	
	
	/**
	 * 
	 * @param view
	 */
	private void checkPrinterStatus(){
		//TODO:
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(intent, RegisterActivity.BT_STATUS);
			return;
		} 
		//
		new CheckPrinterTask().execute();
	}

	/**
	 *  Registrar nuevo Formulario.
	 * 
	 * @param view
	 */
	public void DoRegisterForm(View view){
		// Verifica perdida de session
		if (!appState.canAccess()) {
			GUIHelper.showErrorLoseSession(this);
			return;
		}

		// Verifico Zona Horaria
		if(!isCorrectTimeZone()){
			Log.d(LOG_TAG, "TimeZone: muestro el mensaje");
			GUIHelper.showAlert(this, 
					getString(R.string.timeZone_error_title), 
					getString(R.string.timeZone_error_msg), false);
			return;
		}

		// Verifico formularios Pendientes
		Long count = DAOFactory.getInstance().getFormularioDAO().countOfPending();
		if(count > 0){
			GUIHelper.showAlert(this, 
					getString(R.string.register_error_title), 
					getString(R.string.register_error_msg), false);
			return;
		}

		// Verifico seteo de PIN
		AuthManager auth = new AuthManager(this);
		if(!auth.isPINSaved()){
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.verify_pin_title));
			alertDialog.setMessage(getString(R.string.verify_pin_msg));
			alertDialog.setCancelable(false);
			alertDialog.setIcon(R.drawable.ic_error_default);
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
					getString(R.string.accept),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					alertDialog.dismiss();
					changePINForm();
				}});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
					getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					alertDialog.dismiss();
				}});
			alertDialog.show();
			return;
		}
		
		/*
		 * 
		 */
		buildSelectPerfilDialog();
	}
	
	private boolean isCorrectTimeZone() {  
		Log.d(LOG_TAG, "TimeZone: enrto a isCorrectTimeZone()");
		TimeZone tzDefault = TimeZone.getDefault();
		int offsetDefault = tzDefault.getRawOffset();
		Log.d(LOG_TAG, "TimeZone: offset default = "+offsetDefault);
		TimeZone argentinaTZ = TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
		int offsetArgentina = argentinaTZ.getRawOffset();
		Log.d(LOG_TAG, "TimeZone: retorno = "+(offsetArgentina == offsetDefault));
		return(offsetArgentina == offsetDefault);
	}

	/**
	 * 
	 */
	private void changeOrientationForm(){
		int orientation = appState.getOrientation();
		if(orientation == LinearLayout.VERTICAL){
			appState.setOrientation(LinearLayout.HORIZONTAL);
			Toast.makeText(this, getText(R.string.app_horizontal_position_msg),Toast.LENGTH_SHORT).show();
		}else{
			appState.setOrientation(LinearLayout.VERTICAL);
			Toast.makeText(this,getText(R.string.app_vertical_position_msg),Toast.LENGTH_SHORT).show();
		}

		SharedPreferences.Editor prefEditor = getSharedPreferences(appState.getClass().getName(), MODE_PRIVATE).edit();
		prefEditor.putInt(Constants.PREF_DEFAULT_ORIENTATION, appState.getOrientation()); 
		prefEditor.commit();
	}


	/**
	 * 
	 */
	private void closeApplication(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.confirm_title)
		.setMessage(R.string.exit_confirm_msg)
		.setCancelable(false)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				appState.hasSynchronized = false;
				finish();
			}
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}


	/**
	 * 
	 */
	private void buildSelectPerfilDialog(){
		Log.i(LOG_TAG, "buildSelectFormPrompt: enter");

		// Adapter de Opciones
		ArrayAdapter<AplicacionPerfil> adapter;
		adapter = new ArrayAdapter<AplicacionPerfil>(this, android.R.layout.simple_list_item_1, aplicacionPerfilList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					holder.setTextColor(getResources().getColor(android.R.color.white));
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.form_title));
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				//
				Bundle bundle = new Bundle();
				bundle.putString(Constants.ACTION, Constants.ACTION_CREATE);
				bundle.putInt(Constants.ENTITY_ID, aplicacionPerfilList.get(which).getId());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		//
		Dialog selectPerfilDialog =  builder.create();
		selectPerfilDialog.show();
	}

	/**
	 * 
	 * @author tecso.coop
	 *
	 */
	private class CheckPrinterTask extends AsyncTask<Void, Void, Void> {
		private AIDigitalManager manager;
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(MainActivity.this,"",
					getString(R.string.printing_msg), true);
			this.manager = new AIDigitalManager(MainActivity.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Binding service
				Thread.sleep(1000L);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			this.manager.checkPrinterStatus();
			dialog.dismiss();
			DeviceContext.disableBluetooth();
		}
	}


	//	/**
	//	 * 
	//	 * @author tecso.coop
	//	 *
	//	 */
	//	private class CheckPrinterTask extends AsyncTask<Void, CharSequence, Void> {
	//		private DAAServiceImpl localService;
	//		@Override
	//		protected void onPreExecute() {
	//			Toast.makeText(MainActivity.this, 
	//					"Imprimiendo texto de prueba…",Toast.LENGTH_LONG).show();
	//			this.localService = new DAAServiceImpl(MainActivity.this);
	//		}
	//		@Override
	//		protected Void doInBackground(Void... params) {
	//			try {
	//				Thread.sleep(1000);
	//				this.localService.checkPrinterStatus();
	//			} catch (Exception e) {
	//				Log.d(LOG_TAG, "**ERROR**", e);
	//			}
	//			return null;
	//		}
	//	}
	
	//Cuando llega el broadcast "coop.tecso.aid.FormEnviado", modificamos en la vista el estado del formulario que se acaba de enviar
	private BroadcastReceiver broadCastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context c, Intent intent) {

			Bundle bundle = intent.getExtras();
			int id_form = bundle.getInt("ID_FORM");
			//Toast.makeText(MainActivity.this, "Enviado: "+id_form, Toast.LENGTH_LONG).show();
			
			ListView aux=(ListView) findViewById(android.R.id.list);
			
			
			try {
				int cant_elementos = getListView().getAdapter().getCount();
				SearchPageAdapter.ViewHolder viewHolder = (ViewHolder) aux.getChildAt(cant_elementos-id_form).getTag();
				Formulario formulario = (Formulario) getListAdapter().getItem(cant_elementos-id_form);
				
				DAOFactory.getInstance().getFormularioDAO().refresh(formulario);
				
				viewHolder.syncronizedTextView.setText(R.string.synchronized_item_label);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
			}
		}
	};

	/**
	 * 
	 *  @author tecso.coop
	 *
	 */
	private class BuildSearchPageTask extends AsyncTask<Void, CharSequence, Boolean> {
		private ProgressDialog dialog;
		private MainActivity context = MainActivity.this;
		private DAAServiceImpl localService;
		private WebServiceController webService;

		private DAOFactory daoFactory;
		private Map<String, Integer> remoteStatusMap;
		private Map<String, Integer> localStatusMap;
		private String message;

		private List<Formulario> formularioList = new ArrayList<Formulario>();

		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(context,"",context.getString(R.string.loading_msg), true);
			this.localService = new DAAServiceImpl(context);
			daoFactory = DAOFactory.getInstance();
			remoteStatusMap = new HashMap<String, Integer>();
			localStatusMap  = new HashMap<String, Integer>();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				// Validaciones Iniciales
				// Usuario Logueado (Chequeo de session)
				UsuarioApm currentUser;
				if (appState.canAccess()) {
					currentUser = appState.getCurrentUser();
				} else {
					currentUser = localService.getCurrentUser();
				}

				if (currentUser == null){
					throw new Exception("Perdió sesión con MR-Admin. Intente loguearse nuevamente");
				} 

				if (!localService.hasAccess(currentUser.getId(),Constants.COD_APPLICATION)) {
					throw new Exception("Usuario sin permisos para acceder a la aplicación");
				}
				appState.setCurrentUser(currentUser);

				if (!appState.hasSynchronized) {
					appState.hasSynchronized = true;
					publishProgress(context.getString(R.string.synchronizing_msg));
					// Sincronizo Datos
					DispositivoMovil dispositivoMovil = localService.getDispositivoMovil();
					appState.setDispositivoMovil(dispositivoMovil);
					try {

						String serverURL = localService.getServerURL();
						appState.setServerURL(serverURL);
						webService = WebServiceController.getInstance(context);

						// Se sincroniza y carga versiones de tablas
						// Build remote status 
						List<TablaVersion> tablaVersionList = webService.syncTablaVersionList(Constants.COD_APPLICATION);
						Log.d(LOG_TAG, "Remote status: ");
						for (TablaVersion tablaVersion : tablaVersionList) {
							Log.d(LOG_TAG, "## "+tablaVersion.getTabla() + " --> " + tablaVersion.getLastVersion());
							remoteStatusMap.put(tablaVersion.getTabla().toLowerCase(Locale.getDefault()), tablaVersion.getLastVersion());
						}
						// Build local status 
						Log.d(LOG_TAG, "Local status: ");
						tablaVersionList = daoFactory.getTablaVersionDAO().findAllActive();
						for (TablaVersion tablaVersion : tablaVersionList) {
							Log.d(LOG_TAG, "## "+tablaVersion.getTabla() + " --> " + tablaVersion.getLastVersion());
							localStatusMap.put(tablaVersion.getTabla().toLowerCase(Locale.getDefault()), tablaVersion.getLastVersion());
						}

						// Datos Maestros 
						publishProgress(context.getString(R.string.synchronizing_item_msg, "datos maestros"));
						synchronize(Infraccion.class);
						synchronize(TipoDocumento.class);
						synchronize(TipoFormulario.class);
						synchronize(TipoVehiculo.class);
						synchronize(MarcaVehiculo.class);
						synchronize(ClaseLicencia.class);
						synchronize(EstadoTipoFormulario.class);
						synchronize(LineaTUP.class);
						synchronize(Alcoholimetro.class);
						synchronize(Medico.class);
						// Parametros
						publishProgress(context.getString(R.string.synchronizing_item_msg, "parámetros"));
						synchronize(AplicacionParametro.class);
						
						// Telefonos Panico
						synchronize(TelefonoPanico.class);

						publishProgress(context.getString(R.string.synchronizing_item_msg, "motivos de cierre"));
						synchronize(MotivoCierreTipoFormulario.class);
						synchronize(MotivoAnulacionTipoFormulario.class);
						// Domicilios
						publishProgress(context.getString(R.string.synchronizing_item_msg, "información de calles"));
						synchronize(AplicacionBinarioVersion.class);
						// Talonario
						publishProgress(context.getString(R.string.synchronizing_item_msg, "talonario"));
						synchronize(Serie.class);
					} catch (Exception e) {
						// Si falla la sincronizacion al WS se continua en modo desconectado
						Log.d(LOG_TAG, "BuildSearchPageTask: ***NO SYNC***", e);
					}

					// Inicializo caches
					// ==================
					publishProgress(context.getString(R.string.initializing_item_msg, "cache"));
					//TODO: ###PARAM_CACHE###
					ParamHelper.initialize();
					
					// Elimino Formularios sincronizados
					int hours = 6;
					try {
						hours = ParamHelper.getInteger(ParamHelper.UMBRAL_ELIMINACION);
					} catch (Exception e) {
						Log.e(LOG_TAG, "**ERROR**", e);
					}
					DAOFactory.getInstance().getFormularioDAO().deleteSynchronized(hours);
				}

				//TODO: ###SERVICES###
				if(daoFactory.getFormularioDAO().countOfActive() > 0){
					Intent intent = new Intent(context , SyncFormularioService.class);
					intent.putExtra("period", 5000L);
					context.startService(intent);
				}
				
				if(daoFactory.getPanicoDAO().countOfActive() > 0){
					Intent intent = new Intent(context , SyncPanicoService.class);					
					context.startService(intent);
				}

				// AplicacionPerfil 
				aplicacionPerfilList = localService.getListAplicacionPerfilByAplicacion(Constants.COD_APPLICATION);

				// Formularios
				formularioList = daoFactory.getFormularioDAO().findAllOrdered();

				return true;
			} catch(ReplyException e){
				Log.d(LOG_TAG, "**ERROR**", e);
				message = e.getCode() +" - "+e.getMessage();
				return false;
			} catch (Exception e) {
				Log.d(LOG_TAG, "**ERROR**", e);
				message = e.getMessage();
				return false;
			}finally{
				//TODO:
				//				OpenHelperManager.releaseHelper();
			}
		}

		@Override
		protected void onProgressUpdate(CharSequence... values) {
			dialog.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			DeviceContext.disableBluetooth();
			if (!result) {
				GUIHelper.showAlert(context,"Error de Sesión", message);
				return;
			}
			
			SearchPageAdapter adapter = new SearchPageAdapter(context, formularioList);
			setListAdapter(adapter);

			context.setTitle(Utils.getFormattedTitle(context));
		}

		/**
		 * 
		 * @param clazz 
		 * @throws Exception
		 */
		private <T extends AbstractEntity> void synchronize(Class<T> clazz) throws Exception{
			Log.i(LOG_TAG, String.format("Sincronizando %s ...", clazz.getSimpleName()));
			String key = clazz.getSimpleName().toLowerCase(Locale.getDefault());

			boolean forceUpdate = appState.getDispositivoMovil().getForzarActualizacion() == 1;

			int localVersion  = localStatusMap.get(key).intValue();
			int remoteVersion = remoteStatusMap.get(key).intValue();
			
			Log.i(LOG_TAG, "SINCRONIZANDO: "+clazz.getSimpleName()+"local="+localVersion+" remote="+remoteVersion);

			if(localVersion < remoteVersion || forceUpdate){
				// Sync binary file
				if (clazz.equals(AplicacionBinarioVersion.class)) {
					webService.syncEntityDeltaBinary(appState.getCurrentUser().getId(), forceUpdate);
					return;
				}
				// Sync by Application
				if (clazz.equals(AplicacionParametro.class)) {
					webService.syncEntityDeltaByAplicacion(clazz, forceUpdate);
					return;
				}
				// Sync by Area
				if (clazz.equals(TelefonoPanico.class)) {
					webService.syncEntityDeltaByArea(clazz, forceUpdate);
					return;
				}
				// Sync by Delta
				webService.syncEntityDelta(clazz, forceUpdate);
			}
		}
	}

	private AIDigitalApplication appState;
}