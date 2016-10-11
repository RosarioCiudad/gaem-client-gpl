package coop.tecso.aid.activities;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.entities.EstadoTipoFormulario;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.MotivoCierreTipoFormulario;
import coop.tecso.aid.gui.components.PerfilGUI;
import coop.tecso.aid.gui.components.PhotoGUI;
import coop.tecso.aid.helpers.AuthManager;
import coop.tecso.aid.helpers.GUIHelper;
import coop.tecso.aid.helpers.SQLHelper;
import coop.tecso.aid.services.AIDigitalManager;
import coop.tecso.aid.utils.ButtonEnabler;
import coop.tecso.aid.utils.Constants;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.util.DeviceContext;

/**
 * 
 * @author tecso.coop
 *
 */
public class RegisterActivity extends Activity {

	private final static String LOG_TAG = RegisterActivity.class.getSimpleName();

	private static PerfilGUI form;
	private AIDigitalApplication appState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.appState = (AIDigitalApplication) getApplicationContext();

		Bundle bundle = getIntent().getExtras();
		String[] params = new String[3];
		// Accion
		params[0] = bundle.getString(Constants.ACTION);
		// EntityID : create[perfilID] | update[formularioID]
		params[1] = String.valueOf(bundle.getInt(Constants.ENTITY_ID));
		// EntityTypeID (Form Type): create[TransitoID|AlcoholemiaID] | update[none]
		params[2] = String.valueOf(bundle.getInt(Constants.ENTITY_TYPE_ID));

		// Register receiver for print action
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Constants.ACTION_PRINT_FORM);
//		registerReceiver(mReceiver, intentFilter);
		
// Para el moto E no apagamos el bluetooth	
//		DeviceContext.disableBluetooth();

		// Render dynamic form
		new RenderFormularioTask().execute(params);
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		try {
//			unregisterReceiver(mReceiver);
//		} catch (Exception e) {}
//	}

	public static final int ANULAR_FORM = 1;
	public static final int CERRAR_FORM = 2;
	public static final int BT_STATUS = 3;
	public static final int REQUEST_NEW_PHOTO = 4;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOG_TAG, "onActivityResult: enter");
		// 
		if(resultCode == RESULT_OK){
			Bundle extras;
			switch (requestCode) {
			case REQUEST_NEW_PHOTO:
				PhotoGUI attacher = (PhotoGUI) form.getCampoGUISelected();
				attacher.setImageBitmap();
//				extras = data.getExtras();
//				attacher.setImageBitmap((Bitmap) extras.get("data"));
				break;
			case CERRAR_FORM:
				extras = data.getExtras();
				int motivoCierreID = extras.getInt(Constants.COD_MOTIVO_CIERRE);
				// Execute Close Task
				new CloseFormTask().execute(motivoCierreID);
				break;
			case ANULAR_FORM:
				extras = data.getExtras();
				int motivoAnulacionID = extras.getInt(Constants.COD_MOTIVO_ANULACION);
				// Execute Anular Task
				new AnularFormTask().execute(motivoAnulacionID);
				break;
			case BT_STATUS:
				doPrintForm();
				break;
			}
		}
		// Return key is pressed
		if(resultCode == RESULT_CANCELED){

		}
		Log.i(LOG_TAG, "onActivityResult: exit");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Verifica perdida de session
		if (!appState.canAccess()) {
			GUIHelper.showError(this, "Se perdió la sesión.");
			RegisterActivity.this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		// Verifica perdida de session
		if (!appState.canAccess()) {
			GUIHelper.showError(this, "Se perdió la sesión.");
			RegisterActivity.this.finish();	
		}
		
		Formulario formulario = (Formulario) form.getEntity();
		// Verifica Formulario
		if(formulario.getEstadoTipoFormulario().getId() !=
				EstadoTipoFormulario.ID_EN_PREPARACION || !form.isDirty()){
			RegisterActivity.this.finish();
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.exit_form_title)
		.setMessage(R.string.exit_form_warning)
		.setCancelable(false)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialogVerifyPIN(ANULAR_FORM).show();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.register_option_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Bundle bundle = getIntent().getExtras();
		// Si edito el form, no permito anular|cerrar
		String action = bundle.getString(Constants.ACTION);
		if(Constants.ACTION_CREATE.equals(action)){
			menu.findItem(R.id.printForm).setVisible(false);
		}
		if(Constants.ACTION_UPDATE.equals(action)){
			if(form == null) return true;
			Formulario formulario = (Formulario) form.getEntity();
			switch (formulario.getEstadoTipoFormulario().getId()) {
			case EstadoTipoFormulario.ID_EN_PREPARACION:
				menu.findItem(R.id.printForm).setVisible(false);
				break;
			case EstadoTipoFormulario.ID_CERRADA_PROVISORIA:
				menu.findItem(R.id.printForm).setVisible(false);
				break;
			case EstadoTipoFormulario.ID_CERRADA_DEFINITIVA:
				menu.findItem(R.id.printForm).setVisible(true);
				menu.findItem(R.id.anularForm).setVisible(false);
				menu.findItem(R.id.closeForm).setVisible(false);
				break;
			case EstadoTipoFormulario.ID_ANULADA:
				menu.findItem(R.id.printForm).setEnabled(false);
				menu.findItem(R.id.anularForm).setVisible(false);
				menu.findItem(R.id.closeForm).setVisible(false);
				break;
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// verify access
		if (!appState.canAccess()) {
			GUIHelper.showErrorLoseSession(this);
			return true;
		}

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.anularForm:
			dialogVerifyPIN(ANULAR_FORM).show();
			return true;
		case R.id.closeForm:
			if(form.validate()){
				dialogVerifyPIN(CERRAR_FORM).show();
			}else{
				GUIHelper.showMessage(this, getString(R.string.form_validate_error));
			}
			return true;
		case R.id.printForm:
			doPrintForm();
			return true;
		case R.id.sendAlert:
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle(R.string.confirm_title);
			alertDialog.setMessage(R.string.alert_confirm_msg);
			alertDialog.setCancelable(false);
			alertDialog.setIcon(R.drawable.ic_error_default);
			alertDialog.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							new AIDigitalManager(RegisterActivity.this).sendAlertaPanico();
							GUIHelper.showMessage(RegisterActivity.this, "¡Alerta disparada!");
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
		case R.id.minimizeAllForm:
			doMinimizeAll();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SQLHelper sql = new SQLHelper(this);
		sql.openReadableDatabase();
		sql.closeDatabase();
	}

	/**
	 * 
	 * Construye un dialog para el seteo de PIN
	 * 
	 * @param form
	 */
	public Dialog dialogVerifyPIN(final int action){
		final Dialog dialog  = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_verify_pin);
		dialog.setCancelable(true);
		
		if(ANULAR_FORM == action){
			LinearLayout container = (LinearLayout) dialog.findViewById(R.id.main_layout);
			container.setBackgroundColor(Color.RED);
		}
		
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
					pinEditText.setError(getString(R.string.set_pin_pin_label) +" debe ser de 4 digitos");
					return;
				}
				// Encodeo el PIN ingresado
				//String encodedPIN = AuthManager.encodePIN(inputpin);
				// Recupero el PIN almacenado localmente
				SharedPreferences sharedPreferences = 
						getSharedPreferences(appState.getClass().getName(), Context.MODE_PRIVATE);
				String storedPIN = sharedPreferences.getString(username, "");
				// Comparo 
				if(inputpin.equals(storedPIN)){
					new SaveFormTask(action).execute();
					dialog.dismiss();
				}else{
					pinEditText.setError(getString(R.string.set_pin_pin_label) +" incorrecto");
				}
			}
		});
		ButtonEnabler.register(buttonConfirm, pinEditText);

		Button buttonCreatePin = (Button) dialog.findViewById(R.id.create_pin);
		buttonCreatePin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				new AuthManager(RegisterActivity.this).dialogChangePIN().show();
			}
		});

		return dialog;
	}

	private void doPrintForm(){
		// Filters to manage response from DAAService
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Constants.ACTION_PRINT_FORM_ERROR);
//		intentFilter.addAction(Constants.ACTION_PRINT_FORM_SUCCESS);
//
//		registerReceiver(mReceiver, intentFilter);
		
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(intent, BT_STATUS);
			return;
		} 

		new PrintFormTask().execute();
	}
	
	
	private void doMinimizeAll() {
		form.colapseAll();
	}
	

	/**
	 *
	 */
	private class RenderFormularioTask extends AsyncTask<String, String, Boolean> {
		private ProgressDialog dialog;
		private AIDigitalManager manager;
		private RegisterActivity context;
		private Formulario formulario;
		private String message;

		@Override
		protected void onPreExecute() {
			this.context = RegisterActivity.this;
			this.dialog  = ProgressDialog.show(context, "", getString(R.string.loading_msg), true);
			this.manager = new AIDigitalManager(context);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String action = params[0];
			Integer entityID = Integer.valueOf(params[1]);
			if (!appState.canAccess()){
				message = "Perdió la sesión";
				return false;
			} 
			try {
				Thread.sleep(1000);
				if(action.equals(Constants.ACTION_CREATE)){
					Integer formTypeID = 1;//Integer.valueOf(params[2]);
					// Create
					formulario = manager.getFormForCreate(entityID, formTypeID);
				}else{
					// Update
					formulario = manager.getFormForUpdate(entityID);
				}
			} catch (ReplyException e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				message = e.getMessage();
				return false;
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				message = String.format("Error %s. Verifique los logs", e.getMessage());
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();

			if(result == false){
				GUIHelper.showAlert(RegisterActivity.this, 
						"Error al cargar formulario", message);
				return;
			}

			form = manager.buildForm(formulario);
			// ScrollView with disable focus when move
			ScrollView scrollView = new ScrollView(context) {
				@Override
				protected boolean onRequestFocusInDescendants(int direction,
						Rect previouslyFocusedRect) {return true;}
			};

			LinearLayout appBody = new LinearLayout(context);

			appBody.setOrientation(LinearLayout.VERTICAL);	
			appBody.addView(form.getView());
			scrollView.addView(appBody);

			setContentView(scrollView);
			//--
			// Nro Acta
			String numero = formulario.getNumero();
			Integer inspector = formulario.getNumeroInspector();
			Integer reparticion = formulario.getReparticion();
			context.setTitle(getString(R.string.app_header_register_title, numero, inspector, reparticion));
			//--
		}
	}


	/**
	 * Save Form
	 * 
	 */
	private class SaveFormTask extends AsyncTask<Void, String, Boolean> {
		private ProgressDialog dialog;
		private AIDigitalManager manager;
		private int action;

		public SaveFormTask(int action) {
			this.action = action;
		}

		@Override
		protected void onPreExecute() {
			this.dialog  = ProgressDialog.show(RegisterActivity.this, "", getString(R.string.loading_msg), true);
			this.manager = new AIDigitalManager(RegisterActivity.this);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (!appState.canAccess()) throw new Exception("Session closed!");
				manager.saveForm(form);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();

			if(result){
				form.disable();
				Intent intent = null;
				switch (action) {
				case CERRAR_FORM:
					intent = new Intent(RegisterActivity.this, SaveFormActivity.class);
					break;
				case ANULAR_FORM:
					intent = new Intent(RegisterActivity.this, AnularFormActivity.class);
					break;
				}
				startActivityForResult(intent, action);
			}else{
				GUIHelper.showMessage(RegisterActivity.this, "Error al guardar Formulario");	
			}
		}
	}


	/**
	 * Cerrar Definitivamente Formulario
	 *
	 */
	private class CloseFormTask extends AsyncTask<Integer, CharSequence, Boolean> {
		private ProgressDialog dialog;
		private AIDigitalManager service;

		@Override
		protected void onPreExecute() {
			Context context = RegisterActivity.this;
			this.dialog = ProgressDialog.show(context, "", context.getString(R.string.saving_msg), true);
			this.service = new AIDigitalManager(context);
		}
		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				Thread.sleep(1000);
				service.closeForm(form, params[0]);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				return false;
			}
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result){
				Toast.makeText(RegisterActivity.this, 
						"Formulario se guardó correctamente", Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			}
		}
	}


	/**
	 * Anular Definitivamente Formulario
	 *
	 */
	private class AnularFormTask extends AsyncTask<Integer, CharSequence, Boolean> {
		private ProgressDialog dialog;
		private AIDigitalManager service;

		@Override
		protected void onPreExecute() {
			this.service = new AIDigitalManager(RegisterActivity.this);
			this.dialog = ProgressDialog.show(RegisterActivity.this, "", 
					RegisterActivity.this.getString(R.string.saving_msg), true);
		}
		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				Thread.sleep(1000);
				service.anularForm(form, params[0]);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				return false;
			}
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result){
				Toast.makeText(RegisterActivity.this, 
						"Formulario se anuló correctamente", Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			}
		}
	}

	/**
	 * Imprimir Formulario
	 *
	 */
	private class PrintFormTask extends AsyncTask<Void, Void, Void> {
		private AIDigitalManager manager;
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			this.dialog = ProgressDialog.show(RegisterActivity.this,"",
					getString(R.string.printing_msg), true);
			this.manager = new AIDigitalManager(RegisterActivity.this);
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
			this.manager.generateReport(form);
			dialog.dismiss();
			DeviceContext.disableBluetooth();
		}
	}

//	private BroadcastReceiver mReceiver = new BroadcastReceiver() {   
//		@Override
//		public void onReceive(Context context, Intent intent) {         
//			String action = intent.getAction();
//			Log.i(LOG_TAG, "onReceive: " + action);
//
//			// Print Form
//			if (action.equals(Constants.ACTION_PRINT_FORM)) {
//				new PrintFormTask().execute();
//				return;
//			}
//		}
//	};
	
	
	public static class SaveFormActivity extends Activity {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			requestWindowFeature(Window.FEATURE_LEFT_ICON);
			setContentView(R.layout.dialog_save_form);
			setTitle(getString(R.string.save_form_title));
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_menu_save);
			// MotivoCierreTipoFormulario
			Spinner spinnerMotivoCierreTipoFormulario =
					(Spinner) findViewById(R.id.spinnerMotivoCierreTipoFormulario);
			//
			List<MotivoCierreTipoFormulario> listMotivoCierreTipoFormulario = new ArrayList<MotivoCierreTipoFormulario>();
			
			// None option
			MotivoCierreTipoFormulario motivoCierreTipoFormulario = new MotivoCierreTipoFormulario();
			motivoCierreTipoFormulario.setId(-1);
			motivoCierreTipoFormulario.setDescripcion("Seleccionar");
			listMotivoCierreTipoFormulario.add(motivoCierreTipoFormulario);
			// 
			listMotivoCierreTipoFormulario.addAll(
					DAOFactory.getInstance().getMotivoCierreTipoFormularioDAO().findAllActive());
			ArrayAdapter<MotivoCierreTipoFormulario> adapterMotivoCierreTipoFormulario;
			adapterMotivoCierreTipoFormulario = new ArrayAdapter<MotivoCierreTipoFormulario>(this , 
					android.R.layout.simple_spinner_item, listMotivoCierreTipoFormulario) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					TextView holder;
					if (convertView == null) {
						convertView = View.inflate(SaveFormActivity.this, android.R.layout.simple_spinner_item, null);
						holder = (TextView) convertView.findViewById(android.R.id.text1);
						convertView.setTag(holder);
					} else {
						holder = (TextView) convertView.getTag();
					}
					holder.setText(this.getItem(position).getDescripcion());
					return convertView;
				}
				@Override
				public View getDropDownView(int position, View convertView,	ViewGroup parent) {
					TextView holder;
					if (convertView == null) {
						convertView = View.inflate(SaveFormActivity.this, android.R.layout.simple_spinner_dropdown_item, null);
						holder = (TextView) convertView.findViewById(android.R.id.text1);
						convertView.setTag(holder);
					} else {
						holder = (TextView) convertView.getTag();
					}
					holder.setText(this.getItem(position).getDescripcion());
					return convertView;
				}
			};
			adapterMotivoCierreTipoFormulario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerMotivoCierreTipoFormulario.setAdapter(adapterMotivoCierreTipoFormulario);
			spinnerMotivoCierreTipoFormulario.requestFocus();
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.i(LOG_TAG, "onActivityResult: enter");
			// 
			if(resultCode == RESULT_OK){
				switch (requestCode) {
				case BT_STATUS:
					DoPrint(null);
					break;
				}
			}
			// Return key is pressed
			if(resultCode == RESULT_CANCELED){

			}
			Log.i(LOG_TAG, "onActivityResult: exit");
		}

		/**
		 * 
		 * @param v
		 */
		public void DoSave(View v){
			Spinner spinnerMotivoCierreTipoFormulario =
					(Spinner) findViewById(R.id.spinnerMotivoCierreTipoFormulario);
			TextView title = (TextView) findViewById(R.id.textViewFormTitle);

			MotivoCierreTipoFormulario motivoCierreTipoFormulario = 
					(MotivoCierreTipoFormulario) spinnerMotivoCierreTipoFormulario.getSelectedItem();
			
			if(motivoCierreTipoFormulario.getId() < 1){
				title.setError("");
				spinnerMotivoCierreTipoFormulario.setFocusableInTouchMode(true);
				spinnerMotivoCierreTipoFormulario.requestFocus();
				spinnerMotivoCierreTipoFormulario.setFocusableInTouchMode(false);
				//
				GUIHelper.showMessage(this, getString(R.string.form_validate_error));
				return;
			}

			Intent intent = new Intent();
			intent.putExtra(Constants.ACTION, RegisterActivity.CERRAR_FORM);
			intent.putExtra(Constants.COD_MOTIVO_CIERRE, motivoCierreTipoFormulario.getId());

			setResult(Activity.RESULT_OK, intent);
			
			
			super.finish();
		}
		
		@Override
		protected void onDestroy() {
			DeviceContext.disableBluetooth();
			super.onDestroy();
		}

		/**
		 * 
		 * @param v
		 */
		public void DoPrint(View v){
			if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(intent, BT_STATUS);
				return;
			} 
			
			new PrintFormTask().execute();
		}
		
		/**
		 * Imprimir Formulario
		 *
		 */
		private class PrintFormTask extends AsyncTask<Void, Void, Void> {
			private AIDigitalManager manager;
			private ProgressDialog dialog;
			
			@Override
			protected void onPreExecute() {
				this.dialog = ProgressDialog.show(SaveFormActivity.this,"",
						getString(R.string.printing_msg), true);
				this.manager = new AIDigitalManager(SaveFormActivity.this);
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
				this.manager.generateReport(form);
				this.dialog.dismiss();
				DeviceContext.disableBluetooth();
			}
		}
	}
}
