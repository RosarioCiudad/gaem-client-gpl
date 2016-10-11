package coop.tecso.daa.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpStatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import coop.tecso.daa.R;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.dao.AplicacionBinarioVersionDAO;
import coop.tecso.daa.dao.AplicacionDAO;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.TablaVersionDAO;
import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.base.Jsoner;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.base.TablaVersion;
import coop.tecso.daa.domain.gps.HistorialUbicacion;
import coop.tecso.daa.domain.notificacion.Notificacion;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;
import coop.tecso.daa.domain.util.GsonJsoner;
import coop.tecso.daa.domain.web.Reply;
import coop.tecso.daa.domain.web.WebService;
import coop.tecso.daa.persistence.DatabaseHelper;
import coop.tecso.daa.utils.ErrorConstants;

public class WebServiceController  {


	private static WebServiceController INSTANCE;

	public static synchronized WebServiceController getInstance(Context context) {
		if(null == INSTANCE){
			INSTANCE = new WebServiceController(context);
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param deviceID
	 * @return
	 * @throws Exception
	 */
	public UsuarioApm login(String username, String password, String deviceID) throws ReplyException{
		WebService ws = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", username);
			params.put("password", password); 
			params.put("deviceid", deviceID); 

			ws = new WebService(WS_URL);		

			Reply<String> reply = ws.webPost("loginManager.json", params);
			
			//Original:
			//Reply<String> reply = ws.webGet("login.json", params);

			Log.d(LOG_TAG, "Reply:" +reply);
			if(reply.code != HttpStatus.SC_OK){
				throw new ReplyException(reply.code, reply.message);
			}
			return gson.fromJson(reply.result, UsuarioApm.class);
		}catch (ReplyException e) {
			throw e;
		}catch (Exception e) {
			Log.d(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, "Error Inesperado");
		} finally {
			if (ws != null) ws.abort();
		}
	}

	/**
	 * 
	 * @return
	 * @throws ReplyException
	 */
	public List<TablaVersion> syncTablaVersionList(String codigoApp) throws ReplyException {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("app", codigoApp);
			// Invoke WS
			String response = secureWebGetInvoke("tablaversion/list.json", params);

			Type type = new TypeToken<List<TablaVersion>>(){}.getType();
			// Parse and return response
			return gson.fromJson(response, type);
		}catch (ReplyException e) {
			throw e;
		}catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(403 , "Error Inesperado");
		}
	}

	/**
	 * 
	 * @param deviceID
	 * @return
	 * @throws ReplyException
	 */
	public DispositivoMovil syncDispositivoMovil(String deviceID) throws ReplyException {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("deviceID", deviceID);
			// Invoke WS
			String response = secureWebGetInvoke("dispositivomovil.json", params);
			// Parse and return response
			return gson.fromJson(response, DispositivoMovil.class);
		}catch (ReplyException e) {
			throw e;
		}catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(403 , "Error Inesperado");
		}
	}

	/**
	 * 
	 * @param clazz
	 * @param tableName
	 * @param forceUpdate
	 * @throws Exception
	 */
	public <T extends AbstractEntity> void syncEntityDelta(Class<T> clazz, boolean forceUpdate) throws ReplyException {
		Log.d(LOG_TAG, "syncEntityDelta: enter");
		// Retrieve TablaVersion by tableName
		TablaVersionDAO tablaVersionDAO = daoFactory.getTablaVersionDAO();
		TablaVersion tablaVersion = tablaVersionDAO.findByTableName(clazz.getSimpleName());
		// Checking update version
		Integer version = forceUpdate? 0 : tablaVersion.getLastVersion();

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("entidad", clazz.getSimpleName());
		params.put("version", version.toString());

		Log.d(LOG_TAG, String.format("entity: %s version: %s force: %s",
				clazz.getSimpleName(), version, forceUpdate));
		// Invoke WS
		String response = secureWebGetInvoke("delta/list.json", params);

		// Deserialize json response
		Gson gson = new GsonBuilder()
		.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {					
				return new Date(json.getAsJsonPrimitive().getAsLong()); 
			}
		}).create();

		List<T> entityDelta = new ArrayList<T>();
		JsonElement json = new JsonParser().parse(response);
		for (JsonElement je: json.getAsJsonArray()) {
			entityDelta.add(gson.fromJson(je, clazz));
		}	

		// Update Entities 
		RuntimeExceptionDao<T, Integer> entityDAO = database.getRuntimeExceptionDao(clazz);
		for (T entity: entityDelta) {            	
			Log.d(LOG_TAG, "Procesing: " + entity.getClass().getSimpleName());
			if (entity.getEstado() == 1) {
				Log.d(LOG_TAG, " Updating entity ...");
				entityDAO.createOrUpdate(entity);
			} else {
				Log.d(LOG_TAG, " Deleting entity ...");
				entityDAO.delete(entity);
			}
			version = entity.getVersion();
		}

		Log.d(LOG_TAG, "Saving version " + version);
		tablaVersion.setLastVersion(version);
		tablaVersionDAO.update(tablaVersion);
		Log.d(LOG_TAG, "syncEntityDelta: exit");
	}

	/**
	 * 
	 * @param clazz
	 * @param tableName
	 * @param forceUpdate
	 * @throws Exception
	 */
	public <T extends AbstractEntity> void syncEntityDeltaByUsuario(
			Class<T> clazz, String username, boolean forceUpdate) throws ReplyException {
		Log.d(LOG_TAG, "syncEntityDeltaByUsuario: enter");
		TablaVersionDAO tablaVersionDAO = daoFactory.getTablaVersionDAO();
		String tableKey = String.format("%s|%s", clazz.getSimpleName(), username);
		// Retrieve TablaVersion by tableKey
		TablaVersion tablaVersion = tablaVersionDAO.findByTableName(tableKey);
		if(tablaVersion == null){
			tablaVersion = new TablaVersion();
			tablaVersion.setTabla(tableKey);
			tablaVersion.setUsuario(username);
			tablaVersion.setLastVersion(0);
		}
		
		// Checking update version
		Integer version = forceUpdate? 0 : tablaVersion.getLastVersion();

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("entidad", clazz.getSimpleName());
		params.put("version", version.toString());

		Log.d(LOG_TAG, String.format("entity: %s version: %s force: %s",
				clazz.getSimpleName(), version, forceUpdate));
		// Invoke WS
		String response = secureWebGetInvoke("delta/listByUsuario.json", params);

		// Deserialize json response
		Gson gson = new GsonBuilder()
		.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)	throws JsonParseException {					
				return new Date(json.getAsJsonPrimitive().getAsLong()); 
			}
		}).create();

		List<T> entityDelta = new ArrayList<T>();
		JsonElement json = new JsonParser().parse(response);
		for (JsonElement je: json.getAsJsonArray()) {
			entityDelta.add(gson.fromJson(je, clazz));
		}	

		// Update Entities 
		RuntimeExceptionDao<T, Integer> entityDAO = database.getRuntimeExceptionDao(clazz);
		for (T entity: entityDelta) {            	
			Log.d(LOG_TAG, "Procesing: " + entity.getClass().getSimpleName());
			if (entity.getEstado() == 1) {
				Log.d(LOG_TAG, " Updating entity ...");
				entityDAO.createOrUpdate(entity);
			} else {
				Log.d(LOG_TAG, " Deleting entity ...");
				entityDAO.delete(entity);
			}
			version = entity.getVersion();
		}

		Log.d(LOG_TAG, "Saving version " + version);
		tablaVersion.setLastVersion(version);
		tablaVersionDAO.createOrUpdate(tablaVersion);
		Log.d(LOG_TAG, "syncEntityDeltaByUsuario: exit");
	}
	
	/**
	 * 
	 * @param tableName
	 * @param usuarioID
	 * @param forceUpdate
	 * @throws Exception
	 */
	public void syncEntityDeltaBinary(Integer usuarioID, boolean forceUpdate) throws ReplyException {
		Log.d(LOG_TAG, "syncEntityDeltaBinary: enter");
		String entityName = AplicacionBinarioVersion.class.getSimpleName();

		// Retrieve TablaVersion by tableName
		TablaVersionDAO tablaVersionDAO = daoFactory.getTablaVersionDAO();
		TablaVersion tablaVersion = tablaVersionDAO.findByTableName(entityName);
		// Checking update version
		Integer version = forceUpdate? 0 : tablaVersion.getLastVersion();

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("entidad", entityName);
		params.put("version", version.toString());

		Log.d(LOG_TAG, String.format("entity: %s version: %s force: %s", entityName, version, forceUpdate));
		// Invoke WS
		String response = secureWebGetInvoke("delta/list.json", params);

		// Deserialize json response
		Type type = new TypeToken<List<AplicacionBinarioVersion>>(){}.getType();
		List<AplicacionBinarioVersion> entityDelta = gson.fromJson(response, type);

		Map<String, AplicacionBinarioVersion> mToInstall = 
				new HashMap<String, AplicacionBinarioVersion>();

		AplicacionBinarioVersionDAO entityDAO = daoFactory.getAplicacionBinarioVersionDAO();
		AplicacionDAO aplicacionDAO = daoFactory.getAplicacionDAO();
		for (AplicacionBinarioVersion entity: entityDelta) {      
			// Aplicacion
			Aplicacion aplicacion = aplicacionDAO.findById(entity.getAplicacion().getId());
			if(!aplicacionDAO.hasAccess(usuarioID, aplicacion.getCodigo())){
				continue;
			}

			Log.d(LOG_TAG, "Procesing: " + entity.getClass().getSimpleName());
			if (entity.getEstado() == 1) {
				Log.d(LOG_TAG, " Updating entity ...");
				entityDAO.createOrUpdate(entity);
				// Analizo que binario descargar
				int tipoBinario = entity.getAplicacionTipoBinario().getId();
				String key = aplicacion.getCodigo()+"#"+tipoBinario;
				// TipoBinario = Template o TipoBinario = DATA [multiples por aplicacion]
				if(tipoBinario == 2	|| tipoBinario == 3) continue;
				// 
				AplicacionBinarioVersion binToInstall = mToInstall.get(key);
				if(null == binToInstall || binToInstall.getVersion() < entity.getVersion()){
					mToInstall.put(key, entity);
				}
			} else {
				Log.d(LOG_TAG, " Deleting entity ...");
				entityDAO.delete(entity);
			}
			version = entity.getVersion();
		}

		//
		for (String key : mToInstall.keySet()) {
			Install(mToInstall.get(key));
		}

		Log.d(LOG_TAG, "Guardando version " + version);
		tablaVersion.setLastVersion(version);
		tablaVersionDAO.update(tablaVersion);
	}
	
	/**
	 * Syncronize local formulario to server.
	 * 
	 * @param afiliacion
	 * @return
	 * @throws ReplyException 
	 */
	public synchronized boolean syncUbicacion(
			HistorialUbicacion historialUbicacion) throws ReplyException {
		Log.i(LOG_TAG, "syncHistorialUbicacion: enter");

		String data = gson.toJson(historialUbicacion);

		// Load POST Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		try {
			// Invoke WS
			String response = secureWebPostInvoke("historialUbicacion.json", params);
			// Parse and return response
			return gson.fromJson(response, Boolean.class);
		}catch (ReplyException e) {
			throw e;
		}catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE , "Error Inesperado");
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Notificacion getNotificacion(long id) throws ReplyException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notificacionID", id);

		String jsonResponse = "";//secureWebServiceInvoke("Notificacion", params);

		Log.d(LOG_TAG, jsonResponse);

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
					throws JsonParseException {					
				return new Date(json.getAsJsonPrimitive().getAsLong()); 
			} 
		});

		// Parse Response into our object            
		Gson gson = builder.create();								

		Notificacion notificacion = gson.fromJson(jsonResponse, Notificacion.class);
		Log.d(LOG_TAG, notificacion.toString());

		return notificacion;
	}

	/**
	 * 
	 * Acuse de recibo de notificacion en el DM
	 * 
	 * @param id
	 * @return
	 */
	public String receiveNotificacion(long id) throws ReplyException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notificacionID", id);

		String response = "";//secureWebServiceInvoke("ReceiveNotification", params);

		if (response.contains("ERROR"))
			return "ERROR";

		return "OK";
	}
	
	
	/**
	 * Download and install a binary file from URL via Http/Https GET.
	 * Note: installation execute only .apk's files.
	 * 
	 * @param fileurl
	 * @throws ReplyException 
	 */
	private void Install(AplicacionBinarioVersion binary) throws ReplyException{
		try {
			Map<String, String> params = new HashMap<String, String>();
			DAAApplication appState =  (DAAApplication) ctx.getApplicationContext();
			params.put("username", appState.getCurrentUser().getUsername());
			params.put("usertoken", appState.getCurrentUser().getUsertoken());

			// Build URL
			StringBuffer fileurl = new StringBuffer(WS_URL+"/"+binary.getUbicacion());
			int i = 0;
			for (Map.Entry<String, String> param : params.entrySet()) {
				if(i++ == 0) {
					fileurl.append("?");
				} else {
					fileurl.append("&");
				}
				try {
					fileurl.append(param.getKey()+"="+URLEncoder.encode(param.getValue(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e(LOG_TAG, "**ERROR**", e);
				} 
			}

			//
			Log.d(LOG_TAG, "Descargando… " + fileurl.toString());

			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};
			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL url = new URL(fileurl.toString());

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.connect();

			String fileName = binary.getUbicacion().substring(binary.getUbicacion().lastIndexOf(File.separator) + 1);

			File file = null;
			switch (binary.getAplicacionTipoBinario().getId()) {
			case 1: // Core
				String path = Environment.getExternalStorageDirectory() + "/download/";
				new File(path).mkdirs();
				file = new File(path, fileName);
				break;
			case 2: // Template: download file into internal template storage
				file = new File(ctx.getDir("tpl", Context.MODE_PRIVATE), fileName);
				break;
			case 3: // DataBase: download data..
				file = new File(ctx.getDir("tmp", Context.MODE_PRIVATE), fileName);
				break;
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			InputStream inputStream = httpURLConnection.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, len1);
			}
			fileOutputStream.close();
			inputStream.close();

			Log.d(LOG_TAG, "Descargando OK");

			AplicacionDAO aplicacionDAO = DAOFactory.getInstance().getAplicacionDAO();

			if(binary.getAplicacionTipoBinario().getId() == 1){
				// Application
				Aplicacion apl = aplicacionDAO.findById(binary.getAplicacion().getId());
				// Intent to install apk
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

				PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
				// Create Notification
				Notification notification = new Notification(
						android.R.drawable.ic_menu_info_details, //Icon to use
						ctx.getString(R.string.update_available), //Message
						System.currentTimeMillis()); //When to display
				//Add to the Notification
				notification.setLatestEventInfo(
						ctx.getApplicationContext(),
						ctx.getString(R.string.update_application, apl.getDescripcion()), //Title of detail view
						ctx.getString(R.string.update_message),  //Text on detail view
						pIntent);
				notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
				//Display the Notification
				NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				nm.notify(binary.getId(), notification); 
			}
		} catch (Exception e) {	  
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE, e.getMessage());
		}
	} 


	/**
	 * Obtiene las notificaciones pendiente de envio que se encuentran en el servidor.
	 * Este metodo se utiliza en caso que el servicio C2DM no se encuentre disponible
	 * y se realice una sincronizacion por el canal de contingencias. 
	 * 
	 * @param deviceID
	 * @return
	 */
	@Deprecated
	public List<Notificacion> getNotificationsPending(Integer deviceID) throws Exception {
		// Load POST Parameters
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceID", deviceID);

		// Call the web service
		String jsonResponse = "";//secureWebServiceInvoke("GetPendingNotifications", params);			
		Log.d(LOG_TAG, jsonResponse);

		// Creates the JSON object which will manage the information received 
		GsonBuilder builder = new GsonBuilder(); 

		// Register an adapter to manage the date types as long values 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
					throws JsonParseException {					
				return new Date(json.getAsJsonPrimitive().getAsLong()); 
			} 
		});

		// Parse Response into our object            
		Gson gson = builder.create();								
		List<Notificacion> notificationsPending = new ArrayList<Notificacion>();
		JsonElement json = new JsonParser().parse(jsonResponse);
		for (JsonElement je: json.getAsJsonArray()) {            					
			notificationsPending.add(gson.fromJson(je, Notificacion.class));
		}

		return notificationsPending;
	}

	/**
	 * 
	 * @return
	 */
	@Deprecated
	public boolean confirmReceiptNotificacion(int notificacionID){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("notificacionID", notificacionID);

			String response = "";//secureWebServiceInvoke("SetEstadoEnviada", params);

			Log.d(LOG_TAG, response);
			if(response.contains("OK")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 
	 * @param method
	 * @param params
	 * @return
	 */
	private String secureWebGetInvoke(String method, Map<String, String> params) throws ReplyException {
		WebService ws = null;
		try {
			DAAApplication appState =  (DAAApplication) ctx.getApplicationContext();

			params.put("username", appState.getCurrentUser().getUsername());
			params.put("usertoken", appState.getCurrentUser().getUsertoken());
			ws = new WebService(WS_URL);

			Reply<String> reply = ws.webGet(method, params);

			Log.d(LOG_TAG, "Reply: " +reply);
			if(reply.code == HttpStatus.SC_OK){
				return reply.result;
			}else{
				throw new ReplyException(reply.code, reply.message);
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_REQUEST_TIMEOUT, "Error de Comunicación");
		} finally {
			if (ws != null) ws.abort();
		}
	}
	
	/**
	 * 
	 * @param method
	 * @param params
	 * @return
	 */
	private String secureWebPostInvoke(String method, Map<String, String> params) throws ReplyException {
		WebService ws = null;
		try {
			DAAApplication appState =  (DAAApplication) ctx.getApplicationContext();
			
			params.put("username", appState.getCurrentUser().getUsername());
			params.put("usertoken", appState.getCurrentUser().getUsertoken());
			ws = new WebService(WS_URL);

			Reply<String> reply = ws.webPost(method, params);

			Log.d(LOG_TAG, "Reply: " +reply);
			if(reply.code == HttpStatus.SC_OK){
				return reply.result;
			}else{
				throw new ReplyException(reply.code, reply.message);
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_REQUEST_TIMEOUT, "Error de Comunicación");
		} finally {
			if (ws != null) ws.abort();
		}
	}

	/**
	 * 
	 * @param method
	 * @param params
	 * @return
	 */
	@Deprecated
	public boolean sendLocationGPS(HistorialUbicacion uGPS) throws ReplyException {
		Log.d(LOG_TAG, "sendLocationGPS: enter");
		WebService ws = null;
		try {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ubicacionGPS", uGPS);

			String response = "";//secureWebServiceInvoke("SaveGPSLocation", params);

			Log.d(LOG_TAG, "Response: " + response);
			if (response.toUpperCase().contains("ERROR")) {
				throw new ReplyException(110, ErrorConstants.ERROR_110);
			}

			if (response.contains("OK")) {
				return true;
			} else {
				return false;
			}

		} catch (ReplyException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReplyException(110, ErrorConstants.ERROR_110);
		} finally {
			if (ws != null) ws.abort();
		}
	}



	// Implementation helpers
	private static final String LOG_TAG = WebServiceController.class.getSimpleName();

	private static String WS_URL;
	private static String URL;
	private DAOFactory daoFactory;
	private Context ctx;
	private DatabaseHelper database;
	private Jsoner gson;

	private WebServiceController(Context context) {
		database = (DatabaseHelper) OpenHelperManager.getHelper(context, DatabaseHelper.class);
		daoFactory  = DAOFactory.getInstance();

		SharedPreferences myPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		URL = myPrefs.getString("URL", context.getString(R.string.server_url));
		WS_URL = URL + "/api/";

	
		gson = new GsonJsoner();

		ctx = context;
	}

}
