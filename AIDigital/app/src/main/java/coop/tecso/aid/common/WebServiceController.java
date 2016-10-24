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

package coop.tecso.aid.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
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
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpStatus;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentValues;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.graph.GraphAdapterBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.dao.TablaVersionDAO;
import coop.tecso.aid.dao.UsuarioApmReparticionDAO;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.FormularioDetalle;
import coop.tecso.aid.entities.Panico;
import coop.tecso.aid.entities.Talonario;
import coop.tecso.aid.entities.UsuarioApmReparticion;
import coop.tecso.aid.helpers.SQLHelper;
import coop.tecso.aid.persistence.DatabaseHelper;
import coop.tecso.aid.utils.Constants;
import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.base.Jsoner;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.base.TablaVersion;
import coop.tecso.daa.domain.util.GsonJsoner;
import coop.tecso.daa.domain.web.Reply;
import coop.tecso.daa.domain.web.WebService;

public class WebServiceController  {

	private static WebServiceController INSTANCE;

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized WebServiceController getInstance(Context context) {
		if(null == INSTANCE){
			INSTANCE = new WebServiceController(context);
		}
		return INSTANCE;
	}

	
	/**
	 * 
	 * @param codigoApp
	 * @return
	 * @throws Exception
	 */
	public String estadoLicencia(String codTipDoc, String numero, String sexo, String clase)
			throws Exception {
		try {
			
			Map<String, String> params = new HashMap<String, String>();
			if(codTipDoc != null)
				params.put("tipoDocumento", codTipDoc);
			
			params.put("numeroDocumento", numero);
			if(!sexo.equals("S")){
				params.put("sexo", sexo);
			}
			if(clase != null && !clase.equals("Seleccionar")){
				params.put("claseLicencia", clase);
			}
			// Invoke WS
			String response = secureWebGetInvoke("apsv/estadoLicencia.json", params);

			return response;
		}catch (ReplyException e) {
			throw e;
		}catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE , "Error Inesperado");
		}
	}
	
	
	/**
	 * 
	 * @param codigoApp
	 * @return
	 * @throws Exception
	 */
	public List<TablaVersion> syncTablaVersionList(String codigoApp)
			throws Exception {
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
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE , "Error Inesperado");
		}
	}


	/**
	 * 
	 * @param codigoApp
	 * @return
	 * @throws Exception
	 */
	public List<Talonario> syncListTalonario(String tipoFormulario) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("deviceid", appState.getDispositivoMovil().getNumeroIMEI());
			params.put("tipoformularioid", tipoFormulario);
			// Invoke WS
			String response = secureWebGetInvoke("list/talonario.json", params);

			Type type = new TypeToken<List<Talonario>>(){}.getType();
			// Parse and return response
			return gson.fromJson(response, type);
		}catch (ReplyException e) {
			throw e;
		}catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE , "Error Inesperado");
		}
	}

	/**
	 * 
	 * @param clazz
	 * @param forceUpdate
	 * @throws Exception
	 */
	public <T extends AbstractEntity> void syncEntityDelta(Class<T> clazz, boolean forceUpdate) throws Exception {
		Log.d(LOG_TAG, "syncEntityDelta: enter");
		// Retrieve TablaVersion by tableName
		TablaVersionDAO tablaVersionDAO =  DAOFactory.getInstance().getTablaVersionDAO();
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
	 * @param forceUpdate
	 * @throws Exception
	 */
	public <T extends AbstractEntity> void syncEntityDeltaByArea(Class<T> clazz, boolean forceUpdate) throws Exception {
		Log.d(LOG_TAG, "syncEntityDeltaByArea: enter");
		// Retrieve TablaVersion by tableName
		TablaVersionDAO tablaVersionDAO =  DAOFactory.getInstance().getTablaVersionDAO();
		TablaVersion tablaVersion = tablaVersionDAO.findByTableName(clazz.getSimpleName());
		// Checking update version
		Integer version = forceUpdate? 0 : tablaVersion.getLastVersion();

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("entidad", clazz.getSimpleName());
		params.put("areaId", appState.getDispositivoMovil().getArea().getId()+"");
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
		Log.d(LOG_TAG, "syncEntityDeltaByArea: exit");
	}
	
	
	/**
	 * 
	 * @param clazz
	 * @param forceUpdate
	 * @throws Exception
	 */
	public <T extends AbstractEntity> void syncEntityDeltaByAplicacion(Class<T> clazz, boolean forceUpdate) throws Exception {
		Log.d(LOG_TAG, "syncEntityDelta: enter");
		// Retrieve TablaVersion by tableName
		TablaVersionDAO tablaVersionDAO =  DAOFactory.getInstance().getTablaVersionDAO();
		TablaVersion tablaVersion = tablaVersionDAO.findByTableName(clazz.getSimpleName());
		// Checking update version
		Integer version = forceUpdate? 0 : tablaVersion.getLastVersion();

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("entidad", clazz.getSimpleName());
		params.put("version", version.toString());
		params.put("codigoApp", Constants.COD_APPLICATION);

		Log.d(LOG_TAG, String.format("entity: %s version: %s force: %s",
				clazz.getSimpleName(), version, forceUpdate));
		// Invoke WS
		String response = secureWebGetInvoke("delta/listByAplicacion.json", params);

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
	 * Syncronize local formulario to server.
	 * 
	 * @param afiliacion
	 * @return
	 * @throws ReplyException 
	 */
	public synchronized boolean syncFormulario(Formulario formulario) throws ReplyException {
		Log.i(LOG_TAG, "syncFormulario: enter");

		String signature = Base64.encodeToString(formulario.getFirmaDigital(), Base64.DEFAULT);
		// Normalizo la entidad
		formulario.setFirmaDigital(null);

		// 
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
				return new JsonPrimitive(date.getTime());
			}});
		gsonBuilder.registerTypeAdapter(byte[].class, new JsonSerializer<byte[]>() {
			@Override
			public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(Base64.encodeToString(src, Base64.DEFAULT));
			}});
		
		//Log.d("FOTO", "syncFormulario: ECHACINICIO: Date= "+formulario.ge);
		
		Log.d(LOG_TAG, "syncFormulario: FECHACINICIO: Date= "+formulario.getFechaInicio()+" Time="+formulario.getFechaInicio().getTime());
		Log.d(LOG_TAG, "syncFormulario: FECHACIERRE: Date= "+formulario.getFechaCierre()+" Time"+formulario.getFechaCierre().getTime());

		//
		new GraphAdapterBuilder()
		.addType(Formulario.class)
		.addType(FormularioDetalle.class).registerOn(gsonBuilder);

		String json = gsonBuilder.create().toJson(formulario);
		
		Log.d(LOG_TAG, "DOMINIO JSON = "+json);

		XmlSerializer serializer = Xml.newSerializer();
		StringWriter data = new StringWriter();
		try {
			serializer.setOutput(data);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "data");
			serializer.startTag("", "json");
			serializer.text(json);
			serializer.endTag("", "json");
			serializer.startTag("", "signature");
			serializer.text(signature);
			serializer.endTag("", "signature");
			serializer.endTag("", "data");
			serializer.endDocument();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE, 
					"Error al generar XML file");
		} 

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data.toString());
		try {
			// Invoke WS
			String response = secureWebPostInvoke("formulario.json", params);
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
	 * Syncronize local panico to server.
	 * 
	 * @param panico
	 * @return
	 * @throws ReplyException 
	 */
	public synchronized boolean syncPanico(Panico panico) throws ReplyException {
		Log.i(LOG_TAG, "syncPanico: enter");

		String data = gson.toJson(panico);
		// Load POST Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		try {
			// Invoke WS
			String response = secureWebPostInvoke("panico.json", params);
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
	 * @param tableName
	 * @param usuarioID
	 * @param forceUpdate
	 * @throws Exception
	 */
	public void syncEntityDeltaBinary(Integer usuarioID, boolean forceUpdate) throws ReplyException {
		Log.d(LOG_TAG, "syncEntityDeltaBinary: enter");
		String entityName = AplicacionBinarioVersion.class.getSimpleName();

		// Retrieve TablaVersion by tableName
		TablaVersionDAO tablaVersionDAO = DAOFactory.getInstance().getTablaVersionDAO();
		TablaVersion tablaVersion = tablaVersionDAO.findByTableName(entityName);
		// Checking update version
		Integer version = forceUpdate? 0 : tablaVersion.getLastVersion();

		// Load GET Parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("entidad", entityName);
		params.put("version", version.toString());
		params.put("codigoApp", Constants.COD_APPLICATION);

		Log.d(LOG_TAG, String.format("entity: %s version: %s force: %s", entityName, version, forceUpdate));
		// Invoke WS
		String response = secureWebGetInvoke("delta/listByAplicacion.json", params);

		// Deserialize json response
		Type type = new TypeToken<List<AplicacionBinarioVersion>>(){}.getType();
		List<AplicacionBinarioVersion> entityDelta = gson.fromJson(response, type);

		Map<String, AplicacionBinarioVersion> mToInstall = 
				new HashMap<String, AplicacionBinarioVersion>();

		RuntimeExceptionDao<AplicacionBinarioVersion, Integer>
		entityDAO = database.getRuntimeExceptionDao(AplicacionBinarioVersion.class);

		for (AplicacionBinarioVersion entity: entityDelta) {     
			Log.d(LOG_TAG, "Procesing: " + entity.getClass().getSimpleName());
			if (entity.getEstado() == 1) {
				Log.d(LOG_TAG, " Updating entity ...");
				entityDAO.createOrUpdate(entity);
				// Analizo que binario descargar
				int tipoBinario = entity.getAplicacionTipoBinario().getId();
				String key = entity.getAplicacion().getId()+"#"+tipoBinario;
				// TipoBinario = Template o TipoBinario = DATA [multiples por aplicacion]
				if(tipoBinario == 2	|| tipoBinario == 3){
					key += entity.getId();
				} else{
					continue;
				}
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
			install(mToInstall.get(key));
		}

		Log.d(LOG_TAG, "Guardando version " + version);
		tablaVersion.setLastVersion(version);
		tablaVersionDAO.update(tablaVersion);
	}


	/**
	 * Download and install a binary file from URL via Http/Https GET.
	 * Note: installation execute only .apk's files.
	 * 
	 * @param fileurl
	 * @throws ReplyException 
	 */
	private void install(AplicacionBinarioVersion binary) throws ReplyException{
		try {
			Map<String, String> params = new HashMap<String, String>();
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

			String fileName = fileurl.substring(fileurl.lastIndexOf(File.separator) + 1);

			File file = null;
			switch (binary.getAplicacionTipoBinario().getId()) {
			case 1: // Core
				//NONE!!
				break;
			case 2: // Template: download file into internal template storage
				file = new File(appState.getDir("tpl", Context.MODE_PRIVATE), fileName);
				break;
			case 3: // DataBase: download data..
				file = new File(appState.getDir("tmp", Context.MODE_PRIVATE), fileName);
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

			if(binary.getAplicacionTipoBinario().getId() == 3){
				executeImport(file);
			}
		} catch (Exception e) {	  
			Log.e(LOG_TAG, "**ERROR**", e);
			throw new ReplyException(HttpStatus.SC_METHOD_FAILURE, e.getMessage());
		}
	} 

	/**
	 * 
	 * @param path
	 * @throws Exception
	 */
	private void executeImport(File file) throws Exception {
		// 
		String table = file.getName().substring(0, file.getName().lastIndexOf("."));
		String separator = "\\|";

		InputStream fis = new FileInputStream(file);
		InputStream bis = new BufferedInputStream(fis);
		//
		SQLHelper sql = new SQLHelper(appState);
		sql.openWritableDatabase();
		sql.delete(table,"", new String[]{});

		ContentValues values = new ContentValues();
		Scanner scanner = new Scanner(bis);
		String[] cols = scanner.nextLine().split(separator);
		sql.beginTransaction();
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			String[] data = line.split(separator);
			for (int i = 0; i < data.length; i++) {
				values.put(cols[i], data[i].trim());
			}
			sql.insert(table, values);
		}
		sql.setTransactionSuccessful();
		sql.endTransaction();
		sql.closeDatabase();
		scanner.close();
		bis.close();
		fis.close();

		file.delete();
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

	// Implementation helpers
	private static final String LOG_TAG = WebServiceController.class.getSimpleName();

	private static String WS_URL;
	private DatabaseHelper database;
	private Jsoner gson;
	private AIDigitalApplication appState;

	private WebServiceController(Context context) {
		database = (DatabaseHelper) OpenHelperManager.getHelper(context, DatabaseHelper.class);
		appState = (AIDigitalApplication) context.getApplicationContext();
		WS_URL = appState.getServerURL() + "/api/";
		gson = new GsonJsoner();
	}
}