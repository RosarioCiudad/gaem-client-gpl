package coop.tecso.aid.gui.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.gui.helpers.FilaTablaBusqueda;
import coop.tecso.aid.gui.helpers.Tratamiento;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.gui.utils.EvalEvent;
import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

public class CampoGUI extends Component {

	protected PerfilGUI perfilGUI;
	protected Tratamiento tratamiento;
	protected String etiqueta;
	protected String valorDefault = "";
	protected boolean soloLectura;
	protected boolean obligatorio;
	protected List<AbstractEntity> options;
	protected List<FilaTablaBusqueda> tablaBusqueda;
	protected List<Value> values;
	protected TextView label;
	protected List<Value> initialValues;
	protected String mascaraVisual;
	protected AIDigitalApplication appState; 
	
	// Constructores
	public CampoGUI(Context context) {
		super();
		this.context = context;
		this.appState = (AIDigitalApplication) context.getApplicationContext();
	}

	public CampoGUI(Context context, boolean enabled) {
		super();
		this.context = context;
		this.enabled = enabled;
		this.appState = (AIDigitalApplication) context.getApplicationContext();
	}

	public CampoGUI(Context context, List<Value> values) {
		super();
		this.context = context;
		this.values = values;
		this.appState = (AIDigitalApplication) context.getApplicationContext();
	}

	// Getters y Setters
	public PerfilGUI getPerfilGUI() {
		return perfilGUI;
	}
	public void setPerfilGUI(PerfilGUI perfilGUI) {
		this.perfilGUI = perfilGUI;
	}
	public Tratamiento getTratamiento() {
		return tratamiento;
	}
	public void setTratamiento(Tratamiento tratamiento) {
		this.tratamiento = tratamiento;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public boolean isSoloLectura() {
		return soloLectura;
	}
	public void setSoloLectura(boolean soloLectura) {
		this.soloLectura = soloLectura;
	}
	public boolean isObligatorio() {
		return obligatorio;
	}
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}
	public String getValorDefault() {
		return valorDefault;
	}
	public void setValorDefault(String valorDefault) {
		this.valorDefault = valorDefault;
	}
	public List<FilaTablaBusqueda> getTablaBusqueda() {
		return tablaBusqueda;
	}
	public void setTablaBusqueda(List<FilaTablaBusqueda> tablaBusqueda) {
		this.tablaBusqueda = tablaBusqueda;
	}	
	public List<AbstractEntity> getOptions() {
		return options;
	}
	public void setOptions(List<AbstractEntity> options) {
		this.options = options;
	}
	public List<Value> getInitialValues() {
		return initialValues;
	}
	public void setInitialValues(List<Value> initialValues) {
		this.initialValues = initialValues;
	}
	public TextView getLabel() {
		return label;
	}
	public String getMascaraVisual() {
		return mascaraVisual;
	}
	public void setMascaraVisual(String mascaraVisual) {
		this.mascaraVisual = mascaraVisual;
	}

	// Metodos
	@Override
	public void addValue(Value value) {
		this.values.add(value);
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
		// Valor a guardar
		String valor = getValorView();
		String msg = String.format("save: %s - Campo: %s - CampoValor: %s CampoValorOpcion: %s - Valor: %s",
				getClass().getSimpleName(),
				campo.getCampo().getEtiqueta(),
				campoValor==null?"null":campoValor.getCampoValor().getEtiqueta(),
				campoValorOpcion==null?"null":campoValorOpcion.getCampoValorOpcion().getEtiqueta(),
				valor);
		
		Log.d(getClass().getSimpleName(), msg);

		Value data = new Value(campo, campoValor, campoValorOpcion, valor);
		this.values.add(data);
		
		return this.values;
	}
	
	//Sobreescribir 
	public String getValorView(){
		return "";
	}

	public List<Value> dirtyValues() {
		if(this.isDirty())
			return this.values();
		else
			return new ArrayList<Value>();
	}

	@Override
	public View build() {
		// View generico para campos de tratamientos no implementados
		TableRow layout = new TableRow(context);
		layout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		layout.setGravity(Gravity.CENTER_VERTICAL);

		TextView label = new TextView(context);
		label.setTextColor(context.getResources().getColor(R.color.label_text_color));//this.DEFAULT_LABEL_TEXT_COLOR);
		label.setText(this.getEtiqueta()+": ");
		label.setGravity(Gravity.RIGHT);

		TextView msg = new TextView(context);
		msg.setTextColor(context.getResources().getColor(R.color.label_text_color));//this.DEFAULT_LABEL_TEXT_COLOR);
		msg.setText("Tratamiento '"+this.getTratamiento()+"' no implementado.");
		msg.setGravity(Gravity.CENTER);

		layout.addView(label);
		layout.addView(msg);

		this.view = layout; 
		return this.view;
	}

	@Override
	public View redraw() {
		ViewGroup viewGroup = (ViewGroup) this.view;
		disableEnableView(viewGroup);
		return this.view;
	}
	
	/**
	 * 
	 * @param viewGroup
	 */
	private void disableEnableView(ViewGroup viewGroup){
		for (int i = 0; i < viewGroup.getChildCount(); i++){
			View child = viewGroup.getChildAt(i);
			child.setEnabled(enabled);
			child.setFocusable(enabled);
			if (child instanceof ViewGroup){ 
				disableEnableView((ViewGroup)child);
			}
		}
	} 

	@Override
	public String toString(){
		return this.getEtiqueta();
	}

	public View getEditViewForCombo(){
		return null;
	}

	public void removeAllViewsForMainLayout(){
		// Do nothing
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}