package coop.tecso.aid.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.base.LoginUserAsyncTask;
import coop.tecso.aid.utils.ButtonEnabler;

public final class GUIHelper {

	public static ProgressDialog getIndetProgressDialog(Context ctx, CharSequence title, CharSequence message) {
		return ProgressDialog.show(ctx, title, message, true, true);
	}

	public static void showMessage(Context ctx, CharSequence error) {
		Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
	}

	public static void showError(Context ctx, CharSequence error) {
		Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
	}	

	public static void showErrorLoseSession(final Activity context) {
		showAlert(context, "Error de Sesión", "Perdió sesión con MR-Admin. Intente loguearse nuevamente.");
	}

	public static void showAlert(final Activity context,String title, String message) {
		showAlert(context, title, message, true);
	}
	
	public static void showAlert(final Activity context,String title, String message, final boolean finish) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		alertDialog.setIcon(R.drawable.ic_error_default);
		alertDialog.setButton(
				DialogInterface.BUTTON_POSITIVE, 
				context.getString(R.string.accept),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						alertDialog.dismiss();
						if(finish) context.finish();
					}
				});
		alertDialog.show();
	}

	/**
	 * 
	 * Build login dialog 
	 * 
	 * @param dialog
	 */
	public static Dialog buildBlockingDialog(final Activity context, String userName){

		Dialog dialog = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		//		dialog.setContentView(R.layout.blocking_dialog);
		//		dialog.setTitle(context.getString(R.string.blocked_app_title));
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_login_mode);
		dialog.setCancelable(false);
		dialog.onAttachedToWindow();

		// User Name
		final EditText userNameView = (EditText) dialog.findViewById(R.id.username_edit_text);
		userNameView.setText(userName);
		// Password
		final EditText passwordView = (EditText) dialog.findViewById(R.id.password_edit_text);
		passwordView.requestFocus();

		// Confirm button
		Button buttonConfirm = (Button) dialog.findViewById(R.id.buttonConfirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AIDigitalApplication appState = (AIDigitalApplication) context.getApplication();

				if(appState.canAccess()){
					// Login Task
					new LoginUserAsyncTask(context).execute(userNameView.getText().toString(), passwordView.getText().toString());
				}else{
					showAlert(context, "Error de Sesión", "Error al iniciar sesión con UDAA.");
				}
			}
		});
		ButtonEnabler.register(buttonConfirm, userNameView, passwordView);

		return dialog;
	}
}