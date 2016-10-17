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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.helpers.SQLHelper;

/**
 * 
 * @author tecso.coop
 *
 */
public class SearchBoxGUI extends CampoGUI {

	private static final String LOG_TAG = SearchBoxGUI.class.getSimpleName();

	private LinearLayout mainLayout;
	private AutoCompleteTextView searchBox;
	private SearchDTO searchDTO;
	
	private String TABLE_NAME;

	// Constructs
	public SearchBoxGUI(Context context) {
		super(context);
	}

	public SearchBoxGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public SearchBoxGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	@Override
	public View build() {
		
		// Valor
		this.searchDTO = new SearchDTO();
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			String value = this.getInitialValues().get(0).getValor();
			String[] values = value.split("\\|");
			this.searchDTO.id = values[0];
			try {
				this.searchDTO.description =  values[1];
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
			}
			try {
				this.searchDTO.extra =  values[2];
			} catch (Exception e) {
				Log.e(LOG_TAG, "**ERROR**", e);
			}
		}

		// Etiqueta
		this.label = new TextView(context);
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta()+": ");
		
		// SearchBox
		ItemAutoTextAdapter adapter = this.new ItemAutoTextAdapter(context);
		searchBox = new AutoCompleteTextView(context);
		searchBox.setTextAppearance(context, R.style.UiTextViewNormal);
		searchBox.setBackgroundResource(android.R.drawable.editbox_background);
		searchBox.setAdapter(adapter);
		searchBox.setOnItemClickListener(adapter);
		searchBox.setEnabled(enabled);
		searchBox.setThreshold(2);
		searchBox.setText(searchDTO.description);
		searchBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		if(enabled)	searchBox.requestFocus();
		
//		if(appState.getOrientation() == LinearLayout.VERTICAL){
			// Se define un LinearLayout para ubicar: 'Label / EditText'
			this.mainLayout = new LinearLayout(context);
			this.mainLayout.setOrientation(LinearLayout.VERTICAL);
//		}else{
//			//Se define un Table Row para ubicar: 'Label | EditText'
//			this.mainLayout = new TableRow(context);
//			this.label.setGravity(Gravity.RIGHT);
//			this.label.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3f));
//			this.searchBox.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
//		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		//TODO
		TABLE_NAME = this.getValorDefault();

		mainLayout.addView(label);
		mainLayout.addView(searchBox);
		this.view = mainLayout;

		Log.i(LOG_TAG, "build: exit");
		return this.view;
	}

	@Override
	public View redraw() {
		this.searchBox.setEnabled(enabled);
		this.searchBox.setFocusable(enabled);
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
		
		// Valida Seleccion
		if(TextUtils.isEmpty(searchDTO.id)){
			searchBox.setError(context.getString(R.string.field_required, getEtiqueta()));
			searchBox.requestFocus();
			return false;
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
	public String getValorView() {
		String value = "";
		if(!TextUtils.isEmpty(searchDTO.id))
			value = searchDTO.id+"|"+searchDTO.description+"|"+searchDTO.extra;

		return value;
	}

	/**
	 * Specializes CursorAdapter to supply choices to a AutoCompleteTextView.
	 * Also implements OnItemClickListener to be notified when a choice is made,
	 * and uses the choice to update other fields on the Activity form.
	 * 
	 * @author tecso.coop
	 * 
	 */
	private class ItemAutoTextAdapter extends CursorAdapter implements OnItemClickListener {

		private SQLHelper sqlHelper;

		public ItemAutoTextAdapter(Context context) {
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
//			queryBuilder.append(" SELECT codigo AS _id, descripcion, descripcionLarga ");
			queryBuilder.append(" SELECT codigo AS _id, * "); // _id, codigo, descripcion, (descripcionLarga)
			queryBuilder.append(String.format(" FROM %s ",TABLE_NAME));
			queryBuilder.append(" WHERE 1=1 ");

			Cursor cursor;
			if (!TextUtils.isEmpty(constraint)) {
				StringBuilder ctrBuilder = new StringBuilder();
				ctrBuilder.append("%");
				ctrBuilder.append(constraint);
				ctrBuilder.append("%");
				queryBuilder.append(" AND (descripcion COLLATE NOCASE LIKE  ?");
				queryBuilder.append(" OR codigo COLLATE NOCASE LIKE ?)");
				//
				cursor = sqlHelper.rawQuery(queryBuilder.toString(), ctrBuilder.toString(), ctrBuilder.toString());
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
			return  cursor.getString(2);
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
			// Get the cursor, positioned to the corresponding row in the result set
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			searchDTO.id = cursor.getString(0);
			searchDTO.description = cursor.getString(2);
			if(cursor.isNull(3)){
				// Caso Normal
				searchDTO.extra = cursor.getString(2);
				searchBox.setEnabled(true);
			}else{
				// Caso Infracciones
				searchDTO.extra = cursor.getString(3);
				searchBox.setEnabled(false);
			}
		}
	}
	
	/**
	 * 
	 */
	private class SearchDTO{
		private String id = "";
		private String description = "";
		private String extra = "";
	}
	
	@Override
	public String getPrinterValor() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("<b>");
		builder.append(searchDTO.id);
		builder.append(": ");
		builder.append(searchDTO.extra);
		builder.append("</b>");
		
		return builder.toString();
	}
}