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

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

public final class GUIHelper {

	public static ProgressDialog getIndetProgressDialog(Context ctx, CharSequence title, CharSequence message) {
		return ProgressDialog.show(ctx, title, message, true, true);
	}
	
	public static void showRecoverableError(Context ctx, CharSequence error) {
		Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
	}
	
	public static void showError(Context ctx, CharSequence error) {
		Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
	}	
	
	public static boolean canHandleIntent(Context context, Intent intent){
	    PackageManager pm = context.getPackageManager();
	    List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return activities.size() > 0;
	}
}