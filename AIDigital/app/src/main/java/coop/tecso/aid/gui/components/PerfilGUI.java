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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import coop.tecso.aid.R;
import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.helpers.ParamHelper;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;

public class PerfilGUI extends Component {


	private LinearLayout layout;
	private CampoGUI campoGUISelected;
	
	public CampoGUI getCampoGUISelected() {
		return campoGUISelected;
	}

	public void setCampoGUISelected(CampoGUI campoGUI) {
		this.campoGUISelected = campoGUI;
	}

	// Constructores
	public PerfilGUI(Context context) {
		super();
		this.context = context;
	}

	public PerfilGUI(Context context, boolean enabled) {
		super();
		this.context = context;
		this.enabled = enabled;
	}

	// Metodos
	@Override
	public void addValue(Value value) {
		return;
	}

	@Override
	public List<Value> values() {
		List<Value> values = new ArrayList<Value>();
		if(this.components != null){
			for(Component co: this.components){
				SeccionGUI seccion = (SeccionGUI) co;
				if(seccion.isVisible()){
					values.addAll(seccion.values());
				}
			}
		}
		return values;
	}

	public List<Value> dirtyValues() {
		List<Value> values = new ArrayList<Value>();
		for(Component co: this.components){
			SeccionGUI seccion = (SeccionGUI) co;
			if(seccion.isVisible()){
				values.addAll(seccion.dirtyValues());
			}
		}
		return values;
	}

	@Override
	public boolean isDirty(){
		for(Component co: this.components){
			if(co.isDirty()) return true;
		}
		return false;
	}

	@Override
	public boolean validate() {
		for(Component co: this.components){
			SeccionGUI seccion = (SeccionGUI) co;
			if(seccion.isVisible() && !seccion.validate()){
				return false;
			}
		}
		return true;
	}

	@Override
	public View build() {
		// Se define un layout lineal vertica para ubicar: 'Label EditText'
		this.layout = new LinearLayout(context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		this.layout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Se agregan los componentes view correspondientes a las secciones al layout contenedor
		if(this.components != null){
			for(Component co: this.components){
				this.layout.addView(co.getView());
			}
		}

		this.view = this.layout; 
		
		if(!enabled){
			redraw();
		}
		return this.view;
	}

	@Override
	public View redraw() {
		if(this.components != null){
			for(Component co: this.components){
				co.redraw();
			}
		}
		return this.view;
	}

	@Override
	public View disable() {
		if(this.components != null){
			for(Component co: this.components){
				co.disable();
			}
		}
		return this.view;
	}
	
	@Override
	public String getPrinterValor() {
		StringBuilder builder = new StringBuilder();
		Formulario formulario = (Formulario) entity;

		builder.append("<b>ACTA DE COMPROBACIÓN</b>");
		builder.append("       ");
		builder.append(String.format("<b>Número: </b> %s",formulario.getNumero()));
		builder.append("<br>");
		builder.append(String.format("<b>%s</b>", formulario.getTipoFormulario().getDescripcion()));
		builder.append("<br>");
		builder.append("<b>Fecha: </b>"+ new SimpleDateFormat("dd/MM/yyyy").format(formulario.getFechaInicio()));
		builder.append("                   ");
		builder.append("<b>Hora: </b>"+ new SimpleDateFormat("HH:mm").format(formulario.getFechaInicio()));
		builder.append("<br>");
		for(Component component: this.components){
			builder.append(component.getPrinterValor());
		}
		builder.append("<br>");
		builder.append("______________________________________________");
		builder.append("<br>");
		
		return builder.toString();
	}
	
	/**
	 * 
	 * @param addFooter
	 * @return
	 */
	public String getPrinterValor(boolean addFooter) {
		
		// Agregar pie?
		if(!addFooter){
			// Caso normal
			return getPrinterValor();
		}
		
		StringBuilder builder = new StringBuilder();
		//
		builder.append("<h1>MUNICIPALIDAD DE ROSARIO</h1><br>");
		builder.append("<center><b>DIRECCIÓN GENERAL DE TRÁNSITO</b></center><br><br>");
		// Informacion del Acta
		builder.append(getPrinterValor());
		
		// Pie con firmas
		builder.append("<br><br><br>");
		builder.append("..............................................");
		builder.append("                 ");
		builder.append("Firma del Imputado");
		builder.append("<br><br><br>");
		builder.append("..............................................");
		builder.append("                 ");
		builder.append("Firma del Testigo 1");
		builder.append("<br><br><br>");
		builder.append("..............................................");
		builder.append("                 ");
		builder.append("Firma del Testigo 2");
		builder.append("<br><br><br>");
		builder.append("..............................................");
		builder.append("                 ");
		builder.append("Firma Conductor Cedido");
		builder.append("<br><br><br><br><br>");
		builder.append("..............................................");
		builder.append("            ");
		builder.append("Firma del Inspector Actuante");
		builder.append("<br><br><br><br><br><br>");
		
		return builder.toString();
	}
	
	/**
	 * Muestra la seccion oculta para el idSeccion indicado
	 * 
	 * @return
	 */
	public void mostrarSeccion(int idSeccion) {
		Log.d("Check", "entro a mostrarSeccion " + idSeccion);
		if (this.components != null) {
			for (Component co : this.components) {
				SeccionGUI seccion = (SeccionGUI) co;
				if (seccion.isOpcional()) {
					AplicacionPerfilSeccion aplPerfilSeccion = (AplicacionPerfilSeccion) seccion.getEntity();
					if (aplPerfilSeccion.getSeccion().getId() == idSeccion)
						seccion.mostrar();
				}
			}
		}
	}
	
	/**
	 * Oculta la seccion para el idSeccion indicado
	 * 
	 * @return
	 */
	public void ocultarSeccion(int idSeccion) {
		if (this.components != null) {
			for (Component co : this.components) {
				SeccionGUI seccion = (SeccionGUI) co;
				if (seccion.isOpcional()) {
					AplicacionPerfilSeccion aplPerfilSeccion = (AplicacionPerfilSeccion) seccion.getEntity();
					if (aplPerfilSeccion.getSeccion().getId() == idSeccion) {
						seccion.ocultar();
					}
				}
			}
		}
	}
	
	/**
	 * Retorna el valor del campo con ID pasado como parametro.
	 * @return
	 */
	public Value getValorForCampoID(int campoID){
		if(null ==  this.components) return null;

		for(Component seccion: this.components){
			for (Component campo : seccion.getComponents()) {
				AbstractEntity entity = campo.getEntity();
				if(campoID == entity.getId()){
					if(entity instanceof AplPerfilSeccionCampo){
						if(campo.values().size() > 0) {
							return campo.values().get(0);
						} else {
							return null;
						}
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * Colapsa todas las secciones dentro del formulario
	 */
	public void colapseAll() {
		if(this.components != null){
			for(Component co: this.components){
				SeccionGUI seccion = (SeccionGUI) co;
				if(seccion.isVisible()){
					seccion.contract();
				}
			}
		}
	}
	
}