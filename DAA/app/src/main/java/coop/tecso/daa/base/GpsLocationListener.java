package coop.tecso.daa.base;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author tecso.coop
 *
 */
public class GpsLocationListener implements LocationListener {
	
	private static final String LOG_TAG = GpsLocationListener.class.getSimpleName();
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(LOG_TAG, "onStatusChanged: ");
	}
	@Override
	public void onProviderEnabled(String provider) {
		Log.d(LOG_TAG, "onProviderEnabled: ");
	}
	@Override
	public void onProviderDisabled(String provider) {
		Log.d(LOG_TAG, "onProviderDisabled: ");
	}
	@Override
	public void onLocationChanged(Location location) {
		Log.d(LOG_TAG, "onLocationChanged: " + location.getLatitude() + " - " + location.getLongitude());
	}
}