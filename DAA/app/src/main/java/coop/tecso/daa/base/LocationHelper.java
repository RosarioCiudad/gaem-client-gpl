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

package coop.tecso.daa.base;

import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import coop.tecso.daa.domain.util.DeviceContext;

/**
 * 
 * @author tecso.coop
 *
 */
public class LocationHelper {

	private static final String LOG_TAG = LocationHelper.class.getSimpleName();
	/**
	 * Singleton instance.
	 */
	private static LocationHelper INSTANCE;
	private LocationManager locationManager;
	private Context context;
	private Location currentBestLocation;

	/**
	 * Initialize the LocationHelper.
	 * 
	 * @param context The Android context.
	 */
	public static void init(Context context) {
		Log.d(LOG_TAG, "Initializing...");
		LocationHelper.INSTANCE = new LocationHelper(context);
	}

	/**
	 * 
	 * @param context
	 */
	private LocationHelper(Context context) {
		this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		this.context = context;

		//TODO:
	}

	/**
	 * Get the singleton instance of {@link LocationHelper}.
	 * 
	 * @return The singleton instance.
	 */
	public static LocationHelper getInstance() {
		if (INSTANCE == null) {
			Log.e(LOG_TAG, "LocationHelper not initialized!");
			throw new RuntimeException("LocationHelper not initialized!");
		}
		return INSTANCE;
	}


	public Location getCurrentLocation(){
		Log.d(LOG_TAG, "getCurrentLocation: enter");
		try {
			// 
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
					0, 0, locationListenerNetwork, Looper.getMainLooper());
			//
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					0, 0, locationListenerGps, Looper.getMainLooper());
			
			//obtengo LA ÚLTIMA MEJOR ubicación
			currentBestLocation = DeviceContext.getLastBestLocation(context);
			Log.d(LOG_TAG," determine location: " + (currentBestLocation != null));

			//Determinar si la fecha es del día, como mínimo.
			//			currentBestLocation = fechaDelDia(DeviceContext.getLastBestLocation(context));
			Log.d(LOG_TAG," determine location: " + (currentBestLocation != null));

			// No pudo determinar ubicacion
			if(currentBestLocation == null){
				Log.e(LOG_TAG, "**Cannot determine Location**");
				return null;
			}else{
				Log.d(LOG_TAG," location type: " + currentBestLocation.getProvider());
			}
		} catch (SecurityException e) {
			Log.e(LOG_TAG, "**ERROR**: No suitable permission is present for the provider", e);
		} catch (IllegalArgumentException e) {
			Log.e(LOG_TAG, "**ERROR**: Provider is null or doesn't exist", e);
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		Log.d(LOG_TAG, "getCurrentLocation: exit");
		return currentBestLocation;
	}

	public void finish(){
		locationManager.removeUpdates(locationListenerGps);
		locationManager.removeUpdates(locationListenerNetwork);
	}


	private LocationListener locationListenerGps = new LocationListener() {
		private final String LOG_TAG = LocationListener.class.getSimpleName();
		public void onLocationChanged(Location location) {
			locationManager.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
			Log.d(LOG_TAG, String.format("**PROVIDER '%s' DISABLED**", provider));
		}

		public void onProviderEnabled(String provider) {
			Log.d(LOG_TAG, String.format("**PROVIDER '%s' ENABLED**", provider));
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(LOG_TAG, String.format("**PROVIDER '%s' STATUS: %s**", provider, status));
		}
	};


	private LocationListener locationListenerNetwork = new LocationListener() {
		private final String LOG_TAG = LocationListener.class.getSimpleName();
		public void onLocationChanged(Location location) {
			locationManager.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
			Log.d(LOG_TAG, String.format("**PROVIDER '%s' DISABLED**", provider));
		}

		public void onProviderEnabled(String provider) {
			Log.d(LOG_TAG, String.format("**PROVIDER '%s' ENABLED**", provider));
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(LOG_TAG, String.format("**PROVIDER '%s' STATUS: %s**", provider, status));
		}
	};


	//	private Location fechaDelDia(Location currentBestLocation){
	//		Date now = new Date();
	//		Date locationTime = new Date(currentBestLocation.getTime());
	//		if(now.getYear() == locationTime.getYear() && now.getMonth() == locationTime.getMonth() && now.getDay() == locationTime.getDay()){
	//			return currentBestLocation;
	//		}
	//		return null;
	//	}

}