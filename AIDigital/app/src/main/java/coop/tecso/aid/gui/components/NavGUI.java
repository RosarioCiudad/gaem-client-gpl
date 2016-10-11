package coop.tecso.aid.gui.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

/**
 * Tratamiento Link a Aplicaciones o Web pages.
 * @author tecso.coop
 *
 */
public class NavGUI extends CampoGUI {
	
	private static final String TAG = NavGUI.class.getSimpleName();

	private LinearLayout mainLayout;
	private LinearLayout buttonLayout;
	private Button button;
	
	// Constructs
	public NavGUI(Context context) {
		super(context);
	}
	
	public NavGUI(Context context, boolean enabled) {
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

		if (appState.getOrientation() == LinearLayout.VERTICAL) {
			// Se define un LinearLayout para ubicar: 'Label / EditText'
			this.mainLayout = new LinearLayout(context);
			this.mainLayout.setOrientation(LinearLayout.VERTICAL);
		} else {
			//Se define un Table Row para ubicar: 'Label | EditText'
			this.mainLayout = new TableRow(context);
			this.label.setGravity(Gravity.RIGHT);
		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		// Se arma el boton para disparar control se seleccion de fecha
		this.button = new Button(context);
		this.button.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.button.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_navigation), null, null, null);
		this.button.setText("Navigation");
		this.button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.button.setFocusable(true);
		this.button.setFocusableInTouchMode(true);
		this.button.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				button.requestFocus();
				return false;
			}
		});
		this.button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				String val = "";
				if (getInitialValues() != null && getInitialValues().size() >= 1) {
					val = getInitialValues().get(0).getValor();
					Log.d(TAG,"Value from InitialValues: " + val);
				} else if (!TextUtils.isEmpty(valorDefault)) {
					val = valorDefault;
					Log.d(TAG, "Value from IAM by default value: " + val);
				} 
				
				String uri = "";
				Log.i("Uri.parse: ", Uri.parse(uri).toString());
				if (TextUtils.isEmpty(uri)) {
					intent = new Intent(Intent.ACTION_VIEW);
					intent.setClassName("com.google.android.apps.maps","com.google.android.maps.driveabout.app.DestinationActivity");
					Log.d(TAG, "Intent without destination: com.google.android.apps.maps , com.google.android.maps.driveabout.app.DestinationActivity");
				} else {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
					Log.d(TAG, "Intent with destination: " +Uri.parse(uri).toString());
				}
				context.startActivity(intent);
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
		if (this.entity instanceof AplPerfilSeccionCampo) {
			campo = (AplPerfilSeccionCampo) this.entity;
		} else if (this.entity instanceof AplPerfilSeccionCampoValor) {
			campoValor = (AplPerfilSeccionCampoValor) this.entity;
			campo = campoValor.getAplPerfilSeccionCampo();
		} else if (this.entity instanceof AplPerfilSeccionCampoValorOpcion) {
			campoValorOpcion = (AplPerfilSeccionCampoValorOpcion) this.entity;
			campoValor = campoValorOpcion.getAplPerfilSeccionCampoValor();
			campo = campoValor.getAplPerfilSeccionCampo();
		}
		String nombreCampo = campo!=null?campo.getCampo().getEtiqueta():"No identificado";
		String valor = this.getValorView();

		Log.d(NavGUI.class.getSimpleName(),"save() : "+this.getTratamiento()+" :Campo: "+nombreCampo
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