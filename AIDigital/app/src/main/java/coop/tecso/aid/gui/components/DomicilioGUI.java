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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
public class DomicilioGUI extends CampoGUI {

	private static final String LOG_TAG = DomicilioGUI.class.getSimpleName();

	private AutoCompleteTextView searchBoxCalle;
	private AutoCompleteTextView searchBoxIntersec;
	private EditText editTextAltura;
	private DomicilioDTO domicilioDTO;
	private TextView labelSentido;
	private Spinner spinnerSentido; 

	// Constructs
	public DomicilioGUI(Context context) {
		super(context);
	}

	public DomicilioGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public DomicilioGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	@Override
	public View build() {
		// Domicilio
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			//
			String strDomicilio = this.getInitialValues().get(0).getValor();
			domicilioDTO = parseDomicilio(strDomicilio);
		}else{
			domicilioDTO = parseDomicilio(this.getValorDefault());
		}
		// Calle
		TextView labelCalle = new TextView(context);
		labelCalle.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelCalle.setText("Calle: ");

		LinearLayout layoutCalle = buildLayout(labelCalle);
		searchBoxCalle = new AutoCompleteTextView(context);
		searchBoxCalle.setTextAppearance(context, R.style.UiEditTex);
		searchBoxCalle.setBackgroundResource(android.R.drawable.editbox_background);
		

		// Create an ItemAutoTextAdapter for the State Name field,
		// and set it as the OnItemClickListener for that field.
		CalleAutoTextAdapter adapter = this.new CalleAutoTextAdapter(context);
		searchBoxCalle.setAdapter(adapter);
		searchBoxCalle.setOnItemClickListener(adapter);
		searchBoxCalle.setEnabled(enabled);
		searchBoxCalle.setThreshold(2);
		searchBoxCalle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		searchBoxCalle.setText(domicilioDTO.calle);

		layoutCalle.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutCalle.addView(searchBoxCalle);

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
				// Actualizo Altura
				domicilioDTO.altura = s.toString();
				if(!TextUtils.isEmpty(domicilioDTO.altura)){
					// Limpio Interseccion
					domicilioDTO.codigoInterseccion = 0;
					domicilioDTO.interseccion = "";
					searchBoxIntersec.setText("");
				}
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
		EditText editTextLetra = new EditText(context);
		editTextLetra.setTextAppearance(context, R.style.UiEditTex);
		editTextLetra.setBackgroundResource(android.R.drawable.editbox_background);
		editTextLetra.setFocusable(enabled);
		editTextLetra.setEnabled(enabled);
		editTextLetra.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		InputFilter maxLengthFilter = new InputFilter.LengthFilter(1);
		editTextLetra.setFilters(new InputFilter[]{ maxLengthFilter });

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
		editTextLetra.setText(domicilioDTO.letra);
		editTextLetra.addTextChangedListener(textWatcherLetra);

		labelSentido = new TextView(context);
		labelSentido.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelSentido.setText("Sentido: ");

		// Interseccion
		TextView labelIntersec = new TextView(context);
		labelIntersec.setTextColor(context.getResources().getColor(R.color.label_text_color));
		labelIntersec.setText("Intersección: ");
		searchBoxIntersec = new AutoCompleteTextView(context);
		searchBoxIntersec.setTextAppearance(context, R.style.UiEditTex);
		searchBoxIntersec.setBackgroundResource(android.R.drawable.editbox_background);
		
		// Create an ItemAutoTextAdapter for the State Name field,
		// and set it as the OnItemClickListener for that field.
		IntersecAutoTextAdapter intersecAdapter = this.new IntersecAutoTextAdapter(context);
		searchBoxIntersec.setAdapter(intersecAdapter);
		searchBoxIntersec.setOnItemClickListener(intersecAdapter);
		searchBoxIntersec.setEnabled(enabled);
		searchBoxIntersec.setThreshold(2);
		searchBoxIntersec.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		searchBoxIntersec.setText(domicilioDTO.interseccion);

		LinearLayout layoutIntersec = buildLayout(labelIntersec);
		layoutIntersec.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutIntersec.addView(searchBoxIntersec);

		// Container: Altura | Bis | LetraCalle
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

		LinearLayout layoutSentido = buildLayout(labelSentido);
		layoutSentido.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layoutSentido.addView(buildSpinnerSentido());

		// Container Params
		LinearLayout.LayoutParams param;
		// Altura
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 20f;
		layoutContainer.addView(layoutAltura, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f;
		layoutContainer.addView(new LinearLayout(context), param);
		// Bis
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 15f;
		param.gravity = Gravity.CENTER_HORIZONTAL;
		layoutContainer.addView(layoutBis, param);
		// Letra
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		//param.weight = 10f;
		param.weight = 15f;
		layoutContainer.addView(layoutLetra, param);
		// Separador
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 5f;
		//param.weight = 45f;
		layoutContainer.addView(new LinearLayout(context), param);
		// Sentido
		param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		param.weight = 40f;
		layoutContainer.addView(layoutSentido, param);

		// Build Section
		TableLayout mainLayout = new TableLayout(context);

		// Calle
		mainLayout.addView(layoutCalle);
		View separator = new View(context);
		separator.setBackgroundColor(context.getResources().getColor(R.color.label_text_color));
		mainLayout.addView(separator, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
		// Altura | Bis | LetraCalle | Boton
		mainLayout.addView(layoutContainer);
		separator = new View(context);
		separator.setBackgroundColor(context.getResources().getColor(R.color.label_text_color));
		mainLayout.addView(separator, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
		// Interseccion
		mainLayout.addView(layoutIntersec);

		Log.i(LOG_TAG, "buildSeccionDomicilio: exit");
		this.view = mainLayout;

		return this.view;
	}

	/**
	 * 
	 * @return
	 */
	private Spinner buildSpinnerSentido() {
		Log.i(LOG_TAG, "buildSpinnerSentido: enter");

		List<SpinnerDTO> listItem = new ArrayList<DomicilioGUI.SpinnerDTO>();
		listItem.add(new SpinnerDTO(-1,"Seleccionar"));
		listItem.add(new SpinnerDTO(1,"Este -> Oeste"));
		listItem.add(new SpinnerDTO(2,"Oeste -> Este"));
		listItem.add(new SpinnerDTO(3,"Norte -> Sur"));
		listItem.add(new SpinnerDTO(4,"Sur -> Norte"));

		// Spinner
		spinnerSentido = new Spinner(context);
		spinnerSentido.setPrompt("Seleccione un Sentido:");
		spinnerSentido.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, 
					View selectedItemView, int position, long id) {
				// Selection
				SpinnerDTO spinnerDTO = (SpinnerDTO) parentView.getItemAtPosition(position);
				domicilioDTO.sentido = spinnerDTO.id;
				domicilioDTO.descSentido = spinnerDTO.description;
				labelSentido.setError(null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}

		});
		spinnerSentido.setEnabled(enabled);
		spinnerSentido.setFocusable(enabled);

		ArrayAdapter<SpinnerDTO> arrayAdapter;
		arrayAdapter = new ArrayAdapter<SpinnerDTO>(context, android.R.layout.simple_spinner_item, listItem) {
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
					convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).description);
				return convertView;
			}
		};
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSentido.setAdapter(arrayAdapter);

		if(domicilioDTO.sentido > 0){
			for (int i = 0; i < listItem.size(); i++) {
				if(listItem.get(i).id == domicilioDTO.sentido){
					spinnerSentido.setSelection(i, false);
					break;
				}
			}
		}

		Log.i(LOG_TAG, "buildSpinnerSentido: exit");
		return spinnerSentido;
	}

	private class SpinnerDTO {
		private int id;
		private String description;
		public SpinnerDTO(int id, String description) {
			this.id = id;
			this.description = description;
		}
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
		searchBoxCalle.setError(null);
		searchBoxIntersec.setError(null);
		editTextAltura.setError(null);

		// Calle
		if(domicilioDTO.codigoCalle == 0){
			searchBoxCalle.setError(context.getString(R.string.field_required, "Calle"));
			searchBoxCalle.requestFocus();
			return false;
		}
		
		// Altura 
		if(TextUtils.isEmpty(editTextAltura.getText()) && domicilioDTO.codigoInterseccion == 0){
			searchBoxIntersec.setError(context.getString(R.string.field_required, "Altura\" o \"Intersección"));
			editTextAltura.setError("");
			searchBoxIntersec.requestFocus();
			return false;
		}
		
		// Valido altura
		if(!TextUtils.isEmpty(editTextAltura.getText())){
			if(validateDomicilio() == false){
				//Domicilio no geocodificado
				searchBoxCalle.setError("El Domicilio ingresado es inválido");
				searchBoxCalle.requestFocus();
				return false;
			}
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
	 * codigoCalle|altura|bis|letra|codigoInterseccion|calle|interseccion|sentido
	 * 
	 * @return
	 */
	public String getValorView() {
		StringBuilder builder = new StringBuilder();
		builder.append(domicilioDTO.codigoCalle);
		builder.append("|");
		builder.append(domicilioDTO.altura);
		builder.append("|");
		builder.append(domicilioDTO.bis);
		builder.append("|");
		builder.append(domicilioDTO.letra);
		builder.append("|");
		builder.append(domicilioDTO.codigoInterseccion);
		builder.append("|");
		builder.append(domicilioDTO.calle);
		builder.append("|");
		builder.append(domicilioDTO.interseccion);
		builder.append("|");
		builder.append(domicilioDTO.sentido);

		return  builder.toString();
	}

	/**
	 * codigoCalle|altura|bis|letra|codigoInterseccion|calle|interseccion|sentido
	 * @param strToParse
	 * @return
	 */
	private DomicilioDTO parseDomicilio(String strToParse) {
		Log.i(LOG_TAG, "parseDomicilio: enter");

		DomicilioDTO domicilioDTO = new DomicilioDTO();

		if(!TextUtils.isEmpty(strToParse)){
			String[] domicilio = strToParse.split("\\|");
			//
			domicilioDTO.codigoCalle =  Integer.valueOf(domicilio[0]).intValue();
			domicilioDTO.altura = domicilio[1];
			domicilioDTO.bis =  Boolean.valueOf(domicilio[2]);
			domicilioDTO.letra = domicilio[3];
			domicilioDTO.codigoInterseccion = Integer.valueOf(domicilio[4]).intValue();
			domicilioDTO.calle = domicilio[5];
			domicilioDTO.interseccion = domicilio[6];
			domicilioDTO.sentido = Integer.valueOf(domicilio[7]).intValue();	
		}

		Log.i(LOG_TAG, "parseDomicilio: exit");
		return domicilioDTO;
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
			queryBuilder.append(" SELECT codigo AS _id, abreviatura ");
			queryBuilder.append(" FROM gis_calle ");
			queryBuilder.append(" WHERE 1=1 ");

			Cursor cursor;
			if (!TextUtils.isEmpty(constraint)) {
				// Query for any rows where the state name begins with the
				// string specified in constraint.
				constraint = String.format("%%%s%%", constraint.toString().trim());
				queryBuilder.append(" AND abreviatura COLLATE NOCASE LIKE  ? ORDER BY 2");
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
			if( cursor.getInt(0) != domicilioDTO.codigoCalle){
				domicilioDTO.codigoCalle =  cursor.getInt(0);
				domicilioDTO.calle = cursor.getString(1);
				domicilioDTO.codigoInterseccion = 0;
				searchBoxIntersec.setText("");
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
	private class IntersecAutoTextAdapter extends CursorAdapter implements OnItemClickListener {

		private SQLHelper sqlHelper;
		public IntersecAutoTextAdapter(Context context) {
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
			queryBuilder.append(" SELECT c.codigo AS _id, c.abreviatura ");
			queryBuilder.append(" FROM gis_calle c, gis_calleinterseccion i");
			queryBuilder.append(" WHERE 1=1 ");
			queryBuilder.append(" AND c.codigo = i.codigointerseccion ");
			queryBuilder.append(" AND i.codigocalle = "+domicilioDTO.codigoCalle);

			Cursor cursor;
			if (!TextUtils.isEmpty(constraint)) {
				// Query for any rows where the state name begins with the
				// string specified in constraint.
				constraint = String.format("%%%s%%", constraint.toString().trim());
				queryBuilder.append(" AND c.abreviatura COLLATE NOCASE LIKE  ? ORDER BY 2");
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
			// Limpio altura
			editTextAltura.setText("");
			// Interseccion
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			domicilioDTO.codigoInterseccion = cursor.getInt(0);
			domicilioDTO.interseccion = cursor.getString(1);
		}
	}

	private class DomicilioDTO {
		private String calle = "";
		private String interseccion = "";
		private String altura = "";
		private int codigoCalle;
		private int codigoInterseccion;
		private String letra = "";
		private boolean bis;
		private int sentido;
		private String descSentido;
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
		args.add(String.valueOf(domicilioDTO.codigoCalle));
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
		StringBuilder builder = new StringBuilder();
		// Calle
		builder.append("Calle: "+domicilioDTO.calle);
		// Altura
		if(!TextUtils.isEmpty(domicilioDTO.altura)){
			builder.append("<br>");
			builder.append("Altura: ");
			builder.append(domicilioDTO.altura);
			if (domicilioDTO.bis) builder.append(" Bis ");	
		}
		// Sentido
		if(domicilioDTO.sentido > 0){
			builder.append("<br>");
			builder.append("Sentido: "+domicilioDTO.descSentido);
			builder.append("<br>");
		}
		// Interseccion
		if(!TextUtils.isEmpty(domicilioDTO.interseccion)){
			builder.append("<br>");
			builder.append("Intersección: ");
			builder.append(domicilioDTO.interseccion);
			builder.append("<br>");
		}
		return builder.toString();
	}
}