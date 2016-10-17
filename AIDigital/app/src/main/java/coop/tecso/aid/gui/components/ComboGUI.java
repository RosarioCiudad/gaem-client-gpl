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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Tratamiento;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;

/**
 * 
 * @author tecso.coop
 *
 */
public class ComboGUI extends CampoGUI {

	private final String LOG_TAG = getClass().getSimpleName();

	protected Spinner cmbValores;
	private LinearLayout mainLayout;

	// Constructs
	public ComboGUI(Context context) {
		super(context);
	}

	public ComboGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public ComboGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	// Getters y Setters


	// Metodos
	@Override
	public View build() {
		// Etiqueta
		this.label = new TextView(context);
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta()+": ");

		if(appState.getOrientation() == LinearLayout.VERTICAL){
			// Se define un LinearLayout para ubicar: 'Label / EditText'
			mainLayout = new LinearLayout(context);
			mainLayout.setOrientation(LinearLayout.VERTICAL);
		}else{
			//Se define un Table Row para ubicar: 'Label | EditText'
			mainLayout = new TableRow(context);
			mainLayout.setGravity(Gravity.CENTER_VERTICAL);
			this.label.setGravity(Gravity.RIGHT);
		}
		mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));

		// Combo Layout
		final LinearLayout cmbLayout = new LinearLayout(context);
		cmbLayout.setOrientation(LinearLayout.VERTICAL);
		cmbLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));

		// Opciones
		final List<CampoGUI> items = new ArrayList<CampoGUI>();
		for(Component co: this.components){
			CampoGUI campoGUI = (CampoGUI) co;
			campoGUI.removeAllViewsForMainLayout();
			items.add(campoGUI);
		}

		// Adapter de Opciones
		final ArrayAdapter<CampoGUI> adapter;
		adapter = new ArrayAdapter<CampoGUI>(context, android.R.layout.simple_spinner_item, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(context, android.R.layout.simple_spinner_item, null);
					// Creates a ViewHolder and store references to the two children
					// views we want to bind data to.
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					holder.setTextColor(context.getResources().getColor(R.color.label_text_color));
					convertView.setTag(holder);
				} else {
					// Get the ViewHolder back to get fast access to the TextView
					// and the ImageView.
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getEtiqueta());
				return convertView;
			}
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Combo Spinner
		this.cmbValores = new Spinner(context);
		this.cmbValores.setPrompt(this.getEtiqueta());
		this.cmbValores.setEnabled(enabled);
		this.cmbValores.setAdapter(adapter);
		
		// Se agrega el spinner al Layout
		cmbLayout.addView(this.cmbValores);

		this.cmbValores.setOnItemSelectedListener(new OnItemSelectedListener() {
			private CampoGUI itemPrevio = null;
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
				if (itemPrevio != null) {					
					cmbLayout.removeView(itemPrevio.getEditViewForCombo());
				}
				CampoGUI itemActual = adapter.getItem(pos);
				if (itemActual.getEditViewForCombo() != null){
					cmbLayout.addView(itemActual.getEditViewForCombo());				
					itemPrevio = itemActual;				
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		// Se setea el valor precargado o valor por defecto en caso que exista
		int idOpcion = 0;
		if(this.getInitialValues() != null && this.getInitialValues().size() == 1){
			Value value = this.getInitialValues().get(0);
			if(this.entity instanceof AplPerfilSeccionCampo){
				idOpcion = value.getCampoValor().getId();
			}else if(this.entity instanceof AplPerfilSeccionCampoValor){
				idOpcion = value.getCampoValorOpcion().getId();
			}
		}else if(!TextUtils.isEmpty(this.getValorDefault())){
			try{
				idOpcion = Integer.valueOf(this.getValorDefault()).intValue();
			}catch (Exception e) {
				idOpcion = 0;
				Log.d(LOG_TAG, "build(): el valor por defecto debe ser númerico: "+this.getValorDefault(), e);
			}
		}
		if(idOpcion > 0){
			for(Component co: this.components){
				CampoGUI campoGUI = (CampoGUI) co;
				if(campoGUI.getEntity().getId()==idOpcion){
					int pos = adapter.getPosition(campoGUI);
					this.cmbValores.setSelection(pos, true); // el 2do parametro en true fuerza el ItemSelectedListener
				}
			}
		}
		// Se agregan los componentes a la fila
		mainLayout.addView(this.label);
		mainLayout.addView(cmbLayout);

		this.view = mainLayout;

		return this.view;
	}

	@Override
	public View getEditViewForCombo(){
		this.mainLayout.removeView(this.label);
		return this.mainLayout;
	}

	@Override
	public View redraw() {
		this.cmbValores.setEnabled(enabled);
		return this.view;
	}

	@Override
	public List<Value> values() {
		this.values = new ArrayList<Value>();

		CampoGUI opcion = (CampoGUI) this.cmbValores.getSelectedItem();
		if(opcion != null){
			this.values.addAll(opcion.values());
		}

		return this.values;
	}

	@Override
	public boolean isDirty(){

		boolean dirty = true;
		CampoGUI opcion = (CampoGUI) cmbValores.getSelectedItem();

		if(getInitialValues() != null && getInitialValues().size() == 1){
			Value value = getInitialValues().get(0);
			if(opcion.getTratamiento().equals(Tratamiento.NA)){
				// Opcion Simple
				if(this.entity instanceof AplPerfilSeccionCampo){
					dirty = value.getCampoValor().getId() != opcion.getEntity().getId();
				}else if(this.entity instanceof AplPerfilSeccionCampoValor){
					dirty = value.getCampoValorOpcion().getId() != opcion.getEntity().getId();
				}
			}else{
				//Opcion Anidada
				dirty = opcion.isDirty();
			}
		}

		if(dirty){
			Log.d(LOG_TAG, String.format("%s dirty=true", getEtiqueta()));
		}

		return dirty;
	}

	@Override
	public View disable() {
		super.disable();
		// Disable childs
		if(this.components != null){
			for(Component co: this.components){
				co.disable();
			}
		}
		return this.view;
	}

	@Override
	public boolean validate() {
		// Verifico si es un campo obligatorio
		if(isObligatorio() == false){
			//
			return true;
		}
		label.setError(null);
		// Verifico si la opcion es obligatoria
		CampoGUI option = (CampoGUI) cmbValores.getSelectedItem();
		if (!option.validate()) {
			label.setError(context.getString(R.string.field_required, getEtiqueta()));
			this.cmbValores.setFocusableInTouchMode(true);
			this.cmbValores.requestFocus();
			this.cmbValores.setFocusableInTouchMode(false);
			return false;
		}

		return true;
	}

	@Override
	public String getPrinterValor() {
		CampoGUI option = (CampoGUI) cmbValores.getSelectedItem();

		//No imprimir sexo, ni opcion seleccionar en ticket
		if(getEtiqueta().equalsIgnoreCase("sexo") 
				|| TextUtils.isEmpty(option.getEtiqueta())
				|| option.getEtiqueta().equalsIgnoreCase("seleccionar")){
			return "";
		}

		StringBuilder builder = new StringBuilder();
		builder.append(getEtiqueta());
		builder.append(": ");
		builder.append(option.getEtiqueta());

		return builder.toString();
	}

	@Override
	public String getValorView() {
		// TODO Auto-generated method stub
		return null;
	}

}