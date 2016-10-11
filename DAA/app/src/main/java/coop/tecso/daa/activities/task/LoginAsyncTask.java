package coop.tecso.daa.activities.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import coop.tecso.daa.R;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.common.GUIHelper;
import coop.tecso.daa.common.WebServiceController;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.DispositivoMovilDAO;
import coop.tecso.daa.dao.UsuarioApmDAO;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;
import coop.tecso.daa.domain.util.DeviceContext;
import coop.tecso.daa.utils.Constants;

/**
 * 
 * @author tecso.coop
 *
 */
public class LoginAsyncTask extends AsyncTask<String, CharSequence, Boolean> {

	private static final String LOG_TAG = LoginAsyncTask.class.getSimpleName();

	private ProgressDialog progressDialog;
	private DAOFactory daoFactory;
	private WebServiceController webService;
	private DAAApplication appState;
	private String message;
	private Context context;

	/**
	 * 
	 * @param context
	 */
	public LoginAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		this.daoFactory = DAOFactory.getInstance();
		this.webService = WebServiceController.getInstance(context);
		this.appState = (DAAApplication) context.getApplicationContext();
		this.progressDialog = GUIHelper.getIndetProgressDialog(
				context, context.getString(R.string.login_init), context.getString(R.string.please_wait));
		if(this.progressDialog != null) {
			this.progressDialog.setCancelable(false);
			this.progressDialog.setCanceledOnTouchOutside(false);
		}
	}

	@Override
	protected Boolean doInBackground(String... param) {	    	
		// Params
		String username = param[0];
		String password = param[1];
		String deviceid = DeviceContext.getDeviceId(context);

		// Context
		DispositivoMovil dispositivoMovil = null;
		UsuarioApm usuario;

		// Login Local
		if(appState.isLocalLogin()){
			Log.d(LOG_TAG, "Comenzando Login Local...");

			UsuarioApmDAO usuarioApmDAO = daoFactory.getUsuarioApmDAO();
			usuario = usuarioApmDAO.findByUserName(username);

			if(usuario != null){
				// Valido usuario contra certificado local
				if(cerficateLogin(usuario.getUsercert(), password)){
					// Set current user
					appState.setCurrentUser(usuario);

					DispositivoMovilDAO dispositivoMovilDAO = daoFactory.getDispositivoMovilDAO();
					// Verify connectivity
					if(DeviceContext.hasConnectivity(context)){
						// Intent synchronize DispositivoMovil
						try {
							dispositivoMovil = webService.syncDispositivoMovil(deviceid);
							
							daoFactory.getAreaDAO().createOrUpdate(dispositivoMovil.getArea());
							dispositivoMovilDAO.createOrUpdate(dispositivoMovil);
						} catch (Exception e) {
							Log.d(LOG_TAG, "**ERROR**", e);
						}
					}
					// Fail DispositivoMovil synchronization
					if(dispositivoMovil == null){
						dispositivoMovil = dispositivoMovilDAO.findByIMEI(deviceid);
					}
				}else{
					// Login counter validation
					appState.addLocalLoginCount();
					int loginLocalMaxCount;
					try{ 
						AplicacionParametro aplParam;
						aplParam = daoFactory.getAplicacionParametroDAO().findByCodigo(Constants.COD_LOCAL_LOGIN_MAX_COUNT, Constants.COD_DAA);
						loginLocalMaxCount =  Integer.valueOf(aplParam.getValor()).intValue();
					}catch (Exception e) {
						Log.d(LOG_TAG, "Error en valor de parametro '"+Constants.COD_LOCAL_LOGIN_MAX_COUNT);
						loginLocalMaxCount = 3;
					}
					// Si la cantidad de intentos de login local supera el valor seteado se pasa al login remoto
					if(appState.getLocalLoginCount() >= loginLocalMaxCount){
						Log.d(LOG_TAG, "Cantidad de intentos supera el valor permitido. Se fuerza el login remoto.");
						// Si no existe el usuario en la db local se continua con un login remoto
						appState.setLocalLogin(false);
					}else{
						//
						message = "Usuario o Contraseña incorrecta";
						return false;
					}
				}
			}else{
				Log.d(LOG_TAG, "No existe Usuario en db local.. se fuerza el login remoto. ");
				appState.setLocalLogin(false);
			}
		}

		try {
			// Login Remoto
			if(!appState.isLocalLogin()){
				Log.d(LOG_TAG, "Remote Login...");
				// LoginUser via WS
				usuario = webService.login(username, password, deviceid);
				// Dates
//				long toleance = 1000 * 60 * 15; // 15 minutes
//				if(System.currentTimeMillis() +toleance < usuario.getFechaUltMdf().getTime() ){
//					message = "Verifique si la fecha del dispositivo es correcta..";
//					return false;
//				}
				
				// Open PK12 Certificate
				cerficateLogin(usuario.getUsercert(), password);
				// Create or Update UsuarioApm
				UsuarioApmDAO usuarioApmDAO = daoFactory.getUsuarioApmDAO();
				usuarioApmDAO.createOrUpdate(usuario);
				appState.setCurrentUser(usuario);
				// Synchronize DispositivoMovil via WS
				dispositivoMovil = webService.syncDispositivoMovil(deviceid);

				daoFactory.getAreaDAO().createOrUpdate(dispositivoMovil.getArea());
				// Create or Update DispositivoMovil
				DispositivoMovilDAO dispositivoMovilDAO = daoFactory.getDispositivoMovilDAO();
				dispositivoMovilDAO.createOrUpdate(dispositivoMovil);
			}

			// Device Context
			appState.setDispositivoMovil(dispositivoMovil);

			return true;
		}catch(ReplyException error) {
			Log.e(LOG_TAG, "Error iniciando sesion: "+error.getMessage());
			message = error.getCode()+" - "+error.getMessage();
			return false;
		}catch(Exception e) {
			Log.e(LOG_TAG, "Error iniciando sesion", e);
			message = "Error inesperado :" + e.getMessage(); 
			return false;
		}finally{
//			OpenHelperManager.releaseHelper();
		}	     
	}   

	@Override
	protected void onProgressUpdate(CharSequence... values) {
		this.progressDialog.setMessage(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		this.progressDialog.dismiss();
		if(result){
			// Send UnBlock Application Message
			Intent intent = new Intent();
			intent.setAction(Constants.ACTION_UNBLOCK_APPLICATION);
			this.appState.sendBroadcast(intent);

			new InitAppAsyncTask(context).execute();
		}else{
			GUIHelper.showError(context, message);
		}
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
			String alias = keyStore.aliases().nextElement();
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
			Log.i(LOG_TAG, "extracting the private key... OK");
			appState.setPrivateKey(privateKey);
			isValid = true;
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**: " + e.getMessage());
			appState.setPrivateKey(null);
			isValid = false;
		}
		Log.d(LOG_TAG, "cerficateLogin: exit");
		return isValid;
	}
}