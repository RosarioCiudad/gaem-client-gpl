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