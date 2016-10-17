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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

/**
 * 
 * @author tecso.coop
 *
 */
public class DateGUI extends CampoGUI {
	
	private static final String LOG_TAG = DateGUI.class.getSimpleName();

	private int mes;
	private int dia;
	private int anio;
	
	private LinearLayout mainLayout;
	private LinearLayout buttonLayout;
	private Button button;
	
	// Constructs
	public DateGUI(Context context) {
		super(context);
	}

	public DateGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public DateGUI(Context context, boolean enabled) {
		super(context, enabled);
	}
	
	// Getters y Setters
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public String getValorView() {
		return this.button.getText().toString();
	}
	
	// Metodos
	protected Dialog crearDialog() {
		return new DatePickerDialog(this.context, mDateSetListener, anio, mes, dia);
	}

	/**
	 *  Updates the date in the TextView
	 */
	private void updateDisplay() {
		Calendar date = Calendar.getInstance();
		date.set(anio, mes, dia);
		
		button.setText(new SimpleDateFormat("dd/MM/yyyy").format(date.getTime()));
	}

	/**
	 *  The callback received when the user "sets" the date in the dialog
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			anio = year;
			mes = monthOfYear;
			dia = dayOfMonth;
			updateDisplay();
		}
	};
	
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
		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);
		// Se arma el boton para disparar control se seleccion de fecha
		this.button = new Button(context);
		this.button.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				crearDialog().show();
			}
		});
		this.button.setEnabled(enabled);
		// Se carga el valor inicial del campo: 'Valor Precargado', 'Valor por defecto de perfil' o 'Fecha Actual'
		String fecha = ""; 
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			fecha = this.getInitialValues().get(0).getValor();
		}else{
			fecha = this.getValorDefault();
		}
		Date d = new Date();
		if(!TextUtils.isEmpty(fecha)){
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy"); 
			try {
				d = sdf.parse(fecha);
			} catch (ParseException e) {
				d = new Date();
				Log.e(DateGUI.class.getSimpleName(), "build(): no se pudo parsear la fecha: "+fecha, e);
			} 
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		this.anio = c.get(Calendar.YEAR);
		this.mes = c.get(Calendar.MONTH);
		this.dia = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		// Se contiene el boton en un linear layout para que no se expanda junto a la columna
		this.buttonLayout = new LinearLayout(context);
		this.buttonLayout.addView(button);
		
		// Se cargan los componentes en el layout
		this.mainLayout.addView(label);
		this.mainLayout.addView(this.buttonLayout);
		
		this.view = mainLayout;
		
		return this.view;
	}

	@Override
	public View redraw() {
		this.button.setEnabled(enabled);
		return this.view;
	}
	
	@Override
	public List<Value> values() {
		this.values = new ArrayList<Value>();

		AplPerfilSeccionCampo campo = null;
		AplPerfilSeccionCampoValor campoValor = null;
		AplPerfilSeccionCampoValorOpcion campoValorOpcion = null;
		if(this.entity instanceof AplPerfilSeccionCampo){
			campo = (AplPerfilSeccionCampo) this.entity;
		}else if(this.entity instanceof AplPerfilSeccionCampoValor){
			campoValor = (AplPerfilSeccionCampoValor) this.entity;
			campo = campoValor.getAplPerfilSeccionCampo();
		}else if(this.entity instanceof AplPerfilSeccionCampoValorOpcion){
			campoValorOpcion = (AplPerfilSeccionCampoValorOpcion) this.entity;
			campoValor = campoValorOpcion.getAplPerfilSeccionCampoValor();
			campo = campoValor.getAplPerfilSeccionCampo();
		}
		String nombreCampo = campo!=null?campo.getCampo().getEtiqueta():"No identificado";
		String valor = this.getValorView();
		
		Log.d(DateGUI.class.getSimpleName(),"save() : "+this.getTratamiento()+" :Campo: "+nombreCampo
				+" idCampo: "+(campo!=null?campo.getId():"null")
				+", idCampoValor: "+(campoValor!=null?campoValor.getId():"null")
				+", idCampoValorOpcion: "+(campoValorOpcion!=null?campoValorOpcion.getId():"null")
				+", Valor: "+valor);
		
		Value data = new Value(campo,campoValor,campoValorOpcion,valor, null);
		this.values.add(data);
		
		return this.values;
	}

	@Override
	public boolean isDirty(){
		Log.d(LOG_TAG, "isDirty: enter");
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
	public View getEditViewForCombo(){
		return this.buttonLayout;
	}
	
	@Override
	public void removeAllViewsForMainLayout(){
		this.mainLayout.removeAllViews();
	}
}
