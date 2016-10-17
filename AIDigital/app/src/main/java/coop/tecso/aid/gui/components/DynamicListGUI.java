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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.components.TextGUI.Teclado;
import coop.tecso.aid.gui.helpers.Tratamiento;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.helpers.GUIHelper;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.CampoValor;

/**
 * 
 * @author tecso.coop
 *
 */
public class DynamicListGUI extends CampoGUI {

	private String etiqueta;
	private ImageButton btnAdd;
	private LinearLayout mainLayout;
	private LinearLayout titleLayout;
	private LinearLayout componentLayout;

	private List<ImageButton> listBtnRemove; 
	private List<CampoGUI> elements;

	private int maxItem;

	public DynamicListGUI(Context context) {
		super(context);
		this.elements = new ArrayList<CampoGUI>();
		this.listBtnRemove = new ArrayList<ImageButton>();
	}

	public DynamicListGUI(Context context,List<Value> values) {
		super(context,values);
		this.elements = new ArrayList<CampoGUI>();
		this.listBtnRemove = new ArrayList<ImageButton>();
	}

	public DynamicListGUI(Context context, boolean enabled) {
		super(context, enabled);
		this.elements = new ArrayList<CampoGUI>();
		this.listBtnRemove = new ArrayList<ImageButton>();
	}

	// Getters y Setters
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	// Metodos
	@Override
	public View build() {
		// Se define un layout lineal vertical para armar el campo desplegable
		this.mainLayout = new LinearLayout(context);
		this.mainLayout.setOrientation(LinearLayout.VERTICAL);
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Titulo: Se define un layout lineal horizonal para mostrar la etiqueta del campo y el boton para retraer/expandir
		this.titleLayout = new LinearLayout(context);
		this.titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.titleLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.titleLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		try {
			maxItem = Integer.valueOf(this.getMascaraVisual()).intValue();	
		} catch (Exception e) {
			maxItem = 5;
		}

		// 	 Etiqueta
		this.label = new TextView(context);
		this.label.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta());
		this.label.setGravity(Gravity.CENTER_VERTICAL);
		this.label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		// Boton para agregar elemento a la lista
		this.btnAdd = new ImageButton(context);
		this.btnAdd.setBackgroundResource(R.drawable.ic_menu_add_blue2);  
		this.btnAdd.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.btnAdd.setEnabled(enabled);
		this.btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(maxItem <= elements.size()){
					GUIHelper.showError(context, String.format("No puede agregar más '%s'",getEtiqueta()));
					return;
				}
				addItem(null);
			}
		});
		// Se crea layout para alinear icono a la derecha
		LinearLayout btnAddLayout = new LinearLayout(context);
		btnAddLayout.setGravity(Gravity.RIGHT);
		btnAddLayout.addView(this.btnAdd);
		btnAddLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.titleLayout.addView(this.label);
		this.titleLayout.addView(btnAddLayout);

		// Se define un layout de tipo Linear para contener los distintos Campos
		this.componentLayout = new LinearLayout(context);
		this.componentLayout.setOrientation(LinearLayout.VERTICAL);
		this.componentLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));

		// Se recorren los valores precargados y por cada uno se agrega un elemento
		if(this.getInitialValues() != null){
			for(Value initialValue: getInitialValues()){
				addItem(initialValue);
			}
		}
		// Espacio separador
		View gap = new View(context);
		gap.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));

		// Se agregan partes del componente al layout contenedor
		this.mainLayout.addView(titleLayout);
		this.mainLayout.addView(gap, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4));
		this.mainLayout.addView(this.componentLayout);

		this.view = this.mainLayout;

		return this.view;
	}

	@Override
	public View redraw() {
		this.btnAdd.setEnabled(enabled);
		for(CampoGUI element: this.elements){
			if(this.enabled)
				element.enable();
			else
				element.disable();
		}

		for (ImageButton btn : this.listBtnRemove) {
			btn.setEnabled(this.enabled);
		}
		return this.view;
	}

	@Override
	public List<Value> values() {
		this.values = new ArrayList<Value>();
		for(CampoGUI campo: this.elements){
			values.addAll(campo.values());
		}
		return this.values;
	}

	@Override
	public boolean isDirty(){
		for(CampoGUI campo: this.elements){
			if(campo.isDirty())	return true;
		}
		return false;
	}

	@Override
	public boolean validate() {
		// 
		if(isObligatorio() && this.elements.size() < 1){
//			GUIHelper.showError(context, context.getString(R.string.field_required, getEtiqueta()));
			label.setError(context.getString(R.string.field_required, getEtiqueta()));
			btnAdd.requestFocus();
			return false;
		}
		label.setError(null);
		//
		for(Component co: this.elements){
			if(co.validate() == false){
				return false;
			}
		}
		return true;
	}

	@Override
	public View disable() {
		super.disable();
		if(this.components != null){
			for(Component co: this.components){
				co.disable();
			}
		}

		return this.view;
	}

	/**
	 * Elimina un item de la lista
	 * 
	 * @param view
	 */
	private void removeItem(View btnRemove){

		// Se busca el indice del componente en la lista
		int index = this.listBtnRemove.indexOf(btnRemove);

		// Con el indice se elimina el elemento en la misma posicion en la lista de elementos
		this.elements.remove(index);
		this.listBtnRemove.remove(index);

		// Con el indice se elimina el view del componente de la tabla de visualizacion
		this.componentLayout.removeViewAt(index);
	}

	/**
	 * Agrega un item a la lista. Para esto crea un nuevo comopente a partir del CampoValor detalle.
	 * 
	 * @param initialValue
	 */
	private void addItem(Value initialValue) {
		// Identificamos el CampoValor de detalle (solo debe existir uno)
		AplPerfilSeccionCampoValor perfilSeccionCampoValor = null; 
		CampoGUI campo = null; 
		if(this.components != null && this.components.size() > 0){
			campo = (CampoGUI) this.components.get(0);
			if(campo != null && campo.getEntity() instanceof AplPerfilSeccionCampoValor){
				perfilSeccionCampoValor = (AplPerfilSeccionCampoValor) campo.getEntity();
			}
		}
		if(perfilSeccionCampoValor == null){
			return;
		}
		
		// Existe un Campo agregado y no se modificó
		if(elements.size() > 0){
			for(Component co: this.elements){
				if(co.validate() == false) return;
			}
		}

		// Creamos nuevo elemento
		final CampoValor campoValor = perfilSeccionCampoValor.getCampoValor();
		Tratamiento tratamiento = Tratamiento.getById(campoValor.getTratamiento());
		CampoGUI newElement = null;
		switch (tratamiento) {
		case TA: // Alfanumerico 
			newElement = new TextGUI(context, Teclado.ALFANUMERICO, false, enabled);
			break;
		case TAM: // Alfanumerico Multilinea
			newElement = new TextGUI(context,Teclado.ALFANUMERICO, true, enabled);
			break;
		case TNE: // Entero
			newElement = new TextGUI(context,Teclado.NUMERICO, false, enabled);
			break;
		case TND: // Decimal
			newElement = new TextGUI(context,Teclado.DECIMAL, false, enabled);
			break;
		case SBX:  // SearchBox
			newElement = new SearchBoxGUI(context, enabled);
			break;	
		case TF:  // Fecha
			newElement = new DateGUI(context, enabled);
			break;
		case TH:  // Hora
			newElement = new TimeGUI(context, enabled);
			break;
		case PIC:  // Adjuntar Imagen
			newElement = new PhotoGUI(context, enabled);
			break;
		default:
			// Tratamiento no implementado
//			newElement = new CampoGUI(context);
			break;
		}
		newElement.setPerfilGUI(perfilGUI);
		newElement.setEntity(perfilSeccionCampoValor);
		newElement.setEtiqueta(campoValor.getEtiqueta());
		newElement.setValorDefault(campoValor.getValorDefault());
		newElement.setTratamiento(Tratamiento.getById(campoValor.getTratamiento()));
		newElement.setTablaBusqueda(campo.getTablaBusqueda());
		newElement.setObligatorio(campoValor.getObligatorio() == 1);
		newElement.setComponents(new ArrayList<Component>());
		// Carga de valor inicial (precargado)
		if(initialValue != null){
			List<Value> initialValues = new ArrayList<Value>();
			initialValues.add(initialValue);
			newElement.setInitialValues(initialValues);
		}
		newElement.build();
		
		// Se crea el boton para eliminar el elemento
		ImageButton btnRemove = new ImageButton(context);
		btnRemove.setBackgroundResource(R.drawable.ic_menu_remove);
		btnRemove.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		btnRemove.setEnabled(enabled);
		btnRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

				builder.setTitle(R.string.confirm_title)
				.setMessage(v.getContext().getString(R.string.delete_item_confirm_msg, campoValor.getEtiqueta()))
				.setCancelable(false)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Elimina el item
						removeItem(v);
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		// Agregamos nuevo elemento a lista
		this.elements.add(newElement);
		// Se agrega el boton de eliminacion asociado al nuevo elemento a una lista (esto permite obtener el indice del elemento en la lista)
		this.listBtnRemove.add(btnRemove);

		// Layour para centrar el boton eliminar
		LinearLayout btnRemoveLayout = new LinearLayout(context);
		btnRemoveLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		btnRemoveLayout.setGravity(Gravity.CENTER);
		btnRemoveLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		btnRemoveLayout.addView(btnRemove);

		// Layout contenedor del elemento mas el boton eliminar
		LinearLayout container = new LinearLayout(context);
		container.setOrientation(LinearLayout.HORIZONTAL);
		container.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		container.addView(btnRemoveLayout);
		container.addView(newElement.getView(), new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Se agrega elemento al layout de componentes
		this.componentLayout.addView(container);
	}

	@Override
	public String getPrinterValor() {
		StringBuilder builder = new StringBuilder();
		builder.append(getEtiqueta());
		builder.append(":<br>");
		//
		boolean hasData = false;
		for (CampoGUI element : elements) {
			String value = element.getPrinterValor();
			if(!TextUtils.isEmpty(value)){
				hasData = true;
				builder.append(value);
				builder.append("<br>");
			}
		}
		return hasData?builder.toString():"";
	}

	@Override
	public String getValorView() {
		// TODO Auto-generated method stub
		return null;
	}
}