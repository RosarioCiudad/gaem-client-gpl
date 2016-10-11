package coop.tecso.aid.gui.components;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.aid.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import coop.tecso.aid.gui.helpers.Value;

public class SeccionGUI extends Component {

	private static final String LOG_TAG = SeccionGUI.class.getSimpleName();    

	private String etiqueta;
	private TextView label;
	private ImageButton btnExpandible;
	private LinearLayout layout;
	private LinearLayout titleLayout;
	private TableLayout tableLayout; 
	private boolean noDesplegar = false;
	private boolean opcional = false;
	private boolean expanded = true;
	private PerfilGUI perfilGUI;
	private int idCampoResumen;

	// Constructores
	public SeccionGUI(Context context) {
		super();
		this.context = context;
	}

	public SeccionGUI(Context context, boolean enabled) {
		super();
		this.context = context;
		this.enabled = enabled;
	}

	// Getters y Setters
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public TableLayout getTableLayout() {
		return tableLayout;
	}
	public boolean isNoDesplegar() {
		return noDesplegar;
	}
	public void setNoDesplegar(boolean noDesplegar) {
		this.noDesplegar = noDesplegar;
	}
	public boolean isOpcional() {
		return opcional;
	}
	public void setOpcional(boolean opcional) {
		this.opcional = opcional;
	}
	public PerfilGUI getPerfilGUI() {
		return perfilGUI;
	}
	public void setPerfilGUI(PerfilGUI perfilGUI) {
		this.perfilGUI = perfilGUI;
	}
	public int getIdCampoResumen() {
		return idCampoResumen;
	}
	public void setIdCampoResumen(int idCampoResumen) {
		this.idCampoResumen = idCampoResumen;
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
				CampoGUI campo = (CampoGUI) co;
				values.addAll(campo.values());
			}
		}
		return values;
	}

	public List<Value> dirtyValues() {
		List<Value> values = new ArrayList<Value>();
		for(Component co: this.components){
			CampoGUI campo = (CampoGUI) co;
			values.addAll(campo.dirtyValues());
		}
		return values;
	}

	@Override
	public boolean isDirty(){
		for(Component co: this.components){
			if(co.isDirty()){
				Log.i(LOG_TAG, String.format("%s DIRTY", getEtiqueta()));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validate() {
		//--
		if(getEtiqueta().equalsIgnoreCase("conductor")){
			boolean validate = false;
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				if(campo.getEtiqueta().equalsIgnoreCase("documento")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("sexo") && !campo.values().isEmpty()){
					validate = !campo.values().get(0).getCampoValor().getCampoValor().getEtiqueta().startsWith("S");
				}
				if(campo.getEtiqueta().toLowerCase().contains("licencia")){
					validate = !TextUtils.isEmpty(campo.getValorView().trim());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("apellido")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("nombre")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().toLowerCase().contains("domicilio")){
					validate = !(campo.getValorView().equals("0|||false|||||2000|8|ROSARIO|1|ARGENTINA|true") ||
							campo.getValorView().equals("0|||false|||||0|0||1|ARGENTINA|false") ||
							campo.getValorView().equals("0|||false|||||2000|8||1|ARGENTINA|true"));
				}
				if(validate) break;

			}
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				campo.setObligatorio(validate);
			}
		}
		
		if(getEtiqueta().equalsIgnoreCase("Cede Manejo a:")){
			Log.d("CEDE", "entro al control de cede manejo");
			boolean validate = false;
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				if(campo.getEtiqueta().equalsIgnoreCase("documento cede") || campo.getEtiqueta().equalsIgnoreCase("documento")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("apellido cede") || campo.getEtiqueta().equalsIgnoreCase("apellido")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("nombre cede") || campo.getEtiqueta().equalsIgnoreCase("nombre")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().toLowerCase().contains("domicilio cede") || campo.getEtiqueta().toLowerCase().contains("domicilio")){
					validate = !(campo.getValorView().equals("0|||false|||||2000|8|ROSARIO|1|ARGENTINA|true") ||
							campo.getValorView().equals("0|||false|||||0|0||1|ARGENTINA|false") ||
							campo.getValorView().equals("0|||false|||||2000|8||1|ARGENTINA|true"));
				}
				Log.d("CEDE", "validate = "+validate);
				if(validate) break;

			}
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				campo.setObligatorio(validate);
			}
		}
		
		if(getEtiqueta().equalsIgnoreCase("testigo 1")){
			boolean validate = false;
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				if(campo.getEtiqueta().equalsIgnoreCase("documento t1") || campo.getEtiqueta().equalsIgnoreCase("documento")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("apellido t1") || campo.getEtiqueta().equalsIgnoreCase("apellido")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("nombre t1") || campo.getEtiqueta().equalsIgnoreCase("nombre")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().toLowerCase().contains("domicilio t1") || campo.getEtiqueta().toLowerCase().contains("domicilio")){
					validate = !(campo.getValorView().equals("0|||false|||||2000|8|ROSARIO|1|ARGENTINA|true") ||
							campo.getValorView().equals("0|||false|||||0|0||1|ARGENTINA|false") ||
							campo.getValorView().equals("0|||false|||||2000|8||1|ARGENTINA|true"));
				}
				if(validate) break;

			}
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				campo.setObligatorio(validate);
			}
		}
		
		if(getEtiqueta().equalsIgnoreCase("testigo 2")){
			boolean validate = false;
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				if(campo.getEtiqueta().equalsIgnoreCase("documento t2") || campo.getEtiqueta().equalsIgnoreCase("documento")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("apellido t2") || campo.getEtiqueta().equalsIgnoreCase("apellido")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().equalsIgnoreCase("nombre t2") || campo.getEtiqueta().equalsIgnoreCase("nombre")){
					validate = !TextUtils.isEmpty(campo.getValorView());
				}
				if(campo.getEtiqueta().toLowerCase().contains("domicilio t2") || campo.getEtiqueta().toLowerCase().contains("domicilio")){
					validate = !(campo.getValorView().equals("0|||false|||||2000|8|ROSARIO|1|ARGENTINA|true") ||
							campo.getValorView().equals("0|||false|||||0|0||1|ARGENTINA|false") ||
							campo.getValorView().equals("0|||false|||||2000|8||1|ARGENTINA|true"));
				}
				if(validate) break;

			}
			for(Component co: this.components){
				CampoGUI campo = (CampoGUI) co;
				campo.setObligatorio(validate);
			}
		}
		
		//--

		// Recorremos los elementos de la seccion verificando si algun requerido es vacio
		for(Component co: this.components){
			CampoGUI campo = (CampoGUI) co;

			if(!campo.validate()){
				return false;
			}
		}
		return true;
	}

	@Override
	public View build() {
		// Se define un layout lineal vertical para armar la seccion desplegable
		this.layout = new LinearLayout(context);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		this.layout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Titulo: Se define un layout lineal horizonal para mostrar la etiqueta de seccion y el boton para retraer/expandir
		this.titleLayout = new LinearLayout(context);
		this.titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.titleLayout.setBackgroundColor(context.getResources().getColor(R.color.label_background_color));
		this.titleLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		//   Boton para Expander/Retraer
		this.btnExpandible = new ImageButton(context);
		this.btnExpandible.setBackgroundResource(R.drawable.ic_menu_contraer);
		this.btnExpandible.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.btnExpandible.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (expanded) {
					contract();
				} else {
					expand();
				}
			}
		});
		// 	 Etiqueta
		this.label = new TextView(context); 
		this.label.setBackgroundColor(context.getResources().getColor(R.color.label_background_color));
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta());
		this.label.setGravity(Gravity.CENTER_VERTICAL);
		this.label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		this.titleLayout.addView(this.btnExpandible);
		this.titleLayout.addView(this.label);

		// Se define un layout de tipo Tabla para contener los distintos Campos
		this.tableLayout = new TableLayout(context);
		this.tableLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.tableLayout.setPadding(10, 2, 10, 2);

		this.tableLayout.setColumnShrinkable(0, true);
		this.tableLayout.setColumnStretchable(1, true);

		// Se agregan los componentes correspondientes a los campos al layout contenedor
		if(this.components != null){
			for(Component co: this.components){
				this.tableLayout.addView(co.getView()); //(TableRow) 

				// Separador de campos
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.line_background_color));

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

		// Se verifica si debe inicializarse sin desplegar
		if(this.isNoDesplegar())
			contract();

		this.view = this.layout; 

		// Se verifica si la seccion es opcional. En dicho caso se oculta
		if(this.isOpcional()){
			ocultar();
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

	/**
	 * Expande la seccion
	 * 
	 */
	protected void expand() {
		if(this.expanded) return;

		this.layout.addView(this.tableLayout);
		this.btnExpandible.setBackgroundResource(R.drawable.ic_menu_contraer);
		this.expanded = true;

		if (idCampoResumen > 0) this.label.setText(this.getEtiqueta());
	}

	/**
	 * Contrae la seccion 
	 */
	protected void contract() {
		if(!this.expanded) return;

		this.layout.removeView(this.tableLayout);
		this.btnExpandible.setBackgroundResource(R.drawable.ic_menu_expandir);
		this.expanded = false;
	}

	/**
	 * Oculta la seccion
	 * 
	 * @return
	 */
	public View ocultar() {
		if(this.view != null && this.view.getVisibility() == View.VISIBLE){
			this.view.setVisibility(View.GONE);
		}
		return this.view;
	}

	/**
	 * Muestra la seccion oculta
	 * 
	 * @return
	 */
	public View mostrar() {
		if(this.view != null && this.view.getVisibility() == View.GONE){
			this.view.setVisibility(View.VISIBLE);
			this.view.requestFocus();
		}
		return this.view;
	}

	/**
	 * Devuelve true si la seccion es visible
	 * 
	 * @return
	 */
	public boolean isVisible(){
		return (this.view != null && this.view.getVisibility() == View.VISIBLE);
	}

	/**
	 * 
	 */
	@Override
	public String getPrinterValor() {
		if(!this.isVisible())
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append("______________________________________________");
		builder.append("<br>");	
		builder.append("<b>"+etiqueta.toUpperCase()+"</b>");
		builder.append("<br>");	
		//
		boolean hasData = false;
		for (Component component : components) {
			String value = component.getPrinterValor();
			if(!TextUtils.isEmpty(value)){
				hasData = true;
				builder.append(value);
				builder.append("<br>");
			}
		}
		//TODO:
		// En caso de que no se haya especificado el documento, descarto la seccion 'Conductor'
		if(hasData && etiqueta.equalsIgnoreCase("conductor")){
			// Tipo Documento
			int index = builder.toString().indexOf("Documento");
			if(index >= 0){
				// Numero Documento
				index = builder.toString().indexOf("Documento", index + 1);
				if(index < 0) return "";
			}
		}
		// En caso de que no se haya especificado el documento del testigo 1, descarto la seccion
		if(hasData && etiqueta.equalsIgnoreCase("Testigo 1")){
			// Tipo Documento
			int index = builder.toString().indexOf("Documento");
			if(index >= 0){
				// Numero Documento
				index = builder.toString().indexOf("Documento", index + 1);
				if(index < 0) return "";
			}
		}
		// En caso de que no se haya especificado el documento del testigo 1, descarto la seccion
		if(hasData && etiqueta.equalsIgnoreCase("Testigo 2")){
			// Tipo Documento
			int index = builder.toString().indexOf("Documento");
			if(index >= 0){
				// Numero Documento
				index = builder.toString().indexOf("Documento", index + 1);
				if(index < 0) return "";
			}
		}
		// En caso de que no se haya especificado el documento del testigo 1, descarto la seccion
		if(hasData && etiqueta.equalsIgnoreCase("Cede Manejo a:")){
			// Tipo Documento
			int index = builder.toString().indexOf("Documento");
			if(index >= 0){
				// Numero Documento
				index = builder.toString().indexOf("Documento", index + 1);
				if(index < 0) return "";
			}
		}

		return hasData?builder.toString():"";
	}
}