package coop.tecso.aid.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.graph.GraphAdapterBuilder;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.common.DAAServiceImpl;
import coop.tecso.aid.common.WebServiceController;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.dao.PanicoDAO;
import coop.tecso.aid.dao.TalonarioDAO;
import coop.tecso.aid.dao.TelefonoPanicoDAO;
import coop.tecso.aid.entities.ClaseLicencia;
import coop.tecso.aid.entities.EstadoTipoFormulario;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.FormularioDetalle;
import coop.tecso.aid.entities.MotivoAnulacionTipoFormulario;
import coop.tecso.aid.entities.MotivoCierreTipoFormulario;
import coop.tecso.aid.entities.Panico;
import coop.tecso.aid.entities.Talonario;
import coop.tecso.aid.entities.TelefonoPanico;
import coop.tecso.aid.entities.TipoDocumento;
import coop.tecso.aid.entities.TipoFormulario;
import coop.tecso.aid.entities.UsuarioApmReparticion;
import coop.tecso.aid.gui.GUIVisitor;
import coop.tecso.aid.gui.components.DominioGUI;
import coop.tecso.aid.gui.components.PerfilGUI;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.gui.utils.Utils;
import coop.tecso.aid.helpers.ParamHelper;
import coop.tecso.aid.printer.PrinterHelper;
import coop.tecso.aid.utils.ButtonEnabler;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.base.ReplyException;
import coop.tecso.daa.domain.seguridad.Impresora;
import coop.tecso.daa.domain.util.DeviceContext;


/**
 * 
 * @author tecso.coop
 *
 */
public class AIDigitalManager {

	private static final String LOG_TAG = AIDigitalManager.class.getSimpleName();

	private Context context;
	private DAAServiceImpl daaService;
	private AIDigitalApplication appState; 

	/**
	 * 
	 * @param context
	 */
	public AIDigitalManager(Context context) {
		this.context = context;
		this.daaService = new DAAServiceImpl(context);
		this.appState = (AIDigitalApplication) context.getApplicationContext();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Formulario getFormForCreate(Integer perfilID, Integer tipoFormularioID) throws Exception {
		Log.i(LOG_TAG, "getFormForCreate: enter");

		Formulario formulario = new Formulario();

		AplicacionPerfil aplicacionPerfil = daaService.getAplicacionPerfilById(perfilID);
		// EstadoAfiliacion: EN_PREPARACION
		EstadoTipoFormulario estadoFormulario = 
				DAOFactory.getInstance().getEstadoTipoFormularioDAO().findById(EstadoTipoFormulario.ID_EN_PREPARACION); 
		Log.d(LOG_TAG, "getFormForCreate -> estadoFormulario: "+estadoFormulario);
		// TipoFormulario:
		TipoFormulario tipoFormulario = DAOFactory.getInstance().getTipoFormularioDAO().findById(tipoFormularioID);
		Log.d(LOG_TAG, "getFormForCreate -> tipoFormulario: "+tipoFormulario);

		formulario.setTipoFormularioDef(aplicacionPerfil);
		formulario.setTipoFormulario(tipoFormulario);
		formulario.setEstadoTipoFormulario(estadoFormulario);

		formulario.setDispositivoMovil(appState.getDispositivoMovil());
		formulario.setUsuarioCierre(appState.getCurrentUser());
		formulario.setFechaInicio(new Date());
		formulario.setListFormularioDetalle(new ArrayList<FormularioDetalle>());
		//--setear posicion
		try {
			Location location = DeviceContext.getLastBestLocation(context);

			formulario.setLatitud(location.getLatitude());
			formulario.setLongitud(location.getLongitude());
			formulario.setPrecision((double) location.getAccuracy());
			//
			if(LocationManager.GPS_PROVIDER.equals(location.getProvider())){
				formulario.setOrigen(1); // 1-GPS
			}else{
				formulario.setOrigen(2); // 2-Antena
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(location.getTime());
			formulario.setFechaMedicion(calendar.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Reparticion
		UsuarioApmReparticion usuarioApmReparticion = 
				DAOFactory.getInstance().getUsuarioApmReparticionDAO().findByTipoFormulario(tipoFormulario);
		if(usuarioApmReparticion == null){
			throw new ReplyException(500, "No se pudo obtener repartición asociada al usuario");
		}

		formulario.setReparticion(usuarioApmReparticion.getReparticion().getId());
		formulario.setNumeroInspector(usuarioApmReparticion.getNumeroInspector());

		// Seteo: Numero y Serie
		if(false == generateNumero(formulario)){
			throw new ReplyException(500, "No se pudo generar número de acta");
		}

		// Guardo el Formulario
		DAOFactory.getInstance().getFormularioDAO().create(formulario);

		Log.i(LOG_TAG, "getFormForCreate: exit");
		return formulario;
	}

	/**
	 * 
	 * @param formularioID
	 * @return
	 * @throws Exception
	 */
	public Formulario getFormForUpdate(int formularioID) throws Exception{
		Log.i(LOG_TAG, "getFormForUpdate: enter");
		//
		Formulario formulario = DAOFactory.getInstance().getFormularioDAO().findById(formularioID);

		DAOFactory.getInstance().getTipoFormularioDAO().refresh(formulario.getTipoFormulario());
		// Perfil asociado
		AplicacionPerfil aplicacionPerfil = daaService.getAplicacionPerfilById(formulario.getTipoFormularioDef().getId());
		formulario.setTipoFormularioDef(aplicacionPerfil);

		Log.i(LOG_TAG, "getFormForUpdate: exit");
		return formulario;
	}

	/**
	 * 
	 * @param formulario
	 * @return
	 */
	public PerfilGUI buildForm(Formulario formulario) {
		Log.i(LOG_TAG, "buildForm: enter");

		List<Value> listValue = new ArrayList<Value>();
		for (FormularioDetalle formularioDetalle : formulario.getListFormularioDetalle()) {
			Log.d(LOG_TAG, "FormularioDetalle - Valor: " + formularioDetalle.getValor());
			Value value = new Value(formularioDetalle.getTipoFormularioDefSeccionCampo(),
					formularioDetalle.getTipoFormularioDefSeccionCampoValor(),
					formularioDetalle.getTipoFormularioDefSeccionCampoValorOpcion(),
					formularioDetalle.getValor(), 
					formularioDetalle.getImagen());
			listValue.add(value);
		}
		boolean enabled = (formulario.getEstadoTipoFormulario().getId() == EstadoTipoFormulario.ID_EN_PREPARACION);
		Log.d(LOG_TAG, "buildForm: DEBUG 1: formulario: "+formulario.getId());
		Log.d(LOG_TAG, "buildForm: DEBUG 2: estadoFormulario: "+formulario.getEstadoTipoFormulario().getId());
		Log.d(LOG_TAG, "buildForm: DEBUG 3: aplicacionPerfilId: "+formulario.getTipoFormularioDef().getId());
		Log.d(LOG_TAG, "buildForm: DEBUG 4: enabled:" +enabled);

		GUIVisitor visitor = new GUIVisitor(context);
		PerfilGUI form = (PerfilGUI) visitor.buildComponents(formulario.getTipoFormularioDef(), listValue, enabled);
		form.setEntity(formulario);
		Log.i(LOG_TAG, "buildForm: exit");
		return form;
	}

	/**
	 * 
	 *  Guarda la afiliacion persistiendo los datos en la db local.  
	 * 
	 * @param form
	 * @throws Exception
	 */
	public Formulario saveForm(PerfilGUI form) throws Exception {
		Log.i(LOG_TAG, "saveForm: enter");

		Formulario formulario = (Formulario) form.getEntity();
		List<Value> listValue = form.dirtyValues(); 

		SparseArray<Value> mapIdCampo = new SparseArray<Value>();
		for (Value value: listValue) {
			mapIdCampo.append(value.getCampo().getId(), value);
		}

		EstadoTipoFormulario estadoTipoFormulario =
				DAOFactory.getInstance().getEstadoTipoFormularioDAO().findById(EstadoTipoFormulario.ID_CERRADA_PROVISORIA);

		formulario.setUsuarioCierre(appState.getCurrentUser());
		formulario.setEstadoTipoFormulario(estadoTipoFormulario);

		try {
			// Dominio
			int dominioId = ParamHelper.getInteger(ParamHelper.DOMINIO);
			Value value = mapIdCampo.get(dominioId);
			String dominio = DominioGUI.format(value.getValor());
			formulario.setDominio(dominio);
		} catch (Exception e) {
			Log.d(LOG_TAG, "**ERROR**", e);
		}
		
		// Recreo digitalmente el formulario
		Bitmap bitmap = stampDataInBitmap(form.getPrinterValor());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		formulario.setFormularioDigital(stream.toByteArray());
		Log.i(LOG_TAG, "saveForm: exit");

		// Create
		DAOFactory.getInstance().getFormularioDAO().createOrUpdate(formulario);

		Log.i(LOG_TAG, "FormularioValor to create = " + listValue.size());
		// Guardar AfiliacionValor
		for (Value value: listValue) {
			FormularioDetalle formularioDetalle = new FormularioDetalle();
			formularioDetalle.setFormulario(formulario);
			formularioDetalle.setTipoFormularioDefSeccionCampo(value.getCampo());
			formularioDetalle.setTipoFormularioDefSeccionCampoValor(value.getCampoValor());
			formularioDetalle.setTipoFormularioDefSeccionCampoValorOpcion(value.getCampoValorOpcion());
			formularioDetalle.setValor(value.getValor());
			formularioDetalle.setImagen(value.getImagen());

			Log.d(LOG_TAG, " - Creating formularioDetalle : " + formularioDetalle.getValor());
			DAOFactory.getInstance().getFormularioDetalleDAO().createOrUpdate(formularioDetalle);
		}



		return formulario;
	}

	/**
	 * 
	 *  Cierra Formulario definitivamente  
	 * 
	 * @param form
	 * @throws Exception
	 */
	public Formulario closeForm(PerfilGUI form, int motivoCierreID) {
		Log.i(LOG_TAG, "closeForm: enter");

		// Formulario
		Formulario formulario = (Formulario) form.getEntity();

		// EstadoTipoFormulario: Cerrado Definitivamente
		EstadoTipoFormulario estadoTipoFormulario =
				DAOFactory.getInstance().getEstadoTipoFormularioDAO().findById(EstadoTipoFormulario.ID_CERRADA_DEFINITIVA);

		// MotivoCierreTipoFormulario
		MotivoCierreTipoFormulario motivoCierreTipoFormulario =
				DAOFactory.getInstance().getMotivoCierreTipoFormularioDAO().findById(motivoCierreID);

		formulario.setMotivoCierreTipoFormulario(motivoCierreTipoFormulario);
		formulario.setEstadoTipoFormulario(estadoTipoFormulario);
		//		formulario.setCantidadImpresisones(cantidadImpresiones);
		formulario.setFechaCierre(new Date());

		// Firmo digitalmente el formulario
		String signature = signData(formulario);
		formulario.setFirmaDigital(Base64.decode(signature,Base64.DEFAULT));

		// Actualizo el formulario
		DAOFactory.getInstance().getFormularioDAO().update(formulario);

		Log.i(LOG_TAG, "closeForm: exit");
		return formulario;
	}

	/**
	 * 
	 *  Anula Formulario definitivamente
	 * 
	 * @param form
	 * @throws Exception
	 */
	public Formulario anularForm(PerfilGUI form, int motivoAnulacionID) {
		Log.i(LOG_TAG, "anularForm: enter");

		// Formulario
		Formulario formulario = (Formulario) form.getEntity();

		// EstadoTipoFormulario: Anulado
		EstadoTipoFormulario estadoTipoFormulario =
				DAOFactory.getInstance().getEstadoTipoFormularioDAO().findById(EstadoTipoFormulario.ID_ANULADA);

		// MotivoCierreTipoFormulario
		MotivoAnulacionTipoFormulario motivoAnulacion =
				DAOFactory.getInstance().getMotivoAnulacionTipoFormularioDAO().findById(motivoAnulacionID);

		formulario.setMotivoAnulacionTipoFormulario(motivoAnulacion);
		formulario.setEstadoTipoFormulario(estadoTipoFormulario);
		formulario.setFechaCierre(new Date());

		// Firmo digitalmente el formulario
		String signature = signData(formulario);
		formulario.setFirmaDigital(Base64.decode(signature,Base64.DEFAULT));

		// Actualizo el formulario
		DAOFactory.getInstance().getFormularioDAO().update(formulario);

		Log.i(LOG_TAG, "anularForm: exit");
		return formulario;
	}

	/**
	 * 
	 * @return
	 */
	private String signData(Formulario formulario){

		formulario = DAOFactory.getInstance().getFormularioDAO().findById(formulario.getId());
		// 
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
				return new JsonPrimitive(date.getTime());
			}});
		gsonBuilder.registerTypeAdapter(byte[].class, new JsonSerializer<byte[]>() {
			@Override
			public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
				return new JsonPrimitive(Base64.encodeToString(src, Base64.DEFAULT));
			}});

		//
		new GraphAdapterBuilder()
		.addType(Formulario.class)
		.addType(FormularioDetalle.class).registerOn(gsonBuilder);

		String data = gsonBuilder.create().toJson(formulario);

		String signedData = null;
		try {
			signedData = daaService.signData(data);
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		return signedData;
	}


	public boolean checkPrinterStatus() {
		Log.i(LOG_TAG, "checkPrinterStatus: enter");

		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivity(intent);
			return false;
		} 

		try {
			StarIOPort.releasePorts();
		} catch (StarIOPortException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		PrinterHelper printer = new PrinterHelper(context);
		StringBuilder portName = new StringBuilder("BT:");

		// Connect with specified printer
		Impresora impresora = null;
		try {
			impresora = daaService.getImpresora();
			if(impresora != null){
				// Port = BT:XX:XX:XX:XX:XX:XX
				portName.append(impresora.getNumeroUUID());
				if(PrinterHelper.STATUS_OK != 
						printer.checkStatus(portName.toString())){
					// Reset portName to print on any printer
					portName = new StringBuilder("BT:");
				}
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, "**ERROR**", e);
		}

		StringBuilder message = new StringBuilder();
		try {
			// Start connection
			// Checking status
			int status = printer.checkStatus(portName.toString());
			if(PrinterHelper.STATUS_OK == status){
				printer.openConnection(portName.toString());
				StringBuilder data = new StringBuilder();
				data.append("#### Impresión Satisfactoria<br>#### ");
				data.append(new SimpleDateFormat(
						"dd/MM/yyyy HH:mm",Locale.US).format(new Date()));
				data.append("<br><br><br><br><br><br><br><br>");
				printer.writeData(data.toString());
				message.append("Impresión Satisfactoria");
			}else{
				// OFFLINE Printer
				message.append("Impresora fuera de linea");
				if(status == PrinterHelper.STATUS_COVER_OPEN)
					message.append("\n- La tapa se encuentra abierta");
				if(status == PrinterHelper.STATUS_PAPER_EMPTY)
					message.append("\n- Papel agotado");
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			message.append("Se produjo un error durante la impresión: ");
			message.append(e.getMessage());
		} finally {
			printer.closeConnection();
		}
		Toast.makeText(context, message.toString(),Toast.LENGTH_LONG).show();
		return true;
	}


	public boolean generateReport(PerfilGUI form) {
		Log.i(LOG_TAG, "generateReport: enter");

		try {
			StarIOPort.releasePorts();
		} catch (StarIOPortException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		String data = form.getPrinterValor(true);
		PrinterHelper printer = new PrinterHelper(context);
		StringBuilder portName = new StringBuilder("BT:");
		// Connect with specified printer
		Impresora impresora = null;
		try {
			impresora = daaService.getImpresora();
			if(impresora != null){
				// Port = BT:XX:XX:XX:XX:XX:XX
				portName.append(impresora.getNumeroUUID());
				if(PrinterHelper.STATUS_OK != 
						printer.checkStatus(portName.toString())){
					// Reset portName to print on any printer
					portName = new StringBuilder("BT:");
				}
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, "**ERROR**", e);
		}

		boolean result = false;
		StringBuilder message = new StringBuilder();
		try {

			// Cheking status
			int status = printer.checkStatus(portName.toString());
			if(PrinterHelper.STATUS_OK == status){
				//				// Start connection
				//				printer.openConnection(portName.toString());
				//				// ONLINE Printer
				//				Bitmap bitmapEscudo = BitmapFactory.decodeResource(context.getResources(), R.drawable.escudo2);
				//				// Escudo
				//				printer.writeData(bitmapEscudo, 440);
				printer.openConnection(portName.toString());
				// Datos del Acta
				printer.writeData(data);
				message.append("Impresión Satisfactoria");
				result = true;
			}else{
				// OFFLINE Printer
				message.append("Impresora fuera de linea");
				if(status == PrinterHelper.STATUS_COVER_OPEN)
					message.append("\n- La tapa se encuentra abierta");
				if(status == PrinterHelper.STATUS_PAPER_EMPTY)
					message.append("\n- Papel agotado");
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			message.append("Se produjo un error durante la impresión: ");
			message.append(e.getMessage());
		} finally {
			printer.closeConnection();
		}

		if(result){
			Formulario formulario = (Formulario) form.getEntity();
			Integer cantImpresiones = formulario.getCantidadImpresiones();
			if(cantImpresiones == null) cantImpresiones = 0;
			cantImpresiones++;
			formulario.setCantidadImpresiones(cantImpresiones);
		}
		Toast.makeText(context, message.toString(),Toast.LENGTH_LONG).show();
		return true;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private Bitmap stampDataInBitmap(String data){
		Log.d(LOG_TAG, "stampDataInBitmap: enter");
		int width =  368;
		int height = 2000;

		Spanned spannedData = Html.fromHtml(data);
		Spannable span = new SpannableString(spannedData);
		span.setSpan(new AbsoluteSizeSpan(14), 0, spannedData.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		textView.setText(span);
		textView.setTextColor(Color.BLACK);
		textView.getPaint().setAntiAlias(true);
		textView.setBackgroundColor(Color.WHITE);
		textView.setTypeface(Typeface.MONOSPACE);
		textView.setDrawingCacheEnabled(true);

		Bitmap bitmap =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		textView.layout(0, 0, width, height);
		textView.draw(canvas);

		Log.d(LOG_TAG, "stampDataInBitmap: exit");
		return cropBorderFromBitmap(bitmap);
	}

	public Bitmap cropBorderFromBitmap(Bitmap bmp) {
		//Convenience variables
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		int[] pixels = new int[height * width];
		//Load the pixel data into the pixels array
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);

		int length = pixels.length;

		int borderColor = pixels[0];
		//Locate the end of the border
		int borderEnd = 0;
		for(int i = length - 1; i >= 0; i --) {
			if(pixels[i] != borderColor) {
				borderEnd = length - i;
				break;
			}
		}
		//Calculate the margins
		int bottomMargin = borderEnd / width;

		//Create the new, cropped version of the Bitmap
		return Bitmap.createBitmap(bmp, 0, 0, width , height - bottomMargin);
	}


	/**
	 * 
	 * @param tipoFormulario
	 * @return
	 */
	private Boolean generateNumero(Formulario formulario){
		Log.i(LOG_TAG, "generateNumero: enter");
		TalonarioDAO talonarioDAO = DAOFactory.getInstance().getTalonarioDAO();

		TipoFormulario tipoFormulario = formulario.getTipoFormulario();
		Integer dispTalonario = talonarioDAO.countOfActive().intValue();
		if(dispTalonario < 20){
			List<Talonario> listTalonario;
			WebServiceController webService = WebServiceController.getInstance(context);
			try {
				listTalonario = webService.syncListTalonario(tipoFormulario.getCodigo());
				for (Talonario talonario : listTalonario) {
					talonarioDAO.createOrUpdate(talonario);
				}
			} catch (Exception e) {
				Log.d(LOG_TAG, "**ERROR**", e);
			}
		}
		Talonario talonario = talonarioDAO.findBy(appState.getDispositivoMovil(), tipoFormulario);
		if(null == talonario) return false;

		DAOFactory.getInstance().getSerieDAO().refresh(talonario.getSerie());

		// Valor
		String valor = String.valueOf(talonario.getValor());

		formulario.setTalonario(talonario);

		// Numero: Serie-Valor-TipoFormulario
		String numero = String.format("%s-%s-%s", 
				tipoFormulario.getCodigo(),        //TipoActa
				Utils.completarCerosIzq(valor, 6), //Numero
				talonario.getSerie().getCodigo()); //Serie
		Log.d(LOG_TAG, " Numero: "+numero);
		formulario.setNumero(numero);

		// Actualizo talonario
		talonario.setEstado(0);
		talonarioDAO.update(talonario);
		Log.i(LOG_TAG, "generateNumero: exit");
		return true;
	}
	
	/**
	 * 
	 * Construye un dialog para consulta de licencias
	 * 
	 * @return 
	 */
	public Dialog dialogLicencia(){

		final Dialog dialog  = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.dialog_licencia);
		dialog.setTitle(context.getString(R.string.licencia_title));
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_login_mode);
		dialog.setCancelable(true);
		
		// Tipo Documento
		final Spinner spinnerTipoDocumento = (Spinner) dialog.findViewById(R.id.spinner_tipo_documento);
		List<TipoDocumento> listTipoDocumento = new ArrayList<TipoDocumento>();
		TipoDocumento sinDocumento = new TipoDocumento();
		sinDocumento.setId(-1);
		sinDocumento.setDescripcion("Seleccionar");
		listTipoDocumento.add(sinDocumento);
		listTipoDocumento.addAll(
				DAOFactory.getInstance().getTipoDocumentoDAO().findAllActive());
		ArrayAdapter<TipoDocumento> adapterTipoDocumento = 
				new ArrayAdapter<TipoDocumento>(context , 
						android.R.layout.simple_spinner_item, listTipoDocumento) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(context, android.R.layout.simple_spinner_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
			@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
		};
		adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTipoDocumento.setAdapter(adapterTipoDocumento);
		spinnerTipoDocumento.requestFocus();

		// Tipo Documento
		final Spinner spinnerClaseLicencia = (Spinner) dialog.findViewById(R.id.spinner_clase);
		List<ClaseLicencia> listClaseLicencia = new ArrayList<ClaseLicencia>();
		ClaseLicencia sinLicencia = new ClaseLicencia();
		sinLicencia.setId(-1);
		sinLicencia.setDescripcion("Seleccionar");
		listClaseLicencia.add(sinLicencia);
		listClaseLicencia.addAll(
				DAOFactory.getInstance().getClaseLicenciaDAO().findAllActive());
		
		ArrayAdapter<ClaseLicencia> adapterClaseLicencia = 
				new ArrayAdapter<ClaseLicencia>(context , 
						android.R.layout.simple_spinner_item, listClaseLicencia) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(context, android.R.layout.simple_spinner_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
			@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
		};
		adapterClaseLicencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerClaseLicencia.setAdapter(adapterClaseLicencia);
		
		// Numero Documento
		final EditText numeroEditText = (EditText) dialog.findViewById(R.id.numero_documento_edit_text);
		
		// Sexo
		final Spinner spinnerSexo = (Spinner) dialog.findViewById(R.id.spinner_sexo);
		
		// Cancel
		Button buttonCancel = (Button) dialog.findViewById(R.id.licencia_button_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// Confirm
		Button buttonConfirm = (Button) dialog.findViewById(R.id.licencia_button_confirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final TipoDocumento tipoDocumento = (TipoDocumento) spinnerTipoDocumento.getSelectedItem();
				final ClaseLicencia claseLicencia = (ClaseLicencia) spinnerClaseLicencia.getSelectedItem();
				final String sexo = (String) spinnerSexo.getSelectedItem();
				final String numeroDocumento = numeroEditText.getText().toString();
				AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
					private ProgressDialog dialog;
					private WebServiceController webService;
					@Override
					protected void onPreExecute() {
						this.dialog = ProgressDialog.show(context, "", context.getString(R.string.searching_msg));
						this.webService =  WebServiceController.getInstance(context);
					}
					
					@Override
					protected String doInBackground(Void... params) {
						try {
							return webService.estadoLicencia(tipoDocumento.getCodigo(), numeroDocumento, 
									String.valueOf(sexo.charAt(0)), claseLicencia.getDescripcion());
						} catch (Exception e) {
							Log.e(LOG_TAG, "**ERROR**", e);
							return "No pudo realizarse la consulta";
						}
					}
					
					@Override
					protected void onPostExecute(String result) {
						this.dialog.dismiss();
						//numeroEditText.setError(result);
						final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("Información");
						String msg = result.replace("\"", "").replace("\\n", "%n");
						alertDialog.setMessage(String.format(msg));
						alertDialog.setCancelable(false);
						alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
								"OK",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								alertDialog.dismiss();
							}});
						alertDialog.show();
					}
				};
				task.execute();
			}
		});
		ButtonEnabler.register(buttonConfirm, numeroEditText);

		return dialog;
	}

	/**
	 * 
	 */
	public void sendAlertaPanico(){
		//
		MediaPlayer mp = MediaPlayer.create(context, R.raw.alarm_beep_03);
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
		mp.setVolume(1f, 1f);
		mp.start();


		Location location = DeviceContext.getLastBestLocation(context);

		Double latitud  = 0D;
		Double longitud = 0D; 
		Long time       = 0L;
		String provider = "";
		Float precision = 0F;
		String paramKey = ParamHelper.SEND_LOCATION_NOT_FOUND_MESSAGE;
		String defaultMsg = "ALERTA\n" +
				 "NO SE PUDO OBTENER LA UBICACION\n" +
				 "${inspector}\n${fecha}-${hora}";
 
		
		if (location != null){
			latitud   = location.getLatitude();
			longitud  = location.getLongitude();
			time      = location.getTime();
			provider  = location.getProvider();
			precision = location.getAccuracy();
			paramKey  = ParamHelper.SEND_PANICO_MESSAGE;
			defaultMsg = "ALERTA\nhttp://infomapa.rosario.gov.ar/emapa/movil.html?"
					+ "latitud=${latitud}&longitud=${longitud}&nivelZoom=10\n${inspector}\n${fecha}-${hora}";
		}
		
		String paramMsg = ParamHelper.getString(paramKey, defaultMsg);
		
		String msg = paramMsg.replace("${latitud}", String.valueOf(latitud))
				.replace("${longitud}", String.valueOf(longitud))
				.replace("${inspector}", appState.getCurrentUser().getNombre())
				.replace("${fecha}",new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
				.replace("${hora}",new SimpleDateFormat("HH:mm").format(new Date())).replace("\\n", "%n");

		// -->> Send via SMS
		TelefonoPanicoDAO telefonoPanicoDAO = DAOFactory.getInstance().getTelefonoPanicoDAO();
		List<TelefonoPanico> listTelefonoPanico = telefonoPanicoDAO.findAllActive();
		for (TelefonoPanico telefonoPanico : listTelefonoPanico) {
			String phoneNumber = telefonoPanico.getNumero();
			SmsManager sms = SmsManager.getDefault(); 
			// Simple
			try{
				//sms.sendTextMessage(phoneNumber, null, String.format(msg), null, null);
				ArrayList<String> messages = sms.divideMessage(String.format(msg));
				sms.sendMultipartTextMessage(phoneNumber, null, messages, null, null);
			}catch(Exception e){
				Log.e(LOG_TAG, "**ERROR**", e);
			}
		}

		// -->> Send via WS
		Panico panico = new Panico();
		panico.setDispositivoMovil(appState.getDispositivoMovil());
		panico.setUsuarioPanico(appState.getCurrentUser());
		panico.setFechaPanico(new Date());

		panico.setFechaPosicion(new Date(time));
		panico.setLatitud(latitud);
		panico.setOrigen(provider);
		panico.setLongitud(longitud);
		panico.setPrecision(precision);

		PanicoDAO panicoDAO = DAOFactory.getInstance().getPanicoDAO();
		panicoDAO.create(panico);

		Intent intent = new Intent(context , SyncPanicoService.class);
		context.startService(intent);
	}
}