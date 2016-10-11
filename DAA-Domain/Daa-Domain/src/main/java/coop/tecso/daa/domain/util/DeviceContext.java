package coop.tecso.daa.domain.util;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

/**
 * 
 * @author tecso.coop
 *
 */
public final class DeviceContext {
	// The maximum time that should pass before the user gets a location update
	private static long MAX_TIME = 30000L; 


	/**
	 * Returns the most accurate and timely previously detected location.
	 * Where the last result is beyond the specified maximum distance or 
	 * latency a one-off location update is returned via the {@link LocationListener}
	 * specified in {@link setChangedLocationListener}.
	 * @param minDistance Minimum distance before we require a location update.
	 * @param minTime Minimum time required between location updates.
	 * @return The most accurate and / or timely previously detected location.
	 */
	public static Location getLastBestLocation(Context context){
		
		long minTime = System.currentTimeMillis() - MAX_TIME;
		LocationManager locationManager = 
				(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		// Iterate through all the providers on the system, keeping
		// note of the most accurate result within the acceptable time limit.
		// If no result is found within maxTime, return the newest Location.
		List<String> matchingProviders = locationManager.getAllProviders();
		for (String provider: matchingProviders) {
			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if ((time > minTime && accuracy < bestAccuracy)) {
					bestResult = location;
					bestAccuracy = accuracy;
					bestTime = time;
				}else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
					bestResult = location;
					bestTime = time;
				}
			}
		}

		return bestResult;
	} 
	
	
	public static void disableBluetooth(){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasConnectivity(Context context){
		// Android Connectivity Service
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Check 3G connection
		boolean is3GConnected = connectivityManager.
				getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		// Check WIFI connection
		boolean isWIFIConnected = connectivityManager.
				getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

		return is3GConnected || isWIFIConnected;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context); 
		Account account = getAccount(accountManager);

		return (account == null)? "" : account.name;
	}

	/**
	 * Returns the unique device ID, for example, the IMEI for GSM 
	 * and the MEID or ESN for CDMA phones.
	 * Return null if device ID is not available. 
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager =
				(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		return telephonyManager.getDeviceId();
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static float getBatteryLevel(Context context) {
	    Intent batteryIntent = context.registerReceiver(null,
	    		new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	    // Error checking that probably isn't needed but I added just in case.
	    if(level == -1 || scale == -1) return 50.0f;

	    return ((float)level / (float)scale) * 100.0f; 
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static float getSignalLevel() {
//		SignalStrength signalStrength = new SignalStrength();
		int strength = 99;//signalStrength.getGsmSignalStrength(); //0-31
		// 0        -113 dBm or less  
		// 1        -111 dBm  
		// 2...30   -109... -53 dBm  
		// 31        -51 dBm or greater  
		//
		// 99 not known or not detectable
		if(strength == 99) return 0f;
	    return (strength * 100.0f)/31.0f; 
	}
	

	/**
	 * 
	 * @param accountManager
	 * @return
	 */
	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];      
		} else {
			account = null;
		}
		return account;
	}	

}