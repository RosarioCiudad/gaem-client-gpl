package coop.tecso.aid.gui.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Base64;
import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.daa.domain.base.AbstractEntity;

public class Utils {

	/**
	 * Completa un mapa con la lista de valores iniciales para cada campo. 
	 * 
	 * @param listValue
	 * @return mapInitialValues
	 */
	public static Map<String, List<Value>> fillInitialValuesMaps(List<Value> listValue){
		Map<String, List<Value>> mapInitialValues = new HashMap<String, List<Value>>();
		for (Value value : listValue) {
			String key = "";
			// Se carga el valor en la lista asociada al campo
			key = value.getCampo().getId()+"|0|0";
			List<Value> initialValues = mapInitialValues.get(key);
			if(initialValues == null){
				initialValues = new ArrayList<Value>();
			}
			initialValues.add(value);
			mapInitialValues.put(key, initialValues);

			// Se carga el valor en la lista asociada al campoValor
			if(isNotNull(value.getCampoValor())){
				key = value.getCampo().getId()+"|"+value.getCampoValor().getId()+"|0";
				initialValues = mapInitialValues.get(key);
				if(initialValues == null){
					initialValues = new ArrayList<Value>();
				}
				initialValues.add(value);
				mapInitialValues.put(key, initialValues);
			}

			// Se carga el valor en la lista asociada al campoValorOpcion
			if(isNotNull(value.getCampoValorOpcion())){
				key = value.getCampo().getId()+"|"+value.getCampoValor().getId()+"|"+value.getCampoValorOpcion().getId();
				initialValues = mapInitialValues.get(key);
				if(initialValues == null){
					initialValues = new ArrayList<Value>();
				}
				initialValues.add(value);
				mapInitialValues.put(key, initialValues);
			}
		}
		return mapInitialValues;
	}




	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatIniValueKey(Value value){
		String key = "";
		int idCampo = 0;
		int idCampoValor = 0;
		int idCampoValorOpcion = 0;
		if(isNotNull(value.getCampo()))
			idCampo = value.getCampo().getId();
		if(isNotNull(value.getCampoValor()))
			idCampoValor = value.getCampoValor().getId();
		if(isNotNull(value.getCampoValorOpcion()))
			idCampoValorOpcion = value.getCampoValorOpcion().getId();

		key = idCampo+"|"+idCampoValor+"|"+idCampoValorOpcion;

		return key;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public static boolean isNotNull(AbstractEntity entity){
		//
		if(null == entity || entity.getId() < 1){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateToJSON(Date date){
		String json = "";
		try {
			json = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
		} catch (Exception e) {}
		return json;
	}


	/**
	 * 
	 * @param byte[]
	 * @return
	 */
	public static String encodeToBase64(byte[] ba){
		return Base64.encodeToString(ba, Base64.DEFAULT);
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] decodeFromBase64(String str){
		return Base64.decode(str, Base64.DEFAULT);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getFormattedTitle(Context context){
		AIDigitalApplication app = (AIDigitalApplication) context.getApplicationContext();
		// Application version
		String versionName = "";
		try { versionName = "v" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName;
		} catch (Exception e) {}

		// Logged User
		String userName = "";
		if(null != app.getCurrentUser()){
			userName = app.getCurrentUser().getNombre();
		}

		return app.getString(R.string.app_header_title, versionName, userName);
	}

	/**
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean canHandleIntent(Context context, Intent intent){
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return activities.size() > 0;
	}

	/**
	 * Completa la cadena recibida con ceros a la izquierda hasta llegar a la longitud deseada.
	 * 
	 * @param valor
	 * @param cantidadCeros
	 * @return
	 */
	public static String completarCerosIzq(String valor, int longitud){
		try {
			StringBuilder builder = new StringBuilder();
			if (valor.length() < longitud){
				for(int i= valor.length() ; i < longitud ; i++) {
					builder.append("0");
				}
			}
			builder.append(valor);
			
			return builder.toString();
		} catch (Exception e) {
			return valor;
		}
	}	
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getNumberOfMonths(Date date1, Date date2){
		//
		Calendar firstDate = Calendar.getInstance();
		firstDate.setTime(date1);
		//
		Calendar secondDate = Calendar.getInstance();
		secondDate.setTime(date2);

		int months  = (firstDate.get(Calendar.YEAR) - secondDate.get(Calendar.YEAR)) * 12 +
				(firstDate.get(Calendar.MONTH)- secondDate.get(Calendar.MONTH)) + 
				(firstDate.get(Calendar.DAY_OF_MONTH) >= secondDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 

		return months;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isInteger(String string) {
	    try {
	        Integer.valueOf(string);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
}