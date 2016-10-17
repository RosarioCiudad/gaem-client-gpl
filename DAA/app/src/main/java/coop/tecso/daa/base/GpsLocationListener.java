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