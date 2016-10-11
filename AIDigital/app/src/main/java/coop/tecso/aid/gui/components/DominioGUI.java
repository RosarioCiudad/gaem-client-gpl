package coop.tecso.aid.gui.components;

import java.util.List;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;

/**
 * 
 * @author tecso.coop
 *
 */
public class DominioGUI extends CampoGUI {

	private static final String LOG_TAG = DominioGUI.class.getSimpleName();

	private EditText editTextDominio;
	private EditText editTextCopia;
	private CheckBox checkBox;

	// Constructs
	public DominioGUI(Context context) {
		super(context);
		if(editTextDominio != null){
			editTextDominio.setTextAppearance(context, R.style.UiEditTex);
			editTextDominio.setBackgroundResource(android.R.drawable.editbox_background);
		}
	}

	public DominioGUI(Context context,List<Value> values) {
		super(context,values);
		if(editTextDominio != null){
			editTextDominio.setTextAppearance(context, R.style.UiEditTex);
			editTextDominio.setBackgroundResource(android.R.drawable.editbox_background);
		}
	}

	public DominioGUI(Context context, boolean enabled) {
		super(context, enabled);
		if(editTextDominio != null){
			editTextDominio.setTextAppearance(context, R.style.UiEditTex);
			editTextDominio.setBackgroundResource(android.R.drawable.editbox_background);
		}
	}

	@Override
	public View build() {
		// Dominio
		TextView labelDominio = new TextView(context);
		labelDominio.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelDominio.setText("Dominio: ");

		editTextDominio = new EditText(context);
		editTextDominio.setTextAppearance(context, R.style.UiEditTex);
		editTextDominio.setBackgroundResource(android.R.drawable.editbox_background);
		editTextDominio.setFocusable(enabled);
		editTextDominio.setEnabled(enabled);
		editTextDominio.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		editTextDominio.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15) });
		editTextDominio.requestFocus();

		// Copia
		TextView labelCopia = new TextView(context);
		labelCopia.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelCopia.setText("Copia: ");

		editTextCopia = new EditText(context);
		editTextCopia.setTextAppearance(context, R.style.UiEditTex);
		editTextCopia.setBackgroundResource(android.R.drawable.editbox_background);
		editTextCopia.setFocusable(enabled);
		editTextCopia.setEnabled(enabled);
		editTextCopia.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		editTextCopia.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

		// Sin Identificar
		TextView labelSinIdentificar = new TextView(context);
		labelSinIdentificar.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelSinIdentificar.setText("Sin Identificar: ");

		checkBox = new CheckBox(context);
		checkBox.setFocusable(enabled);
		checkBox.setEnabled(enabled);
		checkBox.setButtonDrawable(R.drawable.checkbox_selector);
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(checkBox.isChecked()){
					editTextDominio.setText("");
					editTextDominio.setEnabled(false);
					editTextCopia.setText("");
					editTextCopia.setEnabled(false);
				}else{
					editTextDominio.setEnabled(true);
					editTextCopia.setEnabled(true);
				}
			}
		});

		// Dominio
		if(getInitialValues() != null && getInitialValues().size() >= 1){
			//
			String value = getInitialValues().get(0).getValor();
			String[] values = value.split("\\|");
			//
			editTextDominio.setText(values[0]);
			editTextCopia.setText(values[1]);
			checkBox.setChecked(Boolean.valueOf(values[2]));
		}

		// Container: Dominio | Copia | SinIdentificar | forzarDominio
		LinearLayout layoutContainer = new LinearLayout(context);
		layoutContainer.setOrientation(LinearLayout.HORIZONTAL);
		layoutContainer.setWeightSum(100f);

		LinearLayout layoutDominio = buildLayout(labelDominio);
		layoutDominio.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutDominio.addView(editTextDominio);

		LinearLayout layoutCopia = buildLayout(labelCopia);
		layoutCopia.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutCopia.addView(editTextCopia);

		LinearLayout layoutSinIdentificar = buildLayout(labelSinIdentificar);
		layoutSinIdentificar.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutSinIdentificar.setGravity(Gravity.RIGHT);
		layoutSinIdentificar.addView(checkBox);
		
		// Container Params
		LinearLayout.LayoutParams param;
		// Dominio
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 40f;
		layoutContainer.addView(layoutDominio, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f;
		layoutContainer.addView(new LinearLayout(context), param);
		// Copia
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 25f;
		param.gravity = Gravity.CENTER_HORIZONTAL;
		layoutContainer.addView(layoutCopia, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f; 
		layoutContainer.addView(new LinearLayout(context), param);
		// Sin Identificar
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 25f;
		layoutContainer.addView(layoutSinIdentificar, param);
		
		// Build Section
		TableLayout mainLayout = new TableLayout(context);

		// Altura | Bis | LetraCalle | Boton
		mainLayout.addView(layoutContainer);

		Log.i(LOG_TAG, "buildSeccionDomicilio: exit");
		this.view = mainLayout;

		return this.view;
	}


	@Override
	public String getPrinterValor() {
		//
		String value = getValorView();
		return format(value);
	}

	/**
	 * Format 
	 * dominio|copia|sinIdentificar 
	 * @param value
	 * @return
	 */
	public static String format(String value){
		String[] values = value.split("\\|");
		// Dominio sin Identificar
		if(Boolean.valueOf(values[2])){
			return "No se puede identificar el dominio";
		}
		StringBuilder builder = new StringBuilder();
		// Dominio
		String dominio = values[0];
		dominio = agregarEspacio(dominio);
		if(!TextUtils.isEmpty(dominio)){
			builder.append("Dominio: "+dominio);
		}
		// Copia
		String copia = values[1];
		copia = agregarEspacio(copia);
		if(!TextUtils.isEmpty(copia)){
			builder.append(" Copia: "+copia);
		}
		return builder.toString();
	}


	@Override
	public boolean isDirty(){
		boolean dirty = false;
		// Current Value
		String currentValue = getValorView();
		if(getInitialValues() != null && getInitialValues().size() == 1){
			// Initial Value
			String initialValue = getInitialValues().get(0).getValor();
			if(!currentValue.equals(initialValue)){
				dirty = true;
			}
		}else if(!TextUtils.isEmpty(currentValue)){
			dirty = true;
		}
		if(dirty){
			Log.d(LOG_TAG, String.format("%s dirty=true", getEtiqueta()));
		}
		return dirty;
	}

	@Override
	public boolean validate() {
		if(isObligatorio() == false){
			return true;
		}

		// Dominio Sin Identificar
		if(checkBox.isChecked()){
			return true;
		}

		editTextDominio.setError(null);
		editTextCopia.setError(null);

		// Dominio
		String dominio = editTextDominio.getText().toString();
		
		if(TextUtils.isEmpty(dominio)){
			// Dominio Vacio
			editTextDominio.setError(context.getString(R.string.field_required, "Dominio"));
			editTextDominio.requestFocus();
			return false;
		}
		if(!checkDominio(dominio)){
			// Dominio invalido
			editTextDominio.setError(context.getString(R.string.field_invalid, "Dominio"));
			editTextDominio.requestFocus();
			return false;
		}

		// Copia
		String copia = editTextCopia.getText().toString();
		if(!TextUtils.isEmpty(copia) && !checkDominioCopia(copia)){
			// Copia invalido
			editTextCopia.setSingleLine(true);
			editTextCopia.setError("Inválido");
			editTextCopia.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @return
	 */
	private LinearLayout buildLayout(TextView label){
		LinearLayout linearLayout;
		//		if(appState.getOrientation() == LinearLayout.VERTICAL){
		// Se define un LinearLayout para ubicar: 'Label / EditText'
		linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		//		}else{
		//			//Se define un Table Row para ubicar: 'Label | EditText'
		//			linearLayout = new TableRow(context);
		//			label.setGravity(Gravity.RIGHT);
		//			linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		//		}
		linearLayout.addView(label);
		return linearLayout;
	}

	/**
	 * dominio|copia|sinIdentificar
	 * 
	 * @return
	 */
	public String getValorView() {
		StringBuilder builder = new StringBuilder();
		//builder.append(editTextDominio.getText().toString());
		//si se ingresó sin espacio, se lo agregamos
		builder.append(agregarEspacio(editTextDominio.getText().toString()));
		builder.append("|");
		builder.append(editTextCopia.getText().toString());
		builder.append("|");
		builder.append(checkBox.isChecked());

		return  builder.toString();
	}

	/**
	 * Patrones de Dominios validos Patentes Argentinas viejas
	 */
	private String[] dominioPatternsViejos = {
			"^\\s[A-Z]{3}[0-9]{4}",
			"^[A-Z]{3}\\s[0-9]{4}",
			"^[A-Z]{3}[0-9]{4}",
			"^[A-Z]{3}\\s[0-9]{3}\\s",
			"^[A-Z]{3}\\s[0-9]{3}",
			"^[A-Z]{3}[0-9]{3}\\s",
			"^[A-Z]{3}[0-9]{3}",
			"^[A-Z]{2}\\s[0-9]{4}\\s",
			"^[A-Z]{2}\\s[0-9]{4}",
			"^[A-Z]{2}[0-9]{4}",
			"^[A-Z]{2}[0-9]{4}\\s",
			"^[A-Z]{1}\\s[0-9]{3}[A-Z]{3}",
			"^[A-Z]{1}[0-9]{3}[A-Z]{3}",
			"^[0-9]{3}\\s[A-Z]{3}",
			"^[0-9]{3}\\s[A-Z]{3}\\s",
			"^S\\s[A-Z]{3}[0-9]{3}",
			"^[B-C][1-3][0-9]{6}",
			"^[A-Z]{1}\\s[0-9]{6}"
	};

	/**
	 * Patrones de Dominios validos Patentes Mercosur Autos
	 */
	private String[] dominiosPatentesMercosurAutos = {
			"^[a-zA-Z][a-zA-Z][0-9][0-9][0-9][a-zA-Z][a-zA-Z]", 
			"^[a-zA-Z][a-zA-Z][0-9][0-9][0-9][a-zA-Z][a-zA-Z]\\s"
	};

	/**
	 * Patrones de Dominios validos Patentes Mercosur Motos
	 */
	private String[] dominiosPatentesMercosurMotos = {
			"^[a-zA-Z][0-9][0-9][0-9][a-zA-Z][a-zA-Z][a-zA-Z]",
			"^[a-zA-Z][0-9][0-9][0-9][a-zA-Z][a-zA-Z][a-zA-Z]\\s"
	}; 

	/**
	 * Patrones de Copia validos
	 */
	private String[] dominioCopiaPatterns = {"D","T","C","Q","S","7","8","9"};

	/**
	 * 
	 * @param dominio
	 * @return
	 */
	private boolean checkDominio(String dominio) {
		for (String p : dominioPatternsViejos) {
			if (dominio.matches(p))	return true;
		}
		for (String p : dominiosPatentesMercosurAutos) {
			if (dominio.matches(p))	return true;
		}
		for (String p : dominiosPatentesMercosurMotos) {
			if (dominio.matches(p))	return true;
		}
		return false;
	}

	/**
	 * 
	 * @param dominioCopia
	 * @return
	 */
	private boolean checkDominioCopia(String dominioCopia) {
		for (String p : dominioCopiaPatterns) {
			if (dominioCopia.equals(p))	return true;
		}
		return false;
	}

	public static String agregarEspacio(String dominio) {
		if(!dominio.trim().matches("^[A-Z]{3}[0-9]{3}"))
			return dominio;
		else {
			return dominio.substring(0,3)+" "+dominio.substring(3,dominio.length());
		}
	}
}