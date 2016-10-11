package coop.tecso.aid.gui.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;

public class ListGUI extends CampoGUI {

	private String etiqueta;
	private LinearLayout layout;
	private LinearLayout titleLayout;
	private TableLayout tableLayout; 

	public ListGUI(Context context) {
		super(context);
	}

	public ListGUI(Context context,List<Value> values) {
		super(context,values);
	}

	public ListGUI(Context context, boolean enabled) {
		super(context, enabled);
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
		this.layout = new LinearLayout(context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		this.layout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.layout.setGravity(Gravity.RIGHT);

		// Titulo: Se define un layout lineal horizonal para mostrar la etiqueta del campo y el boton para retraer/expandir
		this.titleLayout = new LinearLayout(context);
		this.titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.titleLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.titleLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// 	 Etiqueta
		this.label = new TextView(context);
		this.label.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta());
		this.label.setGravity(Gravity.CENTER_VERTICAL);
		this.label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		//		this.titleLayout.addView(this.btnExpandible);
		this.titleLayout.addView(this.label);

		// Se define un layout de tipo Tabla para contener los distintos Campos
		this.tableLayout = new TableLayout(context);
		this.tableLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.tableLayout.setPadding(10, 1, 0, 1);
		this.tableLayout.setColumnShrinkable(0, true);
		this.tableLayout.setColumnStretchable(1, true);

		// Se agregan los componentes correspondientes a los camposValor al layout contenedor
		if(this.components != null){
			int size = this.components.size();
			for(Component co: this.components){
				this.tableLayout.addView(co.getView()); //(TableRow) 

				// Separador de campos
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.line_background_color));

				if(--size > 0)
					this.tableLayout.addView(line, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
			}
		}

		// Espacio separador
		View gap = new View(context);
		gap.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));

		// Se agregan partes del componente al layout contenedor
		this.layout.addView(titleLayout);
		this.layout.addView(gap, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4));
		this.layout.addView(this.tableLayout);

		this.view = this.layout;

		return this.view;
	}

	@Override
	public View redraw() {
		for(Component co: this.components){
			CampoGUI campo = (CampoGUI) co;
			campo.redraw();
		}
		return this.view;
	}

	@Override
	public List<Value> values() {
		List<Value> values = new ArrayList<Value>();
		for(Component co: this.components){
			CampoGUI campo = (CampoGUI) co;
			values.addAll(campo.values());
		}
		return values;
	}

	@Override
	public boolean isDirty(){
		if(!super.isDirty()){
			// Recorremos los elementos de la lista verificando si alguno fue modificado
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				if(campo.isDirty()){
					return true;
				}
			}
		}

		return super.isDirty();
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

	@Override
	public String getValorView() {
		// TODO Auto-generated method stub
		return null;
	}

	//	/**
	//	 * Expande la seccion
	//	 * 
	//	 */
	//	protected void expand() {
	//		if(this.expanded) return;
	//
	//		this.layout.addView(this.tableLayout);
	//		this.btnExpandible.setBackgroundResource(R.drawable.ic_menu_contraer);
	//		this.expanded = true;
	//	}
	//
	//	/**
	//	 * Contrae la seccion 
	//	 */
	//	protected void contract() {
	//		if(!this.expanded) return;
	//
	//		this.layout.removeView(this.tableLayout);
	//		this.btnExpandible.setBackgroundResource(R.drawable.ic_menu_expandir);
	//		this.expanded = false;
	//	}
}