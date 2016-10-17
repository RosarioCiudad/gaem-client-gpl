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
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
public class CheckGUI extends CampoGUI {
	
	private static final String LOG_TAG = CheckGUI.class.getSimpleName();

	private LinearLayout mainLayout;
	private CheckBox checkBox;

	// Constructs
	public CheckGUI(Context context) {
		super(context);
	}

	public CheckGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public CheckGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	// Getters y Setters
	public String getValorView() {
		return Boolean.toString(checkBox.isChecked());
	}

	// Metodos
	@Override
	public View build() {
		// Etiqueta
		this.mainLayout = new LinearLayout(context);
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);

		this.checkBox = new CheckBox(context);
		this.checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.checkBox.setEnabled(enabled);
		this.checkBox.setFocusable(enabled);
		this.checkBox.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.checkBox.setText(this.getEtiqueta());
		this.checkBox.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		this.checkBox.setButtonDrawable(R.drawable.checkbox_selector);
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			this.checkBox.setChecked(Boolean.valueOf(this.getInitialValues().get(0).getValor()));
		}else{
			this.checkBox.setChecked(Boolean.valueOf(this.getValorDefault()));
		}
		this.mainLayout.addView(checkBox);
		this.view = mainLayout;

		return this.view;
	}

	@Override
	public View redraw() {
		this.checkBox.setEnabled(enabled);
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

		Log.d(LOG_TAG, "save() : "+this.getTratamiento().getValue()+" :Campo: "+nombreCampo
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
			Log.d(LOG_TAG, String.format("2-TRUE: CV: %s", currentValue));
			dirty = true;
		}
		return dirty;
	}

	@Override
	public View getEditViewForCombo(){
		return this.mainLayout;
	}

	@Override
	public void removeAllViewsForMainLayout(){
		this.mainLayout.removeAllViews();
	}
	
	@Override
	public String getPrinterValor() {
		if (checkBox.isChecked()) {
			return getEtiqueta();
		}else{
			return "";
		}
		
	}
}
