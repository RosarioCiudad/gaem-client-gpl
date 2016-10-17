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
import android.database.Cursor;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
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
public class ComboExtGUI extends CampoGUI {

	private final String LOG_TAG = getClass().getSimpleName();

	private Spinner cmbValores;
	private LinearLayout mainLayout;
	private ComboExtDTO comboDTO;
	private boolean showEditText;
	private EditText editText;

	//
	private final static String ID_OPCION_SELECCIONAR = "-1";
	private final static String ID_OPCION_OTRA = "OTR";

	// Constructs
	public ComboExtGUI(Context context) {
		super(context);
	}

	public ComboExtGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	public ComboExtGUI(Context context, boolean enabled, boolean showEditText) {
		super(context, enabled);
		this.showEditText = showEditText;
	}

	// Getters y Setters
	public String getValorView() {
		String value;
		if(comboDTO.id.equals(ID_OPCION_SELECCIONAR)){
			value = " ";
		}else{
			value = comboDTO.id +"|"+comboDTO.description;
		}
		return  value;
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
		LinearLayout cmbLayout = new LinearLayout(context);
		cmbLayout.setOrientation(LinearLayout.VERTICAL);
		cmbLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));

		//--
		Integer position = 0;
		comboDTO = new ComboExtDTO();
		// Se setea el valor precargado o valor por defecto en caso que exista
		if(getInitialValues() != null && getInitialValues().size() == 1){
			Value value = this.getInitialValues().get(0);
			if(value.getValor().contains("|")){
				//
				String[] values = value.getValor().split("\\|");
				this.comboDTO.id = values[0];
				try {
					this.comboDTO.description = values[1];
				} catch (Exception e) {
					Log.e(LOG_TAG, "**ERROR**", e);
				}
			}
		}

		List<ComboExtDTO> listItem = new ArrayList<ComboExtDTO>();
		ComboExtDTO item = new ComboExtDTO();
		// Opcion Seleccionar
		item.id = ID_OPCION_SELECCIONAR;
		item.description = "Seleccionar";
		listItem.add(item);
		
		//TODO:
	 	if(valorDefault.equals("aid_tipoDocumento")){
	 		// Si es tipoDocumento, no considero opcion seleccionar
	 		listItem.remove(0);
	 	}

		SQLHelper sqlHelper = new SQLHelper(context);
		//Connecting to database
		sqlHelper.openReadableDatabase();
		try {
			String query = String.format("SELECT codigo, descripcion FROM %s ORDER BY 2", valorDefault);
			//Execute query
			Cursor cursor = sqlHelper.rawQuery(query, new String[]{});
			cursor.moveToFirst();
			do {
				item = new ComboExtDTO();
				item.id = cursor.getString(0);
				item.description = cursor.getString(1);
				if(this.comboDTO.id.equals(item.id)) position = listItem.size();
				listItem.add(item);
			} while (cursor.moveToNext());
			cursor.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		} finally {
			//Close connection
			sqlHelper.closeDatabase();
		}		
		//--
		if(showEditText){
			item = new ComboExtDTO();
			item.id = ID_OPCION_OTRA;
			item.description = "Otra";
			listItem.add(item);
		}

		// Adapter de Opciones
		final ArrayAdapter<ComboExtDTO> adapter;
		adapter = new ArrayAdapter<ComboExtDTO>(context, android.R.layout.simple_spinner_item, listItem) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(context, android.R.layout.simple_spinner_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					holder.setTextColor(context.getResources().getColor(R.color.label_text_color));
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).description);
				return convertView;
			}
			@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					//convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
					convertView = View.inflate(context, R.layout.custom_spinneritem, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).description);
				return convertView;
			}
		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		editText = new EditText(context);
		editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		editText.setText(comboDTO.description);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		editText.setEnabled(enabled);
		editText.setFocusable(enabled);
		editText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(50) });
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				comboDTO.description = s.toString();
			}
		});

		// Combo Spinner
		this.cmbValores = new Spinner(context);
		this.cmbValores.setPrompt(this.getEtiqueta());
		this.cmbValores.setEnabled(enabled);
		this.cmbValores.setAdapter(adapter);
		this.cmbValores.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
				ComboExtDTO item = adapter.getItem(pos);
				comboDTO.id = item.id;
				comboDTO.description = item.description;
				if(item.id.equals(ID_OPCION_OTRA)){
					editText.setVisibility(View.VISIBLE);
				}else{
					editText.setVisibility(View.GONE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		this.cmbValores.setSelection(position, true);
		// Se agrega el spinner al Layout
		cmbLayout.addView(this.cmbValores);

		// Se agregan los componentes a la fila
		mainLayout.addView(this.label);
		mainLayout.addView(cmbLayout);
		mainLayout.addView(editText);

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
	public boolean validate() {
		// Verifico si es un campo obligatorio
		if(isObligatorio() == false ||  this.cmbValores.isEnabled() == false){
			//
			return true;
		}
		// Limpio errores previos
		editText.setError(null);
		label.setError(null);
		// Opcion SELECCIONAR
		if (comboDTO.id.equals(ID_OPCION_SELECCIONAR)) {
			label.setError(context.getString(R.string.field_required, getEtiqueta()));
			this.cmbValores.setFocusableInTouchMode(true);
			this.cmbValores.requestFocus();
			this.cmbValores.setFocusableInTouchMode(false);
			return false;
		}
		// Opcion OTRA
		if (comboDTO.id.equals(ID_OPCION_OTRA) 
				&& TextUtils.isEmpty(editText.getText())) {
			editText.setError(context.getString(R.string.field_required, getEtiqueta()));
			editText.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public boolean isDirty(){
		boolean dirty = false;
		if(getInitialValues() != null && getInitialValues().size() == 1){
			ComboExtDTO opcion = (ComboExtDTO) cmbValores.getSelectedItem();
			dirty = !comboDTO.id.equals(opcion.id);
		}else{
			dirty = true;
		}
		if(dirty){
			Log.d(LOG_TAG, String.format("%s dirty=true", getEtiqueta()));
		}
		return dirty;
	}

	/**
	 * 
	 * @author tecso.coop
	 *
	 */
	private class ComboExtDTO {
		String id = "";
		String description = "";
	}

	@Override
	public String getPrinterValor() {
		//
		if(comboDTO.id.equals(ID_OPCION_SELECCIONAR)){
			return "";
		}
		
		return getEtiqueta()+": "+comboDTO.description;
	}
}