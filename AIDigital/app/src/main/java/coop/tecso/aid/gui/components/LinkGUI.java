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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.gui.utils.Utils;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

/**
 * Tratamiento Link a Aplicaciones o Web pages.
 * @author tecso.coop
 *
 */
public class LinkGUI extends CampoGUI {
	
	private LinearLayout mainLayout;
	private LinearLayout buttonLayout;
	private Button button;

	// Constructs
	public LinkGUI(Context context) {
		super(context);
	}

	public LinkGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public LinkGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	// Getters y Setters
	public String getValorView() {
		return this.valorDefault;
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
		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);

		// Se arma el boton para disparar control se seleccion de fecha
		this.button = new Button(context);
		this.button.setText("Abrir");
		this.button.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if(valorDefault.contains("#")){
					String[] params = valorDefault.split("#");
					// Custom Application Launcher
					intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setComponent(new ComponentName(params[0], params[1]));
				}else{
					// Browser Launcher
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(valorDefault));
				}
				
				if(Utils.canHandleIntent(context, intent)){
					context.startActivity(intent);
					return;
				}
				
				// BugFix Error 22 – Acceso a Link incorrecto
				String err = "No se puede ejecutar el enlace: "
							 +valorDefault+"\nPor favor, contactese con el administrador.";
				final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle("Error al ejecutar enlace");
				alertDialog.setMessage(err);
				alertDialog.setCancelable(false);
				alertDialog.setIcon(R.drawable.ic_error_default);
				alertDialog.setButton(context.getString(R.string.accept), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						alertDialog.dismiss();
					}
				});
				alertDialog.show();
			}
		});
		this.button.setEnabled(enabled);

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

		Log.d(LinkGUI.class.getSimpleName(),"save() : "+this.getTratamiento()+" :Campo: "+nombreCampo
				+" idCampo: "+(campo!=null?campo.getId():"null")
				+", idCampoValor: "+(campoValor!=null?campoValor.getId():"null")
				+", idCampoValorOpcion: "+(campoValorOpcion!=null?campoValorOpcion.getId():"null")
				+", Valor: "+valor);

		Value data = new Value(campo,campoValor,campoValorOpcion,valor, null);
		this.values.add(data);

		return this.values;
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