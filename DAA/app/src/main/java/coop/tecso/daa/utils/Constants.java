package coop.tecso.daa.utils;

/**
 * 
 * Constantes generales UDAA
 * 
 * @author tecso
 *
 */
public class Constants {

	// Codigos de Aplicacion
	public static final String COD_DAA = "DAA";
	public static final String COD_HCDIGITAL = "HCDigital";
	
	// Codigos de Aplicacion
	public static final int APLICACION_HCDIGITAL_ID = 1;
	
	// Codigos de AplicacionParametro
	public static final String COD_LOCAL_LOGIN_MAX_COUNT = "maxlocalloginattempt";
	public static final String COD_REMOTE_LOGIN_MAX_COUNT = "maxremoteloginattempt";
	public static final String COD_SESSION_TIME_OUT = "sessionTimeOut";
	public static final String COD_DEVICE_ID = "deviceID";
	public static final String COD_EMERGENCY_CHANNEL_PERIOD = "C2DMServiceTestInterval";
	public static final String COD_SESSION_TIMEOUT = "sessionTimeOut";
	public static final String COD_GCM_PROJECT_ID = "GCMProjectID";
	
	// Codigos de Preferencias
	public static final String PREF_EMAIL = "MAIL";
	public static final String PREF_REGISTRATION_ID = "RegistrationID";
	
	// Tipos de Notificaciones
	public static final int TIPO_NOTIFICACION_GENERAL_ID = 1;		// General
	public static final int TIPO_NOTIFICACION_APLICACION_ID = 2;	// Asociada a Aplicación
	public static final int TIPO_NOTIFICACION_ACT_DATOS_ID = 3;     // Actualización de Datos
	public static final int TIPO_NOTIFICACION_ACT_APL_ID = 4;       // Actualización de Aplicación
	public static final int TIPO_NOTIFICACION_ENTIDAD_APL_ID = 5;   // Asociada a Entidad de Aplicacion
	
	// Estado Notificacion
	public static final int ESTADO_NOTIFICACION_PENDIENTE_ID = 1;	// Pendiente
	public static final int ESTADO_NOTIFICACION_ENVIADA_ID = 2;		// Enviada
	
	// Block Message
	public static final String ACTION_BLOCK_APPLICATION = "coop.tecso.daa.custom.intent.action.BLOCK_APPLICATION";
	// UnBlock Message
	public static final String ACTION_UNBLOCK_APPLICATION = "coop.tecso.daa.custom.intent.action.UNBLOCK_APPLICATION";
	// Lose Session Message
	public static final String ACTION_LOSE_SESSION = "coop.tecso.daa.custom.intent.action.LOSE_SESSION";
	// New Notification Message
	public static final String ACTION_NEW_NOTIFICATION = "coop.tecso.daa.custom.intent.action.NEW_NOTIFICATION";
	
	// Codigos LocationGPS
	public static final String COD_TIMER_PERIOD = "TimerLocationInterval";
	public static final String COD_TIPO_POS = "TipoPosicionamiento";
	public static final String COD_UMBRAL_BAT = "UmbralBateria";
	public static final String COD_NUM_MAX_POS_TRANS = "NumMaxPosTrans";
	
	// GPS variables
	public static final int TIMER_DESHABILITADO = 0;
	public static final int TIPO_PROVEEDOR_GPS = 1;
	public static final int TIPO_PROVEEDOR_NET = 2;
	
	
	// Impresion
	public static final String ACTION_PRINT_FORM_SUCCESS = "coop.tecso.daa.custom.intent.action.PRINT_FORM_SUCCESS";
	public static final String ACTION_PRINT_FORM_ERROR = "coop.tecso.daa.custom.intent.action.PRINT_FORM_ERROR";
}