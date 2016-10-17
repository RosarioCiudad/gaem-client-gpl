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

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;

/**
 * 
 * @author tecso.coop   
 *
 */
public class TextGUI extends CampoGUI {

	private static final String LOG_TAG = TextGUI.class.getSimpleName();

	public static enum Teclado {
		ALFANUMERICO, NUMERICO, DECIMAL, NUMERICO_EXTENDIDO
	};
	private boolean multiLine;
	private Teclado teclado;
	private int inputType;
	private int maxChar = 250;

	private LinearLayout mainLayout;
	private EditText textBox;

	public TextGUI(Context context) { 
		super(context);
		this.textBox.setTextAppearance(context, R.style.UiTextViewMultine);
		this.textBox.setBackgroundResource(android.R.drawable.editbox_background);
	}

	public TextGUI(Context context, List<Value> values) {
		super(context,values);
		this.textBox.setTextAppearance(context, R.style.UiTextViewMultine);
		this.textBox.setBackgroundResource(android.R.drawable.editbox_background);
	}

	public TextGUI(Context context, Teclado teclado, boolean multiLine) {
		super(context);
		this.multiLine = multiLine;
		this.textBox = new EditText(context);
		this.textBox.setTextAppearance(context, R.style.UiTextViewMultine);
		this.textBox.setBackgroundResource(android.R.drawable.editbox_background); 
		setInputType(teclado);
	}

	public TextGUI(Context context, Teclado teclado, boolean multiLine, boolean enabled) {
		super(context, enabled);
		this.multiLine = multiLine;
		this.textBox = new EditText(context);
		this.textBox.setTextAppearance(context, R.style.UiTextViewMultine);
		this.textBox.setBackgroundResource(android.R.drawable.editbox_background);
		setInputType(teclado);
	}

	// Getters y Setters
	private void setInputType(Teclado teclado) {
		switch (teclado) {
		case NUMERICO:
			inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_NUMBER;
			this.textBox.setInputType(inputType);
			break;
		case DECIMAL:
			inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL ;
			this.textBox.setInputType(inputType);
			break;
		case NUMERICO_EXTENDIDO:
			inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE| InputType.TYPE_CLASS_PHONE;
			this.textBox.setInputType(inputType);
			break;
		default:
			inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE| InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
			this.textBox.setInputType(inputType);
			break;
		}
		this.teclado = teclado;
	}
	public void setInputType(int inputType) {		
		this.textBox.setInputType(inputType);
	}
	public int getInputType() {
		return this.textBox.getInputType();
	}
	public Teclado getTeclado() {
		return this.teclado;
	}
	public boolean isMultiLine() {
		return multiLine;
	}
	public String getValorView() {
		return  this.textBox.getText().toString();
	}

	// Metodos
	@Override
	public View build() {
		// Etiqueta
		this.label = new TextView(context);
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta()+": ");

		if(appState.getOrientation() == LinearLayout.VERTICAL){
			// Se define un LinearLayout para ubicar: 'Label / EditText'
			this.mainLayout = new LinearLayout(context);
			this.mainLayout.setOrientation(LinearLayout.VERTICAL);
		}else{
			//Se define un Table Row para ubicar: 'Label | EditText'
			this.mainLayout = new TableRow(context);
			this.label.setGravity(Gravity.RIGHT);
			this.label.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3f));
			this.textBox.setLayoutParams(new LayoutParams(50,  LayoutParams.WRAP_CONTENT,1f));
		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);

		// Texto editable
		this.textBox.setEnabled(enabled);
		this.textBox.setFocusable(enabled);
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			this.textBox.setText(this.getInitialValues().get(0).getValor());
		}else{
			this.textBox.setText(this.getValorDefault());
		}

		if(!TextUtils.isEmpty(this.getMascaraVisual())){
			try {maxChar = Integer.valueOf(this.getMascaraVisual()).intValue();
			} catch (Exception e) {}
		}

		//   Se crea y aplica un filtro para definir la cantidad maxima de caracteres de la caja de texto
		InputFilter maxLengthFilter = new InputFilter.LengthFilter(maxChar);
		this.textBox.setFilters(new InputFilter[]{ maxLengthFilter });

		// Se agrega filtro sobre caracteres permitidos para los teclados numericos
		switch (this.teclado) {
		case NUMERICO_EXTENDIDO:
			InputFilter filterDecimal = new InputFilter() { 
				public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) { 
					for (int i = start; i < end; i++) { 
						if (!Character.isDigit(source.charAt(i)) && source.charAt(i) != '.' && source.charAt(i) != '/') { 
							return ""; 
						}
					} 
					return null; 
				} 
			}; 
			this.textBox.setFilters(new InputFilter[]{filterDecimal}); 
			break;
		default:
			break;
		}

		//   Se agrega validacion de requerido al cambio de foco
		this.textBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus && isObligatorio()) {
					if(TextUtils.isEmpty(textBox.getText().toString())){
						textBox.setError(context.getString(R.string.field_required, getEtiqueta()));
					}else{
						textBox.setError(null);
					}
				}
			}
		});

		//   Se agrega cambio de foco luego de editar
		if(!multiLine){
			this.textBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
					view.clearFocus();
					return true;
				}});
		}

		this.mainLayout.addView(label);
		this.mainLayout.addView(textBox);

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

		if(dirty){
			Log.d(LOG_TAG, String.format("%s dirty=true", getEtiqueta()));
		}
		return dirty;
	}

	@Override
	public boolean validate() {
		if (isObligatorio() && TextUtils.isEmpty(this.textBox.getText().toString())) {
			textBox.setError(context.getString(R.string.field_required, getEtiqueta()));
			textBox.requestFocus();
			return false;
		}

		return true;
	}

	@Override
	public View getEditViewForCombo(){
		return this.textBox;
	}

	@Override
	public void removeAllViewsForMainLayout(){
		this.mainLayout.removeAllViews();
	}

	@Override
	public String getPrinterValor() {
		if(!TextUtils.isEmpty(getValorView()))
			return getEtiqueta()+": "+getValorView();
		else
			return "";
	}
}