package coop.tecso.daa.activities.task;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import coop.tecso.daa.R;
import coop.tecso.daa.activities.NotificacionesActivity;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.common.GUIHelper;
import coop.tecso.daa.common.WebServiceController;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
import coop.tecso.daa.domain.aplicacion.AplicacionTipoBinario;
import coop.tecso.daa.domain.aplicacion.AreaAplicacionPerfil;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.base.TablaVersion;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;
import coop.tecso.daa.domain.perfil.Campo;
import coop.tecso.daa.domain.perfil.CampoValor;
import coop.tecso.daa.domain.perfil.CampoValorOpcion;
import coop.tecso.daa.domain.perfil.PerfilAcceso;
import coop.tecso.daa.domain.perfil.PerfilAccesoAplicacion;
import coop.tecso.daa.domain.perfil.PerfilAccesoUsuario;
import coop.tecso.daa.domain.perfil.Seccion;
import coop.tecso.daa.domain.seguridad.Impresora;
import coop.tecso.daa.domain.seguridad.UsuarioApmDM;
import coop.tecso.daa.domain.seguridad.UsuarioApmImpresora;
import coop.tecso.daa.domain.util.DeviceContext;
import coop.tecso.daa.utils.Constants;

/**
 * 
 * @author tecso.coop
 *
 */
public class InitAppAsyncTask extends AsyncTask<Void, CharSequence, Boolean> {

	private static final String LOG_TAG = InitAppAsyncTask.class.getSimpleName();

	private ProgressDialog progressDialog;
	private Context context;
	private DAOFactory daoFactory;
	private DAAApplication appState;
	private WebServiceController webService;

	private Map<String, Integer> remoteStatusMap;
	private Map<String, Integer> localStatusMap;
	private String message;

	/**
	 * 
	 * @param context
	 */
	public InitAppAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		daoFactory = DAOFactory.getInstance();
		appState = (DAAApplication) context.getApplicationContext();
		// Show status message
		progressDialog = GUIHelper.getIndetProgressDialog(context, context.getString(R.string.login_init), context.getString(R.string.please_wait));
		if(this.progressDialog != null) {
			this.progressDialog.setCancelable(false);
			this.progressDialog.setCanceledOnTouchOutside(false);
		}
		webService = WebServiceController.getInstance(context);

		remoteStatusMap = new HashMap<String, Integer>();
		localStatusMap = new HashMap<String, Integer>();
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {	    	

		if (!DeviceContext.hasConnectivity(context)) {
			Log.e(LOG_TAG, "**SE OMITE SYNC POR FALTA DE CONECTIVIDAD**");
			return true;
		}
		
		try {
			// Se sincroniza y carga versiones de tablas
			// Build remote status 
			List<TablaVersion> tablaVersionList = webService.syncTablaVersionList(Constants.COD_DAA);
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

			// Sincroniza Aplicaciones y Binarios 
			publishProgress(context.getString(R.string.synchronizing_item_msg, "aplicaciones"));
			synchronize(Aplicacion.class);	    			    		
			synchronize(AplicacionTipoBinario.class); 
			
			// Sincroniza Perfil de acceso
			publishProgress(context.getString(R.string.synchronizing_item_msg, "perfiles de acceso"));
			synchronize(PerfilAcceso.class); 
			synchronize(PerfilAccesoUsuario.class);
			synchronize(PerfilAccesoAplicacion.class);
			synchronize(AreaAplicacionPerfil.class);

			// Sincroniza Parametros
			publishProgress(context.getString(R.string.synchronizing_item_msg, "parámetros de aplicación"));
			synchronize(AplicacionParametro.class); 

			// Sincroniza Campos y Seccion:
			publishProgress(context.getString(R.string.synchronizing_item_msg, "campos"));
			synchronize(Campo.class);
			synchronize(CampoValor.class);
			synchronize(CampoValorOpcion.class);

			publishProgress(context.getString(R.string.synchronizing_item_msg, "secciones"));
			synchronize(Seccion.class);
			synchronize(AplicacionPerfil.class);

			// Sincroniza Perfiles por aplicacion 		
			//TODO: synchronizeByAplicacion
			publishProgress(context.getString(R.string.synchronizing_item_msg, "formularios"));
			synchronize(AplPerfilSeccionCampo.class);
			synchronize(AplPerfilSeccionCampoValor.class);
			synchronize(AplPerfilSeccionCampoValorOpcion.class);
			synchronize(AplicacionPerfilSeccion.class);
			
			// Sincroniza por Usuario
			publishProgress(context.getString(R.string.synchronizing_item_msg, "dispositivos móviles"));
			synchronizeByUsuario(UsuarioApmImpresora.class);
			synchronizeByUsuario(UsuarioApmDM.class);
			synchronizeByUsuario(Impresora.class);
			
			publishProgress(context.getString(R.string.synchronizing_item_msg, "binarios"));
			synchronize(AplicacionBinarioVersion.class); 
			synchronize(AreaAplicacionPerfil.class); 
			

//			List<Aplicacion> listAplicacion = daoFactory.getAplicacionDAO().findAllByUsuarioId(appState.getCurrentUser().getId());
//			for (Aplicacion aplicacion : listAplicacion) {
//				int aplicacionID =  aplicacion.getId();
//				publishProgress(currentActivity.getString(R.string.synchronizing_item_msg,"perfiles "+ aplicacion.getDescripcion()));
//				localService.synchronizeByAplicacionID(AplPerfilSeccionCampo.class, "apm_aplPerfilSeccionCampo", aplicacionID);
//				localService.synchronizeByAplicacionID(AplPerfilSeccionCampoValor.class, "apm_aplPerfilSeccionCampoValor", aplicacionID);
//				localService.synchronizeByAplicacionID(AplPerfilSeccionCampoValorOpcion.class, "apm_aplPerfilSeccionCampoValorOpcion", aplicacionID);
//				localService.synchronizeByAplicacionID(AplicacionPerfilSeccion.class, "apm_aplicacionPerfilSeccion", aplicacionID);
//			}
//
			//TODO: Producto 2
//			// Delete All previous Notifications
//			Integer deviceID = appState.getDispositivoMovil().getId();
//			udaaDAO.getNotificacionDAO().deleteAll();
//			// Sincronizando Notificaciones 
//			TablaVersion tv = udaaDAO.getTablaVersionDAO().findByTableName("not_notificacion");
//			if(tv.getLastVersion() == 0){
//				tv.setLastVersion(map.get("not_notificacion"));
//				udaaDAO.getTablaVersionDAO().update(tv);
//			}
//			publishProgress("Sincronizando notificaciones…");
//			localService.synchronize(EstadoNotificacion.class, "not_estadoNotificacion");
//			localService.synchronize(TipoNotificacion.class, "not_tipoNotificacion");
//			localService.synchronize(Notificacion.class, "not_notificacion", deviceID, false);

			return true;
		} catch(ReplyException error) {
			Log.e(LOG_TAG, "Error iniciando sesion: "+error.getMessage());
			message = error.getCode()+" - "+error.getMessage();
			return false;
		}catch(Exception e) {
			Log.e(LOG_TAG, "Error iniciando sesion", e);
			message = "Error inesperado :" + e.getMessage(); 
			return false;
		}finally{
			OpenHelperManager.releaseHelper();
		}     
	}   
	


	@Override
	protected void onProgressUpdate(CharSequence... values) {
		progressDialog.setMessage(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {

		progressDialog.dismiss();
		if (result == false) {
			GUIHelper.showError(context, message);
		}

		//TODO:
		// Se inicia el canal de contingencias
//		udaaApplication.startEmergencyChannel();
		// Se inicia el timer GPS
//		udaaApplication.startGPSLocationTimer();

		//
		((Activity)context).setTitle(appState.getFormattedTitle());

		Intent intent = new Intent(context, NotificacionesActivity.class);
		context.startActivity(intent);
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
		
		if(localVersion < remoteVersion || forceUpdate){
			if (clazz.equals(AplicacionBinarioVersion.class)) {
				webService.syncEntityDeltaBinary(appState.getCurrentUser().getId(), forceUpdate);
			}else{
				webService.syncEntityDelta(clazz, forceUpdate);
			}
		}
	}
	
	/**
	 * 
	 * @param clazz 
	 * @throws Exception
	 */
	private <T extends AbstractEntity> void synchronizeByUsuario(Class<T> clazz) throws Exception{
		Log.i(LOG_TAG, String.format("Sincronizando %s ...", clazz.getSimpleName()));
		
		String username = appState.getCurrentUser().getUsername();
		String localKey = String.format("%s|%s", clazz.getSimpleName(), username);
		String remoteKey = clazz.getSimpleName().toLowerCase(Locale.getDefault());
		
		boolean forceUpdate = appState.getDispositivoMovil().getForzarActualizacion() == 1;
		
		Integer localVersion  = localStatusMap.get(localKey);
		Integer remoteVersion = remoteStatusMap.get(remoteKey);
		
		if(null == localVersion || localVersion < remoteVersion || forceUpdate){
			webService.syncEntityDeltaByUsuario(clazz, username, forceUpdate);
		}
	}
}