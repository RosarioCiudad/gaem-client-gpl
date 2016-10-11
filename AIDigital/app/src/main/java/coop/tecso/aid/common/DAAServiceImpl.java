package coop.tecso.aid.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.graph.GraphAdapterBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;

import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
import coop.tecso.daa.domain.base.Jsoner;
import coop.tecso.daa.domain.notificacion.Notificacion;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;
import coop.tecso.daa.domain.perfil.Campo;
import coop.tecso.daa.domain.perfil.CampoValor;
import coop.tecso.daa.domain.perfil.CampoValorOpcion;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.Impresora;
import coop.tecso.daa.domain.seguridad.UsuarioApm;
import coop.tecso.daa.domain.util.GsonJsoner;

public class DAAServiceImpl implements IDAAService {

	/**
	 * Constructor
	 */
	public DAAServiceImpl(Context context) {
		Log.d(LOG_TAG, "2 Bindeando servicio...");
//		Intent serviceIntent = new Intent("coop.tecso.IDAAService");
//		serviceIntent.setPackage("coop.tecso");
//		context.getApplicationContext().bindService(serviceIntent, svcConn, Context.BIND_AUTO_CREATE);
//		Log.d(LOG_TAG, "3 Bindeando servicio...");
//	
		//código original
		context.getApplicationContext().bindService(new Intent("coop.tecso.IDAAService"), svcConn, Context.BIND_AUTO_CREATE);
		Log.d(LOG_TAG, "3 Bindeando servicio...");
	}

	@Override
	public UsuarioApm login(String username, String password) throws Exception  {
		String json = rawService.login(username, password);		
		Log.d(LOG_TAG, "Usuario login: " + json);
		return gson.fromJson(json, UsuarioApm.class);		
	}

	@Override
	public void changeSession(String username, String password) throws Exception  {
		rawService.changeSession(username, password);		
		Log.d(LOG_TAG, "Cambio de Usuario. Nuevo usuario: " + username);
	}

	@Override
	public String getServerURL() throws Exception  {
		return rawService.getServerURL();		
	}

	@Override
	public boolean hasAccess(int usuarioID, String codAplicacion) throws Exception {
		return rawService.hasAccess(usuarioID, codAplicacion);
	}

	public Notificacion getNotificacionById(int notificacionID) throws Exception {
		String jo = rawService.getNotificacionById(notificacionID);
		Log.d(LOG_TAG, "Notificacion: "+jo);
		return new Gson().fromJson(jo, Notificacion.class);		
	}

	@Override
	public AplicacionPerfil getAplicacionPerfilById(int aplicacionPerfilId) throws Exception {
		String path = rawService.getAplicacionPerfilById(aplicacionPerfilId);
		Log.d(LOG_TAG, "HCD - PATH: " + path);

		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file), 8192);
		String line;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine()) != null){
			sb.append(line);
		}
		br.close();
		file.delete();

		GsonBuilder gsonBuilder = new GsonBuilder()
		.setDateFormat(DateFormat.LONG);

		new GraphAdapterBuilder()
		.addType(AplicacionPerfil.class) 
		.addType(AplicacionPerfilSeccion.class)
		.addType(AplPerfilSeccionCampo.class)
		.addType(AplPerfilSeccionCampoValor.class) 
		.addType(AplPerfilSeccionCampoValorOpcion.class)
		.addType(Campo.class)
		.addType(CampoValor.class)
		.addType(CampoValorOpcion.class).registerOn(gsonBuilder);
		
		return gsonBuilder.create().fromJson(sb.toString(), AplicacionPerfil.class);
	}

	@Override
	public Integer getIdAplicacionPerfilDefaultBy(String codAplicacion)
			throws Exception {
		return Integer.valueOf("1");
	}

	@Override
	public UsuarioApm getCurrentUser() throws Exception  {
		Log.d(LOG_TAG, "CHIMICHANGA: (rawService == null) = "+(rawService==null));
		System.out.println("la reputa que te pario");
		String jo = rawService.getCurrentUser();
		Log.d(LOG_TAG, "Usuario actual: " + jo);
		return gson.fromJson(jo, UsuarioApm.class);		
	}

	@Override
	public DispositivoMovil getDispositivoMovil() throws Exception {
		String json = rawService.getDispositivoMovil();
		Log.d(LOG_TAG, String.format("JSON: %s", json));
		return gson.fromJson(json, DispositivoMovil.class);	
	}
	
	@Override
	public Impresora getImpresora() throws Exception {
		String json = rawService.getImpresora();
		Log.d(LOG_TAG, String.format("JSON: %s", json));
		return gson.fromJson(json, Impresora.class);
	}

	@Override
	public AplicacionParametro getAplicacionParametroByCodigo(
			String codParametro, String codAplicacion) throws Exception {
		String json = rawService.getAplicacionParametroByCodigo(codParametro, codAplicacion);
		Log.d(LOG_TAG, "AplicacionParametro: " + json);
		return gson.fromJson(json, AplicacionParametro.class);
	}


	@Override
	public String signData(String data) throws Exception {
		Log.d(LOG_TAG, "signData: enter");
		
		return rawService.signData(data);
	}


	@Override
	public List<AplicacionPerfil> getListAplicacionPerfilByAplicacion(
			String codigoAplicacion) throws Exception {
		String response = rawService.getListAplicacionPerfilByAplicacion(codigoAplicacion);
		//
		Type type =  new TypeToken<List<AplicacionPerfil>>() {}.getType();
		return gson.fromJson(response, type);
	}


	// Implementation helpers
	private static final String LOG_TAG = DAAServiceImpl.class.getSimpleName();

	private coop.tecso.IDAAService rawService;
	private Jsoner gson;

	private ServiceConnection svcConn = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.d(LOG_TAG, "Bindeando servicio...");
			rawService = coop.tecso.IDAAService.Stub.asInterface(binder);
			Log.d(LOG_TAG, "CHIMICHANGA: (rawService == null) = "+(rawService==null));
			gson = new GsonJsoner();
		}

		public void onServiceDisconnected(ComponentName className) {
			rawService = null;
		}
	};
}