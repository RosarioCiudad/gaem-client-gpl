package coop.tecso.daa.base;

import java.security.PrivateKey;

import android.app.Application;
import android.app.Dialog;
import android.util.Log;
import coop.tecso.daa.R;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.domain.seguridad.DispositivoMovil;
import coop.tecso.daa.domain.seguridad.UsuarioApm;

/**
 * 
 * @author tecso.coop
 *
 */
public class DAAApplication extends Application {

	private static final String LOG_TAG = DAAApplication.class.getSimpleName();

	private boolean localLogin;
	private UsuarioApm currentUser;
	private DispositivoMovil dispositivoMovil;
	private int localLoginCount = 0; 

	// Cambio de usuario
	private boolean changeSession;
	private String userNameForNewSession = "";

	// Session variables
	private boolean blocked = false;
	private Dialog  blockDialog;
	private long lastInteactionTime;
	//
	private PrivateKey privateKey;
	//

	@Override
	public void onCreate() {
		super.onCreate();
		// DAO Entities
		DAOFactory.init(this);
		// GPS Location 
		LocationHelper.init(this);
	}

	// Getters & Setters
	public UsuarioApm getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UsuarioApm currentUser) {
		this.currentUser = currentUser;
	}

	public DispositivoMovil getDispositivoMovil() {
		return dispositivoMovil;
	}

	public void setDispositivoMovil(DispositivoMovil dispositivoMovil) {
		this.dispositivoMovil = dispositivoMovil;
	}

	public boolean isLocalLogin() {
		return localLogin;
	}

	public void setLocalLogin(boolean localLogin) {
		this.localLogin = localLogin;
	}

	public int getLocalLoginCount() {
		return localLoginCount;
	}

	public void addLocalLoginCount() {
		this.localLoginCount++;
	}

	public boolean isChangeSession() {
		return changeSession;
	}

	public void setChangeSession(boolean changeSession) {
		this.changeSession = changeSession;
	}

	public void reset() {
		// Clean up the logged user.
		setCurrentUser(null);
		this.localLoginCount = 0;
	}

//	public void changeSession(String userName) {
//		// Clean up the logged user.
//		setCurrentUser(null);
//
//		changeSession = true;
//		userNameForNewSession = userName;
//
//		// Enviar mensaje de cierre del manager
//		Intent msg = new Intent();
////		msg.setAction(Constants.ACTION_LOSE_SESSION);
//		this.sendBroadcast(msg);
//	}

	/**
	 * 
	 * @param blocked
	 */
	public void setLocked(boolean blocked){
		lastInteactionTime = System.currentTimeMillis();
		this.blocked = blocked;
		if(!blocked && null != blockDialog){
			try {blockDialog.dismiss();
			} catch (Exception e) {}
		}
	}

	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * 
	 * @return
	 */
	public String getFormattedTitle(){
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			Log.e(LOG_TAG, "getFormattedTitle: **ERROR**", e);
		}
		// Logged User
		String userName = "";
		if(null != getCurrentUser()){
			userName = getCurrentUser().getNombre();
		}
		return getString(R.string.app_header_title, versionName, userName);
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle(){
		String versionName;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			versionName = "";
			Log.e(LOG_TAG, "getTitle: **ERROR**", e);
		}
		return getString(R.string.app_name, versionName);
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
}