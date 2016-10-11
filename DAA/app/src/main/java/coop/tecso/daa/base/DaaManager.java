package coop.tecso.daa.base;

import android.content.Context;
import android.util.Log;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.UsuarioApmDAO;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

public class DaaManager {

	private Context mContext;
	private static String LOG_TAG = DaaManager.class.getSimpleName();
	
	public DaaManager(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public UsuarioApm getUsuarioApm(String username, String password) {
		UsuarioApmDAO usuarioApmDAO = DAOFactory.getInstance().getUsuarioApmDAO();
		UsuarioApm usuarioApm = usuarioApmDAO.findByUserName(username);
		try{
			// Se verifica si existe el usuario en la db local
			if(null == usuarioApm){
				DAAApplication appState = (DAAApplication) mContext.getApplicationContext();
				// Si no existe se consulta a traves del WS
				usuarioApm = null; //TODO :WebServiceController.getInstance(mContext).login(username, password, "test");	 
				// Guardo el usuario localmente
				usuarioApmDAO.createOrUpdate(usuarioApm);
			}else{
				// Si existe se verifica el password 
				if(!password.equals(usuarioApm.getPassword())){
					// Si la validacion de password es incorrecta se devuelve null
					usuarioApm = null;
				}
			}
		} catch (Exception e) { 
			Log.e(LOG_TAG, "getUsuarioApm: **ERROR**", e);
		}	
		
		return usuarioApm;
	}

}