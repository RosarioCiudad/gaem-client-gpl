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

package coop.tecso.aid.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import coop.tecso.aid.gui.components.CampoGUI;
import coop.tecso.aid.gui.components.CheckGUI;
import coop.tecso.aid.gui.components.ComboExtGUI;
import coop.tecso.aid.gui.components.ComboGUI;
import coop.tecso.aid.gui.components.Component;
import coop.tecso.aid.gui.components.DateGUI;
import coop.tecso.aid.gui.components.DocumentoGUI;
import coop.tecso.aid.gui.components.DomicilioExtGUI;
import coop.tecso.aid.gui.components.DomicilioGUI;
import coop.tecso.aid.gui.components.DominioGUI;
import coop.tecso.aid.gui.components.DynamicListGUI;
import coop.tecso.aid.gui.components.LabelGUI;
import coop.tecso.aid.gui.components.PerfilGUI;
import coop.tecso.aid.gui.components.SearchBoxGUI;
import coop.tecso.aid.gui.components.SeccionGUI;
import coop.tecso.aid.gui.components.SectionCheckGUI;
import coop.tecso.aid.gui.components.TextGUI;
import coop.tecso.aid.gui.components.TextGUI.Teclado;
import coop.tecso.aid.gui.components.TimeGUI;
import coop.tecso.aid.gui.helpers.Tratamiento;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.gui.utils.Utils;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfil;
import coop.tecso.daa.domain.aplicacion.AplicacionPerfilSeccion;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;
import coop.tecso.daa.domain.perfil.Campo;
import coop.tecso.daa.domain.perfil.CampoValor;
import coop.tecso.daa.domain.perfil.CampoValorOpcion;
import coop.tecso.daa.domain.perfil.PerfilVisitor;
import coop.tecso.daa.domain.perfil.Seccion;

public class GUIVisitor implements PerfilVisitor {

	private final String TAG = getClass().getSimpleName();
	
	private Context context;
	private Component component;
	
	private Map<String,List<Value>> mapInitialValues;
	private List<Component> seccionList = new ArrayList<Component>();
	private List<Component> campoList = new ArrayList<Component>();
	private List<Component> valorList = new ArrayList<Component>();
	private List<Component> opcionList = new ArrayList<Component>();

	private boolean enabled = true;
	private boolean haveInitialValue = false;
	
	public GUIVisitor(Context context) {
		this.context = context;
	}
	
	public GUIVisitor(Context context, boolean enabled) {
		this.context = context;
		this.enabled = enabled;
	}
	
	/**
	 * 
	 * @param perfil
	 * @return
	 */
	public Component buildComponents(AplicacionPerfil perfil) {
		this.component = new PerfilGUI(context, enabled);
		perfil.accept(this);
		return this.component;
	}

	/**
	 * 
	 * @param perfil
	 * @return
	 */
	public Component buildComponents(AplicacionPerfil perfil, List<Value> listValue) {
		this.mapInitialValues = Utils.fillInitialValuesMaps(listValue);
		this.haveInitialValue = true;
		this.component = new PerfilGUI(context, enabled);
		perfil.accept(this);
		return this.component;
	}
	
	/**
	 * 
	 * @param perfil
	 * @return
	 */
	public Component buildComponents(AplicacionPerfil perfil, List<Value> listValue, boolean enabled) {
		this.mapInitialValues = Utils.fillInitialValuesMaps(listValue);
		this.haveInitialValue = true;
		this.enabled = enabled;
		this.component = new PerfilGUI(context, enabled);
		perfil.accept(this);
		return component;
	}
	
	@Override
	public void visit(AplicacionPerfil perfil) {
		Log.d(TAG, "visit: Perfil");
		this.component.setEntity(perfil);
		this.component.setComponents(this.seccionList);
		this.component.build();
		
		this.seccionList = new ArrayList<Component>();
	}

	@Override
	public void visit(AplicacionPerfilSeccion perfilSeccion) {
		Log.d(TAG, "visit: Seccion "+perfilSeccion.getSeccion().getDescripcion());
		Seccion seccion = perfilSeccion.getSeccion();
		SeccionGUI component = new SeccionGUI(context, enabled);
		
		component.setPerfilGUI((PerfilGUI) this.component);
		component.setEntity(perfilSeccion);
		component.setEtiqueta(seccion.getDescripcion());
		component.setNoDesplegar(perfilSeccion.getNoDesplegar() == 1);
		component.setOpcional(perfilSeccion.getOpcional() == 1);
		component.setComponents(this.campoList);
		component.build();
		
		this.seccionList.add(component);
		this.campoList = new ArrayList<Component>();
	}

	@Override
	public void visit(AplPerfilSeccionCampo perfilSeccionCampo) {
		Log.d(TAG, "visit: Campo");
		boolean enabledComponent = enabled && perfilSeccionCampo.getSoloLectura() == 0;
		Campo campo = perfilSeccionCampo.getCampo();
		Log.d(TAG, "visitEtiqueta: perfilSeccionCampo.id="+perfilSeccionCampo.getId());
		Log.d(TAG, "visitEtiqueta: "+campo.getEtiqueta());
		Tratamiento tratamiento = Tratamiento.getById(campo.getTratamiento());
		Log.d(TAG, "visitEtiqueta: control="+tratamiento.getValue());
		CampoGUI component = null; 
		switch (tratamiento) {
		case TA: // Alfanumerico 
			component = new TextGUI(context, Teclado.ALFANUMERICO, false, enabledComponent);
//			component = new LicenciaGUI(context, enabledComponent);
			break;
		case TAM: // Alfanumerico Multilinea
			component = new TextGUI(context,Teclado.ALFANUMERICO, true, enabledComponent);
			break;
		case TNE: // Entero
			component = new TextGUI(context,Teclado.NUMERICO, false, enabledComponent);
			break;
		case TND: // Decimal
			component = new TextGUI(context,Teclado.DECIMAL, false, enabledComponent);
			break;
		case TF:  // Fecha
			component = new DateGUI(context, enabledComponent);
			break;
		case TH:  // Hora
			component = new TimeGUI(context, enabledComponent);
			break;
		case OP:  // Opciones simple seleccion (Combo)
			component = new ComboGUI(context, enabledComponent);
			break;
		case CHK:  // Checkbox
			component = new CheckGUI(context, enabledComponent);
			break;	
		case SBX:  // SearchBox
			component = new SearchBoxGUI(context, enabledComponent);
			break;	
		case LD:  // Lista de Campos Dinamica
			component = new DynamicListGUI(context, enabledComponent);
			break;
		case OP2:  // Opcion simple extendida (Combo)
			component = new ComboExtGUI(context, enabledComponent);
			break;
		case OP3:  // Opcion simple extendida: Opcion Otra
			component = new ComboExtGUI(context, enabledComponent, true);
			break;
		case DOMI:  // Domicilio
			component = new DomicilioGUI(context, enabledComponent);
			break;
		case DOMC:  // Domicilio
			component = new DomicilioExtGUI(context, enabledComponent);
			break;
		case DOM:  // Dominio
			component = new DominioGUI(context, enabledComponent);
			break;
		case DOC:  // Documento
			component = new DocumentoGUI(context, enabledComponent);
			break;
		case SO: // Secciones Opcionales (SectionsCheckList)
			Log.d("TAG", "Entro a SO de GUIVisitor");
//			component = new SectionsCheckListGUI(context, enabledComponent);
			component = new SectionCheckGUI(context, enabledComponent);
			break;
			
		default:
			// Tratamiento no implementado
			component = new CampoGUI(context);
			break;
		}

		component.setPerfilGUI((PerfilGUI) this.component);
		component.setEntity(perfilSeccionCampo);
		component.setEtiqueta(campo.getEtiqueta());
		component.setMascaraVisual(campo.getMascaraVisual());
		component.setObligatorio(campo.getObligatorio() == 1);
		component.setSoloLectura(campo.getSoloLectura() == 1);
		component.setValorDefault(campo.getValorDefault());
		component.setTratamiento(tratamiento);
		component.setComponents(this.valorList);
		// Carga de valor inicial (precargado)
		if(haveInitialValue){
			StringBuilder key = new StringBuilder();
			key.append(perfilSeccionCampo.getId());
			key.append("|0|0");
			component.setInitialValues(mapInitialValues.get(key.toString()));
		}
		component.build();
		this.campoList.add(component);
		this.valorList = new ArrayList<Component>();
	}

	@Override
	public void visit(AplPerfilSeccionCampoValor perfilSeccionCampoValor) {
		Log.d(TAG, "visit: Valor");
		boolean enabledComponent = enabled && perfilSeccionCampoValor.getAplPerfilSeccionCampo().getSoloLectura() == 0;
		CampoValor campoValor = perfilSeccionCampoValor.getCampoValor();
		Tratamiento tratamiento = Tratamiento.getById(campoValor.getTratamiento());
		CampoGUI component = null;
		switch (tratamiento) {
		case NA:  // Etiqueta (Se utiliza para Opciones seleccionadas) 
			component = new LabelGUI(context, enabled);
			break;
		case TA: // Alfanumerico 
			component = new TextGUI(context, Teclado.ALFANUMERICO, false, enabledComponent);
			break;
		case TAM: // Alfanumerico Multilinea
			component = new TextGUI(context,Teclado.ALFANUMERICO, true, enabledComponent);
			break;
		case TNE: // Entero
			component = new TextGUI(context,Teclado.NUMERICO, false, enabledComponent);
			break;
		case TND: // Decimal
			component = new TextGUI(context,Teclado.DECIMAL, false, enabledComponent);
			break;
		case SBX:  // SearchBox
			component = new SearchBoxGUI(context, enabledComponent);
			break;	
		case TF:  // Fecha
			component = new DateGUI(context, enabledComponent);
			break;
		case TH:  // Hora
			component = new TimeGUI(context, enabledComponent);
			break;
		case OP:  // Opciones simple seleccion (Combo)
			component = new ComboGUI(context, enabledComponent);
			break;
		default:
			// Tratamiento no implementado
			component = new CampoGUI(context);
			break;
		}
		
		component.setPerfilGUI((PerfilGUI) this.component);
		component.setEntity(perfilSeccionCampoValor);
		component.setEtiqueta(campoValor.getEtiqueta());
		component.setObligatorio(campoValor.getObligatorio() == 1);
		component.setValorDefault(campoValor.getValorDefault());
		component.setMascaraVisual(campoValor.getMascaraVisual());
		component.setTratamiento(Tratamiento.getById(campoValor.getTratamiento()));
		component.setComponents(this.opcionList);
		// Carga de valor inicial (precargado)
		if(haveInitialValue){
			StringBuilder key = new StringBuilder();
			key.append(perfilSeccionCampoValor.getAplPerfilSeccionCampo().getId());
			key.append("|");
			key.append(perfilSeccionCampoValor.getId());
			key.append("|0");
			component.setInitialValues(mapInitialValues.get(key.toString()));
		}
				
		component.build();
		this.valorList.add(component);
		this.opcionList = new ArrayList<Component>();
	}

	@Override
	public void visit(AplPerfilSeccionCampoValorOpcion perfilSeccionCampoValorOpcion) {
		Log.d(TAG, "visit: Opcion");
		boolean enabledComponent = enabled && 
				perfilSeccionCampoValorOpcion.getAplPerfilSeccionCampoValor().getAplPerfilSeccionCampo().getSoloLectura() == 0;
		CampoValorOpcion campoValorOpcion = perfilSeccionCampoValorOpcion.getCampoValorOpcion();
		Tratamiento tratamiento = Tratamiento.getById(campoValorOpcion.getTratamiento());
		CampoGUI component = null;
		switch (tratamiento) {
		case NA:  // Etiqueta (Se utiliza para Opciones seleccionadas) 
			component = new LabelGUI(context, enabled);
			break;
		case TA: // Alfanumerico 
			component = new TextGUI(context, Teclado.ALFANUMERICO, false, enabledComponent);
			break;
		case TAM: // Alfanumerico Multilinea
			component = new TextGUI(context,Teclado.ALFANUMERICO, true, enabledComponent);
			break;
		case TNE: // Entero
			component = new TextGUI(context,Teclado.NUMERICO, false, enabledComponent);
			break;
		case TND: // Decimal
			component = new TextGUI(context,Teclado.DECIMAL, false, enabledComponent);
			break;
		case TF:  // Fecha
			component = new DateGUI(context, enabledComponent);
			break;
		case TH:  // Hora
			component = new TimeGUI(context, enabledComponent);
			break;
		default:
			// Tratamiento no implementado
			component = new CampoGUI(context);
			break;
		}
		
		component.setPerfilGUI((PerfilGUI) this.component);
		component.setEntity(perfilSeccionCampoValorOpcion);
		component.setEtiqueta(campoValorOpcion.getEtiqueta());
		component.setValorDefault(campoValorOpcion.getValorDefault());
		component.setMascaraVisual(campoValorOpcion.getMascaraVisual());
		component.setObligatorio(campoValorOpcion.getObligatorio() == 1);
		component.setTratamiento(Tratamiento.getById(campoValorOpcion.getTratamiento()));
		component.setComponents(new ArrayList<Component>());
		// Carga de valor inicial (precargado)
		if(haveInitialValue){
			StringBuilder key = new StringBuilder();
			key.append(perfilSeccionCampoValorOpcion.getAplPerfilSeccionCampoValor().getAplPerfilSeccionCampo().getId());
			key.append("|");
			key.append(perfilSeccionCampoValorOpcion.getAplPerfilSeccionCampoValor().getId());
			key.append("|");
			key.append(perfilSeccionCampoValorOpcion.getId());
			component.setInitialValues(mapInitialValues.get(key.toString()));
		}
		component.build();
		this.opcionList.add(component);
	}
}