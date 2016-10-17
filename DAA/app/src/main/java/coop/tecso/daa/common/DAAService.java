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

package coop.tecso.daa.common;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.Signature;
import java.text.DateFormat;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.graph.GraphAdapterBuilder;

import coop.tecso.IDAAService;
import coop.tecso.daa.R;
import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.base.DaaManager;
import coop.tecso.daa.dao.AplicacionBinarioVersionDAO;
import coop.tecso.daa.dao.AplicacionDAO;
import coop.tecso.daa.dao.AplicacionPerfilDAO;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.NotificacionDAO;
import coop.tecso.daa.dao.UsuarioApmDAO;
import coop.tecso.daa.domain.aplicacion.AplicacionParametro;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
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



public class DAAService extends Service {

	private static final String LOG_TAG = DAAService.class.getSimpleName();


	private final IDAAService.Stub binder = new IDAAService.Stub() {

		@Override
		public String getCurrentUser() throws RemoteException {
			DAAApplication appContext = (DAAApplication) getApplication();
			if (appContext.getCurrentUser() != null) { 

				UsuarioApmDAO usuarioApmDAO = DAOFactory.getInstance().getUsuarioApmDAO();
				//
				UsuarioApm usuarioApm = usuarioApmDAO.findById(appContext.getCurrentUser().getId());

				return gson.toJson(usuarioApm);				
			}
			return null;
		}

		@Override
		public String getServerURL() throws RemoteException {
			SharedPreferences myPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
			Log.d(LOG_TAG,"getServerURL: enter");
			String serverURL = myPrefs.getString("URL", getString(R.string.server_url));
			Log.i(LOG_TAG," serverURL: "+serverURL);
			Log.d(LOG_TAG,"getServerURL: exit");
			return  serverURL;
		}

		@Override
		public String getAplicacionPerfilById(int aplicacionPerfilId) throws RemoteException {
			try {
				AplicacionPerfilDAO aplicacionPerfilDAO;
				aplicacionPerfilDAO = DAOFactory.getInstance().getAplicacionPerfilDAO();

				GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(DateFormat.LONG);

				new GraphAdapterBuilder()
				.addType(AplicacionPerfil.class) 
				.addType(AplicacionPerfilSeccion.class)
				.addType(AplPerfilSeccionCampo.class)
				.addType(AplPerfilSeccionCampoValor.class) 
				.addType(AplPerfilSeccionCampoValorOpcion.class)
				.addType(Campo.class)
				.addType(CampoValor.class)
				.addType(CampoValorOpcion.class).registerOn(gsonBuilder);

				AplicacionPerfil ap = aplicacionPerfilDAO.findById(aplicacionPerfilId);
				String res = gsonBuilder.create().toJson(ap);

				// Create file 
				String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				File file = new File(path + File.separator + "apl.json");
				BufferedWriter out = new BufferedWriter(new FileWriter(file), 8192);
				out.write(res);
				//Close the output stream
				out.close();

				Log.d(LOG_TAG,"UDDA - PATH: "+file.getPath());
				return file.getPath();
			} catch (Exception e) {
				Log.e(LOG_TAG, "BOOM", e);
				return null;
			}
		}

		@Override
		public String login(String username, String password) throws RemoteException {
			UsuarioApm usuarioApm = new DaaManager(DAAService.this).getUsuarioApm(username, password);
			try {
				return gson.toJson(usuarioApm);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				return null;
			}
		}

		@Override
		public String getDispositivoMovil() throws RemoteException {
			DAAApplication udaaApplication = (DAAApplication) getApplicationContext();
			try {
				DispositivoMovil dispositivoMovil = udaaApplication.getDispositivoMovil();
				return gson.toJson(dispositivoMovil);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				return null;
			}
		}
		
		@Override
		public String getImpresora() throws RemoteException {
			DAAApplication appContext = (DAAApplication) getApplication();
			try {
				Impresora impresora = DAOFactory.getInstance().
						getImpresoraDAO().findByUsuarioApm(appContext.getCurrentUser());
				return gson.toJson(impresora);
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
				return null;
			}
		}

		@Override
		public String getNotificacionById(int notificacionID) throws RemoteException {
			try {
				NotificacionDAO notificacionDAO = DAOFactory.getInstance().getNotificacionDAO();

				Notificacion notificacion = notificacionDAO.findById(notificacionID);
				String res = gson.toJson(notificacion);

				Log.d(LOG_TAG,"UDDA - Notificacion JSON: "+res);
				return res;
			} catch (Exception e) {
				Log.d(LOG_TAG, "BOOM");
				return null;
			}
		}

		@Override
		public void changeSession(String username, String password) throws RemoteException {
//			DAAApplication appContext = (DAAApplication) getApplication();
//			appContext.changeSession(username);
		}

		@Override
		public boolean hasAccess(int usuarioID, String codAplicacion)
				throws RemoteException {

			AplicacionDAO aplicacionDAO = DAOFactory.getInstance().getAplicacionDAO();
			return  aplicacionDAO.hasAccess(usuarioID, codAplicacion);
		}

		@Override
		public String getAplicacionParametroByCodigo(String codParametro,
				String codAplicacion) throws RemoteException {
			DAOFactory dao = DAOFactory.getInstance();
			AplicacionParametro aplicacionParametro = dao.getAplicacionParametroDAO().findByCodigo(codParametro, codAplicacion);
			if (aplicacionParametro != null) { 
				return gson.toJson(aplicacionParametro);				
			}
			return null;
		}

		@Override
		public String getListBinarioPathBy(String codAplicacion,
				String tipoBinario) throws RemoteException {
			Log.i(LOG_TAG,"getListBinarioPathBy: enter");

			AplicacionBinarioVersionDAO aplicacionBinarioVersionDAO;
			aplicacionBinarioVersionDAO = DAOFactory.getInstance().getAplicacionBinarioVersionDAO();

			List<String> pathList = aplicacionBinarioVersionDAO.getListPathBy(codAplicacion, tipoBinario);
			String result = new Gson().toJson(pathList);

			Log.i(LOG_TAG,"getListBinarioPathBy: exit");
			return result;
		}

		@Override
		public String getListAplicacionPerfilByAplicacion(String codigoAplicacion)
				throws RemoteException {
			Log.i(LOG_TAG,"getListAplicacionPerfilByAplicacion: enter");
			// 	
			AplicacionPerfilDAO aplicacionPerfilDAO =
					DAOFactory.getInstance().getAplicacionPerfilDAO();

			DAAApplication appContext = (DAAApplication) getApplication();

			List<AplicacionPerfil> aplicacionPerfiList = 
					aplicacionPerfilDAO.getListByUsuarioAndCodAplicacion(codigoAplicacion, appContext.getCurrentUser());
			for (AplicacionPerfil aplicacionPerfil : aplicacionPerfiList) {
				aplicacionPerfil.setApPerfilSeccionList(null);
				aplicacionPerfil.setAplicacion(null);
			}
			Log.i(LOG_TAG,"getListAplicacionPerfilByAplicacion: exit");
			return gson.toJson(aplicacionPerfiList);
		}

		@Override
		public String signData(String data) throws RemoteException {
			DAAApplication appState = (DAAApplication) getApplication();
			try {
				// Sign
				Signature signature = Signature.getInstance("SHA1WithRSA");
				signature.initSign(appState.getPrivateKey());
				signature.update(data.getBytes());
				// Sign Data
				byte[] encData = signature.sign();
				return Base64.encodeToString(encData, Base64.DEFAULT);

				// Encrypt
				//				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
				//				cipher.init(Cipher.ENCRYPT_MODE, appState.getPrivateKey());
				//				//			cipher.init(Cipher.DECRYPT_MODE, cert.getPublicKey()); //NO FUNCIONA! 
				//				byte[] eMsg = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
				//				Log.i(LOG_TAG,"DATA: " +data);
				//				Log.i(LOG_TAG,"eDATA: " +new String(eMsg));
				//				return  Base64.encodeToString(eMsg, Base64.DEFAULT);
			} catch (Exception e) {
				Log.i(LOG_TAG,"**ERROR**", e);
				return null;
			}
		}
	};

	private GsonJsoner gson;
	@Override
	public void onCreate() {
		this.gson = new GsonJsoner();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}