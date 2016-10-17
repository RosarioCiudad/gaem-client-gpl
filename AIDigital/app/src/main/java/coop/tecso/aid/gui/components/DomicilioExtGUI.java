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

package coop.tecso.aid.gui.components;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.helpers.SQLHelper;

/**
 * 
 * @author tecso.coop
 *
 */
public class DomicilioExtGUI extends CampoGUI {

	private static final String LOG_TAG = DomicilioExtGUI.class.getSimpleName();

	private AutoCompleteTextView searchBoxCalle;
	private AutoCompleteTextView searchBoxPais;
	private AutoCompleteTextView searchBoxLocalidad;
	private EditText editTextAltura;

	private DomicilioDTO domicilioDTO;

	// Constructs
	public DomicilioExtGUI(Context context) {
		super(context);
	}

	public DomicilioExtGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public DomicilioExtGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	@Override
	public View build() {
		// Domicilio
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			//
			String strDomicilio = this.getInitialValues().get(0).getValor();
			Log.d("LOCALIDAD","LOCALIDAD: seteo el domicilioDTO con el if");
			domicilioDTO = parseDomicilio(strDomicilio);
		}else{
			Log.d("LOCALIDAD","LOCALIDAD: seteo el domicilioDTO con el else");
			domicilioDTO = parseDomicilio(this.getValorDefault());
		}

		//Pais
		TextView labelPais = new TextView(context);
		labelPais.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelPais.setText("País: ");

		LinearLayout layoutPais = buildLayout(labelPais);
		PaisAutoTextAdapter paisAdapter = this.new PaisAutoTextAdapter(context);
		searchBoxPais = new AutoCompleteTextView(context);
		searchBoxPais.setTextAppearance(context, R.style.UiEditTex);
		searchBoxPais.setBackgroundResource(android.R.drawable.editbox_background);
		searchBoxPais.setAdapter(paisAdapter);
		searchBoxPais.setOnItemClickListener(paisAdapter);
		searchBoxPais.setEnabled(enabled);
		searchBoxPais.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		searchBoxPais.setThreshold(2);
		searchBoxPais.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.pais = s.toString();
			}
		});
		searchBoxPais.setText(domicilioDTO.pais);
		layoutPais.addView(searchBoxPais);

		//Localidad
		TextView labelLocalidad = new TextView(context);
		labelLocalidad.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelLocalidad.setText("Localidad + Provincia: ");

		LinearLayout layoutLocalidad = buildLayout(labelLocalidad);
		LocalidadAutoTextAdapter localidadAdapter = this.new LocalidadAutoTextAdapter(context);
		searchBoxLocalidad = new AutoCompleteTextView(context);
		searchBoxLocalidad.setTextAppearance(context, R.style.UiEditTex);
		searchBoxLocalidad.setBackgroundResource(android.R.drawable.editbox_background);
		searchBoxLocalidad.setAdapter(localidadAdapter);
		searchBoxLocalidad.setOnItemClickListener(localidadAdapter);
		searchBoxLocalidad.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		searchBoxLocalidad.setEnabled(enabled);
		searchBoxLocalidad.setFocusable(enabled);
		searchBoxLocalidad.setThreshold(2);
		searchBoxLocalidad.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.localidad = s.toString();
			}
		});
		searchBoxLocalidad.setText(domicilioDTO.localidad);
		layoutLocalidad.addView(searchBoxLocalidad);

		// Calle
		TextView labelCalle = new TextView(context);
		labelCalle.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelCalle.setText("Calle: ");

		LinearLayout layoutCalle = buildLayout(labelCalle);
		searchBoxCalle = new AutoCompleteTextView(context);

		// Create an ItemAutoTextAdapter for the State Name field,
		// and set it as the OnItemClickListener for that field.
		CalleAutoTextAdapter adapter = new CalleAutoTextAdapter(context);
		searchBoxCalle.setTextAppearance(context, R.style.UiEditTex);
		searchBoxCalle.setBackgroundResource(android.R.drawable.editbox_background);
		searchBoxCalle.setAdapter(adapter);
		searchBoxCalle.setOnItemClickListener(adapter);
		searchBoxCalle.setEnabled(enabled);
		searchBoxCalle.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		searchBoxCalle.setThreshold(2);
		searchBoxCalle.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.calle = s.toString();
			}
		});
		searchBoxCalle.setText(domicilioDTO.calle);

		layoutCalle.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutCalle.addView(searchBoxCalle);

		final LinearLayout layoutPaisContainer = new LinearLayout(context);
		layoutPaisContainer.setOrientation(LinearLayout.VERTICAL);
		layoutPaisContainer.addView(layoutPais);
		View sep = new View(context);
		sep.setBackgroundColor(context.getResources().getColor(R.color.label_text_color));
		layoutPaisContainer.addView(sep, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
		layoutPaisContainer.addView(layoutLocalidad);
		sep = new View(context);
		sep.setBackgroundColor(context.getResources().getColor(R.color.label_text_color));
		layoutPaisContainer.addView(sep, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
		layoutPaisContainer.setVisibility(domicilioDTO.esRosario? View.GONE:View.VISIBLE);

		// Es Rosario?
		TextView labelRosario = new TextView(context);
		labelRosario.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelRosario.setText("Rosario: ");
		final CheckBox checkBoxRosario = new CheckBox(context);
		checkBoxRosario.setFocusable(enabled);
		checkBoxRosario.setEnabled(enabled);
		checkBoxRosario.setChecked(domicilioDTO.esRosario);
		checkBoxRosario.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		checkBoxRosario.setButtonDrawable(R.drawable.checkbox_selector);
		checkBoxRosario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(checkBoxRosario.isChecked()){
					domicilioDTO.esRosario = true;
					domicilioDTO.codPais = 1;
					domicilioDTO.codPostal = 2000;
					domicilioDTO.subPostal = 8;
					domicilioDTO.localidad = "ROSARIO";
					searchBoxCalle.setThreshold(2);
					searchBoxPais.setThreshold(2);
					searchBoxLocalidad.setThreshold(2);
					layoutPaisContainer.setVisibility(View.GONE);
				}else{
					domicilioDTO.esRosario = false;
					searchBoxLocalidad.setText("");
					searchBoxCalle.setText("");
					searchBoxCalle.setThreshold(100);
					domicilioDTO.codPais = 1;
					domicilioDTO.codPostal = 0;
					domicilioDTO.subPostal = 0;
					layoutPaisContainer.setVisibility(View.VISIBLE);
				}
			}
		});

		LinearLayout layoutRosario = buildLayout(labelRosario);
		layoutRosario.addView(checkBoxRosario);

		//
		LinearLayout layoutDireccion = new LinearLayout(context);
		layoutDireccion.setOrientation(LinearLayout.HORIZONTAL);
		layoutDireccion.setWeightSum(100f);
		LinearLayout.LayoutParams paramDir = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT); 
		paramDir.weight = 85f;
		layoutDireccion.addView(layoutCalle , paramDir);
		paramDir = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT); 
		paramDir.weight = 15f;
		layoutDireccion.addView(layoutRosario , paramDir);

		// Altura
		TextView labelAltura = new TextView(context);
		labelAltura.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelAltura.setText("Altura: ");

		editTextAltura = new EditText(context);
		editTextAltura.setTextAppearance(context, R.style.UiEditTex);
		editTextAltura.setBackgroundResource(android.R.drawable.editbox_background);
		editTextAltura.setInputType(InputType.TYPE_CLASS_NUMBER);
		editTextAltura.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// Watcher usado para setear la altura
		TextWatcher textWatcherAltura = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.altura = s.toString();
			}
		};
		editTextAltura.setText(domicilioDTO.altura);
		editTextAltura.addTextChangedListener(textWatcherAltura);
		editTextAltura.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		editTextAltura.setEnabled(enabled);
		editTextAltura.setFocusable(enabled);

		// Bis
		TextView labelBis = new TextView(context);
		labelBis.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelBis.setText("Bis: ");

		final CheckBox checkBox = new CheckBox(context);
		checkBox.setFocusable(enabled);
		checkBox.setEnabled(enabled);
		checkBox.setChecked(domicilioDTO.bis);
		checkBox.setButtonDrawable(R.drawable.checkbox_selector);
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				domicilioDTO.bis = checkBox.isChecked();
			}
		});

		// Letra
		TextView labelLetra = new TextView(context);
		labelLetra.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelLetra.setText("Letra: ");
		TextWatcher textWatcherLetra = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.letra = s.toString();
			}
		};
		EditText editTextLetra = new EditText(context);
		editTextLetra.setTextAppearance(context, R.style.UiEditTex);
		editTextLetra.setBackgroundResource(android.R.drawable.editbox_background);
		editTextLetra.setFocusable(enabled);
		editTextLetra.setEnabled(enabled);
		editTextLetra.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		editTextLetra.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(1) });
		editTextLetra.setText(domicilioDTO.letra);
		editTextLetra.addTextChangedListener(textWatcherLetra);

		// Piso
		TextView labelPiso = new TextView(context);
		labelPiso.setTextAppearance(context, R.style.UiTextViewLabelDomicilio);
		labelPiso.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelPiso.setText("Piso: ");
		EditText editTextPiso = new EditText(context);
		editTextPiso.setFocusable(enabled);
		editTextPiso.setEnabled(enabled);
		editTextPiso.setInputType(InputType.TYPE_CLASS_NUMBER);
		editTextPiso.setTextAppearance(context, R.style.UiEditTex);
		editTextPiso.setBackgroundResource(android.R.drawable.editbox_background);
		editTextPiso.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(2) });
		editTextPiso.setText(domicilioDTO.piso);
		editTextPiso.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.piso = s.toString();
			}
		});

		// Dpto
		TextView labelDpto = new TextView(context);
		labelDpto.setTextAppearance(context, R.style.UiTextViewLabelDomicilio);
		//labelDpto.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelDpto.setText("Dpto: ");
		EditText editTextDpto = new EditText(context);
		editTextDpto.setTextAppearance(context, R.style.UiEditTex);
		editTextDpto.setBackgroundResource(android.R.drawable.editbox_background);
		editTextDpto.setFocusable(enabled);
		editTextDpto.setEnabled(enabled);
		editTextDpto.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(3) });
		editTextDpto.setInputType(InputType.TYPE_CLASS_NUMBER);
		editTextDpto.setText(domicilioDTO.dpto);
		editTextDpto.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.dpto = s.toString();
			}
		});

		// MBlk
		TextView labelMBlk = new TextView(context);
		labelMBlk.setTextAppearance(context, R.style.UiTextViewLabelDomicilio);
		//labelMBlk.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelMBlk.setText("MBlock: ");
		EditText editTextMBlk = new EditText(context);
		editTextMBlk.setTextAppearance(context, R.style.UiEditTex);
		editTextMBlk.setBackgroundResource(android.R.drawable.editbox_background);
		editTextMBlk.setFocusable(enabled);
		editTextMBlk.setEnabled(enabled);
		editTextMBlk.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(4) });
		editTextMBlk.setText(domicilioDTO.mblk);
		editTextMBlk.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				domicilioDTO.mblk = s.toString();
			}
		});

		// Container: Altura | Bis | LetraCalle | Piso | Dpto | MBlk
		LinearLayout layoutContainer = new LinearLayout(context);
		layoutContainer.setOrientation(LinearLayout.HORIZONTAL);
		layoutContainer.setWeightSum(100f);

		LinearLayout layoutAltura = buildLayout(labelAltura);
		layoutAltura.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutAltura.addView(editTextAltura);

		LinearLayout layoutBis = buildLayout(labelBis);
		layoutBis.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutBis.addView(checkBox);

		LinearLayout layoutLetra = buildLayout(labelLetra);
		layoutLetra.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutLetra.addView(editTextLetra);

		LinearLayout layoutPiso = buildLayout(labelPiso);
		layoutPiso.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutPiso.addView(editTextPiso);

		LinearLayout layoutDpto = buildLayout(labelDpto);
		layoutDpto.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutDpto.addView(editTextDpto);

		LinearLayout layoutMBlk = buildLayout(labelMBlk);
		layoutMBlk.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutMBlk.addView(editTextMBlk);

		// Container Params
		LinearLayout.LayoutParams param;
		// Altura
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT); //20 + 20 +10
		param.weight = 20f;
		layoutContainer.addView(layoutAltura, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f;
		layoutContainer.addView(new LinearLayout(context), param);
		// Bis
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 10f;
		param.gravity = Gravity.CENTER_HORIZONTAL;
		layoutContainer.addView(layoutBis, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f; 
		layoutContainer.addView(new LinearLayout(context), param);
		// Letra
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 10f;
		layoutContainer.addView(layoutLetra, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f; 
		layoutContainer.addView(new LinearLayout(context), param);
		// Piso
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 15f;
		layoutContainer.addView(layoutPiso, param); 
		// Dpto
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 15f;
		layoutContainer.addView(layoutDpto, param);
		// MBlk
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 15f;
		layoutContainer.addView(layoutMBlk, param);

		// Build Section
		TableLayout mainLayout = new TableLayout(context);

		// Pais + Localidad
		mainLayout.addView(layoutPaisContainer);
		// Direccion
		mainLayout.addView(layoutDireccion);
		View separator = new View(context);
		separator.setBackgroundColor(context.getResources().getColor(R.color.label_text_color));
		mainLayout.addView(separator, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
		// Container: Altura | Bis | LetraCalle | Piso | Dpto | MBlk
		mainLayout.addView(layoutContainer);

		Log.i(LOG_TAG, "buildSeccionDomicilio: exit");
		this.view = mainLayout;

		return this.view;
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
				Log.d(LOG_TAG, String.format("1-TRUE: CV: %s != IV: %s ", currentValue, initialValue));
				dirty = true;
			}
		}else if(!TextUtils.isEmpty(currentValue)){
			Log.d(LOG_TAG, String.format("2-TRUE: CV: %s",currentValue));
			dirty = true;
		}
		return dirty;
	}

	@Override
	public boolean validate() {
		if(isObligatorio() == false){
			return true;
		}
		// Limpio errores previos
		searchBoxPais.setError(null);
		searchBoxLocalidad.setError(null);
		searchBoxCalle.setError(null);
		editTextAltura.setError(null);

		// Rosario
		if(domicilioDTO.esRosario){
			// Calle
			if(domicilioDTO.codCalle == 0){
				searchBoxCalle.setError(context.getString(R.string.field_required, "Calle"));
				searchBoxCalle.requestFocus();
				return false;
			}

			// Altura 
			if(TextUtils.isEmpty(domicilioDTO.altura)){
				editTextAltura.setError(context.getString(R.string.field_required, "Altura"));
				editTextAltura.requestFocus();
				return false;
			}

			// Valido altura
			if(validateDomicilio() == false){
				//Domicilio no geocodificado
				searchBoxCalle.setError("El Domicilio ingresado es inválido");
				searchBoxCalle.requestFocus();
				return false;
			}
		}

		// Domicilio fuera de Rosario
		if(!domicilioDTO.esRosario){
			// Valida Pais
			if(domicilioDTO.codPais == 0){
				searchBoxPais.setError(context.getString(R.string.field_required, "País"));
				searchBoxPais.requestFocus();
				return false;
			}

			// Valida Localidad + Provincia en Argentina
			if(domicilioDTO.codPais == 1 && domicilioDTO.codPostal == 0){
				searchBoxLocalidad.setError("La Localidad + Provincia ingresada es inválida");
				searchBoxLocalidad.requestFocus();
				return false;
			}

			// Valida Localidad + Provincia
			if(TextUtils.isEmpty(searchBoxLocalidad.getText())){
				searchBoxLocalidad.setError(context.getString(R.string.field_required, "Localidad + Provincia"));
				searchBoxLocalidad.requestFocus();
				return false;
			}

			// Calle
			if(TextUtils.isEmpty(searchBoxCalle.getText())){
				searchBoxCalle.setError(context.getString(R.string.field_required, "Calle"));
				searchBoxCalle.requestFocus();
				return false;
			}

			// Altura 
			if(TextUtils.isEmpty(domicilioDTO.altura)){
				editTextAltura.setError(context.getString(R.string.field_required, "Altura"));
				editTextAltura.requestFocus();
				return false;
			}
		}

		return true;
	}


	@Override
	public View getEditViewForCombo(){
		return this.view;
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
	 * codCalle|calle|altura|bis|letra|mblk|piso|dpto|codPostal|subPostal|localidad|codPais|pais|esRosario
	 * 
	 * @return
	 */
	public String getValorView() {
		StringBuilder builder = new StringBuilder();
		builder.append(domicilioDTO.codCalle);
		builder.append("|");
		builder.append(domicilioDTO.calle);
		builder.append("|");
		builder.append(domicilioDTO.altura);
		builder.append("|");
		builder.append(domicilioDTO.bis);
		builder.append("|");
		builder.append(domicilioDTO.letra);
		builder.append("|");
		builder.append(domicilioDTO.mblk);
		builder.append("|");
		builder.append(domicilioDTO.piso);
		builder.append("|");
		builder.append(domicilioDTO.dpto);
		builder.append("|");
		builder.append(domicilioDTO.codPostal);
		builder.append("|");
		builder.append(domicilioDTO.subPostal);
		builder.append("|");
		builder.append(domicilioDTO.localidad);
		builder.append("|");
		builder.append(domicilioDTO.codPais);
		builder.append("|");
		builder.append(domicilioDTO.pais);
		builder.append("|");
		builder.append(domicilioDTO.esRosario);
		return  builder.toString();
	}


	/**
	 * codCalle|calle|altura|bis|letra|mblk|piso|dpto|codPostal|subPostal|localidad|codPais|pais|esRosario
	 * 
	 * @param strToParse
	 * @return
	 */
	private DomicilioDTO parseDomicilio(String strToParse) {
		Log.i(LOG_TAG, "parseDomicilio: enter");

		DomicilioDTO domicilioDTO = new DomicilioDTO();
		if(!TextUtils.isEmpty(strToParse)){
			String[] domicilio = strToParse.split("\\|");
			//
			domicilioDTO.codCalle =  Integer.valueOf(domicilio[0]).intValue();
			domicilioDTO.calle =  domicilio[1];
			domicilioDTO.altura = domicilio[2];
			domicilioDTO.bis =  Boolean.valueOf(domicilio[3]);
			domicilioDTO.letra = domicilio[4];
			domicilioDTO.mblk =  domicilio[5];
			domicilioDTO.piso =  domicilio[6];
			domicilioDTO.dpto =  domicilio[7];
			domicilioDTO.codPostal = Integer.valueOf(domicilio[8]).intValue();
			domicilioDTO.subPostal = Integer.valueOf(domicilio[9]).intValue();
			domicilioDTO.localidad = domicilio[10];
			domicilioDTO.codPais = Integer.valueOf(domicilio[11]).intValue();
			domicilioDTO.pais = domicilio[12];
			domicilioDTO.esRosario =  Boolean.valueOf(domicilio[13]);
		}

		Log.i(LOG_TAG, "parseDomicilio: exit");
		return domicilioDTO;
	}

	/**
	 * 
	 */
	private class DomicilioDTO {
		private String calle = "";
		private String altura = "";
		private String piso = "";
		private String dpto = "";
		private String mblk = "";
		private String pais = "ARGENTINA";
		private String localidad = "ROSARIO";
		private String letra = "";

		private int codCalle;
		private int codPais = 1;
		private int codPostal = 2000;
		private int subPostal = 8;
		private boolean bis;
		private boolean esRosario = true;
	}


	/**
	 * Specializes CursorAdapter to supply choices to a AutoCompleteTextView.
	 * Also implements OnItemClickListener to be notified when a choice is made,
	 * and uses the choice to update other fields on the Activity form.
	 * 
	 * @author tecso.coop
	 * 
	 */
	private class CalleAutoTextAdapter extends CursorAdapter implements OnItemClickListener {

		private SQLHelper sqlHelper;

		public CalleAutoTextAdapter(Context context) {
			// Call the CursorAdapter constructor with a null Cursor.
			super(context, null);
			// Open Database Connection
			this.sqlHelper = new SQLHelper(context);
			this.sqlHelper.openReadableDatabase();
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}

			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(" SELECT codigo AS _id, descripcion ");
			queryBuilder.append(" FROM gis_calle ");
			queryBuilder.append(" WHERE 1=1 ");

			Cursor cursor;
			if (!TextUtils.isEmpty(constraint)) {
				// Query for any rows where the state name begins with the
				// string specified in constraint.
				constraint = String.format("%%%s%%", constraint.toString().trim());
				queryBuilder.append(" AND descripcion COLLATE NOCASE LIKE  ? ORDER BY 2");
				//
				cursor = sqlHelper.rawQuery(queryBuilder.toString(), constraint.toString());
			}else{
				//
				cursor = sqlHelper.rawQuery(queryBuilder.toString());
			}
			cursor.moveToFirst();

			((Activity) context).startManagingCursor(cursor);
			return cursor;
		}

		@Override
		public String convertToString(Cursor cursor) {
			final String str = cursor.getString(1);
			return str;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final String text = convertToString(cursor);
			((TextView) view).setText(text);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final View view =
					inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

			return view;
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			// Get the cursor, positioned to the corresponding row in the result set
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			// Calle
			domicilioDTO.codCalle =  cursor.getInt(0);
		}
	}

	/**
	 * Specializes CursorAdapter to supply choices to a AutoCompleteTextView.
	 * Also implements OnItemClickListener to be notified when a choice is made,
	 * and uses the choice to update other fields on the Activity form.
	 * 
	 * @author tecso.coop
	 * 
	 */
	private class PaisAutoTextAdapter extends CursorAdapter implements OnItemClickListener {

		private SQLHelper sqlHelper;
		public PaisAutoTextAdapter(Context context) {
			// Call the CursorAdapter constructor with a null Cursor.
			super(context, null);
			// Open Database Connection
			this.sqlHelper = new SQLHelper(context);
			this.sqlHelper.openReadableDatabase();
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}

			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(" SELECT codigo AS _id, descripcion ");
			queryBuilder.append(" FROM gis_pais ");
			queryBuilder.append(" WHERE 1=1 ");

			Cursor cursor;
			if (!TextUtils.isEmpty(constraint)) {
				// Query for any rows where the state name begins with the
				// string specified in constraint.
				constraint = String.format("%%%s%%", constraint.toString().trim());
				queryBuilder.append(" AND descripcion COLLATE NOCASE LIKE  ?");
				cursor = sqlHelper.rawQuery(queryBuilder.toString(), constraint.toString());
			}else{
				//
				cursor = sqlHelper.rawQuery(queryBuilder.toString());
			}
			cursor.moveToFirst();
			((Activity) context).startManagingCursor(cursor);
			return cursor;
		}

		@Override
		public String convertToString(Cursor cursor) {
			return cursor.getString(1);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final String text = convertToString(cursor);
			((TextView) view).setText(text);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			return inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			// Interseccion
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			domicilioDTO.codPais = cursor.getInt(0);
			domicilioDTO.pais = cursor.getString(1);
			// Pais es Argentina
			if(domicilioDTO.codPais == 1){
				searchBoxLocalidad.setThreshold(2);
			}else{
				domicilioDTO.codPostal = 0;
				domicilioDTO.codCalle = 0;
				domicilioDTO.subPostal = 0;
				searchBoxLocalidad.setText("");
				searchBoxLocalidad.setThreshold(200);
			}
		}
	}

	/**
	 * Specializes CursorAdapter to supply choices to a AutoCompleteTextView.
	 * Also implements OnItemClickListener to be notified when a choice is made,
	 * and uses the choice to update other fields on the Activity form.
	 * 
	 * @author tecso.coop
	 * 
	 */
	private class LocalidadAutoTextAdapter extends CursorAdapter implements OnItemClickListener {

		private SQLHelper sqlHelper;
		public LocalidadAutoTextAdapter(Context context) {
			// Call the CursorAdapter constructor with a null Cursor.
			super(context, null);
			// Open Database Connection
			this.sqlHelper = new SQLHelper(context);
			this.sqlHelper.openReadableDatabase();
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}

			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(" SELECT codigopostal AS _id, codigosubpostal, ");
			queryBuilder.append(" descripcion ||' ('||provincia||')' ");
			queryBuilder.append(" FROM gis_localidad ");
			queryBuilder.append(" WHERE 1=1 ");

			Cursor cursor;
			if (!TextUtils.isEmpty(constraint)) {
				// Query for any rows where the state name begins with the
				// string specified in constraint.
				constraint = String.format("%%%s%%", constraint.toString().trim());
				queryBuilder.append(" AND descripcion COLLATE NOCASE LIKE  ?");
				cursor = sqlHelper.rawQuery(queryBuilder.toString(), constraint.toString());
			}else{
				//
				cursor = sqlHelper.rawQuery(queryBuilder.toString());
			}
			cursor.moveToFirst();
			((Activity) context).startManagingCursor(cursor);
			return cursor;
		}

		@Override
		public String convertToString(Cursor cursor) {
			return cursor.getString(2);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final String text = convertToString(cursor);
			((TextView) view).setText(text);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			return inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			// Interseccion
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			domicilioDTO.codPostal = cursor.getInt(0);
			domicilioDTO.subPostal = cursor.getInt(1);
			Log.d("LOCALIDAD","LOCALIDAD: seteo la localidad a "+cursor.getString(2));
			domicilioDTO.localidad = cursor.getString(2);
		}
	}


	/**
	 * 
	 * @return
	 */
	private boolean validateDomicilio(){
		//Parameterized query
		StringBuilder query = new StringBuilder();
		query.append(" SELECT 'Domicilio Geocodificado'");
		query.append(" FROM gis_callealtura ");
		query.append(" WHERE 1=1 ");
		query.append(" AND codigo = ? ");
		query.append(" AND alturaDesde <= ? ");
		query.append(" AND (alturaHasta IS NULL OR alturaHasta >= ?) ");
		query.append(" AND bis = ? ");

		// Arguments
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(domicilioDTO.codCalle));
		args.add(String.valueOf(domicilioDTO.altura));
		args.add(String.valueOf(domicilioDTO.altura));
		args.add(String.valueOf(domicilioDTO.bis));
		if(!TextUtils.isEmpty(domicilioDTO.letra)){
			query.append(" AND letraCalle = ? ");
			args.add(domicilioDTO.letra);
		}

		boolean result = false;
		SQLHelper sqlHelper = new SQLHelper(context);
		try {
			sqlHelper.openReadableDatabase();
			//Connecting to database
			//Execute query
			Cursor cursor = sqlHelper.rawQuery(query.toString(), 
					args.toArray(new String[args.size()]));
			result = cursor.moveToFirst();
			cursor.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}finally{
			//Close connection
			sqlHelper.closeDatabase();
		}

		return result;
	}

	@Override
	public String getPrinterValor() {
		//
		if(TextUtils.isEmpty(domicilioDTO.calle) 
				|| TextUtils.isEmpty(domicilioDTO.altura)){
			// Si calle o altura es vacio, no imprimo domicilio en ticket
			return "";
		}
		
		StringBuilder builder = new StringBuilder();
		
		// Pais != Argentina
		if(domicilioDTO.codPais > 1){
			builder.append("Pais: "+domicilioDTO.pais);
			builder.append("<br>");
		}
		builder.append("Localidad: "+domicilioDTO.localidad);
		builder.append("<br>");
		builder.append("Calle: "+domicilioDTO.calle);
		builder.append("<br>");
		builder.append("Altura: "+domicilioDTO.altura);
		if (domicilioDTO.bis) {
			builder.append(" Bis ");	
		}
		if (!TextUtils.isEmpty(domicilioDTO.mblk)) {
			builder.append(" Monoblock: "+domicilioDTO.mblk);	
		}
		if (!TextUtils.isEmpty(domicilioDTO.piso)) {
			builder.append(" Piso: "+domicilioDTO.piso);	
		}
		if (!TextUtils.isEmpty(domicilioDTO.dpto)) {
			builder.append(" Dpto.: "+domicilioDTO.dpto);	
		}

		return builder.toString();
	}
}
