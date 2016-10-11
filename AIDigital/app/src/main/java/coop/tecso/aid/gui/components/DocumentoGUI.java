package coop.tecso.aid.gui.components;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.common.WebServiceController;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.helpers.ParamHelper;

/**
 * 
 * @author tecso.coop
 *
 */
public class DocumentoGUI extends CampoGUI {

	private static final String LOG_TAG = DocumentoGUI.class.getSimpleName();

	private int maxChar = 250;

	private LinearLayout mainLayout;
	private EditText textBox;

	public DocumentoGUI(Context context) {
		super(context);
	}

	public DocumentoGUI(Context context, List<Value> values) {
		super(context,values);
	}

	public DocumentoGUI(Context context, boolean enabled) {
		super(context, enabled);
		this.textBox = new EditText(context);
		//textBox.setTextColor(R.color.negro);
		textBox.setTextAppearance(context, R.style.UiEditTex);
	}

	// Getters y Setters
	public String getValorView() {
		return  this.textBox.getText().toString();
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
			this.mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		}else{
			//Se define un Table Row para ubicar: 'Label | EditText'
			this.mainLayout = new TableRow(context);
			this.label.setGravity(Gravity.RIGHT);
			this.label.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3f));
			this.textBox.setLayoutParams(new LayoutParams(50,  LayoutParams.WRAP_CONTENT,1f));
		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);

		// Texto editable
		this.textBox.setEnabled(enabled);
		this.textBox.setFocusable(enabled);
		if(this.getInitialValues() != null && this.getInitialValues().size() >= 1){
			this.textBox.setText(this.getInitialValues().get(0).getValor());
		}else{
			this.textBox.setText(this.getValorDefault());
		}

		if(!TextUtils.isEmpty(this.getMascaraVisual())){
			try {maxChar = Integer.valueOf(this.getMascaraVisual()).intValue();
			} catch (Exception e) {}
		}

		//   Se crea y aplica un filtro para definir la cantidad maxima de caracteres de la caja de texto
		InputFilter maxLengthFilter = new InputFilter.LengthFilter(maxChar);
		this.textBox.setFilters(new InputFilter[]{ maxLengthFilter });
		this.textBox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_NUMBER);

		//   Se agrega validacion de requerido al cambio de foco
		this.textBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus && isObligatorio()) {
					if(TextUtils.isEmpty(textBox.getText().toString())){
						textBox.setError(context.getString(R.string.field_required, getEtiqueta()));
					}else{
						textBox.setError(null);
					}
				}
			}
		});

		//
		ImageButton searchButton = new ImageButton(context);
		searchButton.setFocusable(enabled);
		searchButton.setEnabled(enabled);
		searchButton.setImageResource(R.drawable.ic_btn_search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// Licencia
				String claseLicencia = null;
				Value value = getPerfilGUI().getValorForCampoID(
						ParamHelper.getInteger(ParamHelper.CLASE_LICENCIA, 27));
				if(value != null && !TextUtils.isEmpty(value.getValor().trim()))
					claseLicencia = value.getValor().split("\\|")[1];
				// Sexo
				String sexo = null;
				value = getPerfilGUI().getValorForCampoID(
						ParamHelper.getInteger(ParamHelper.SEXO, 26));
				if(value != null && !TextUtils.isEmpty(value.getValor()))
					sexo = String.valueOf(value.getValor().charAt(0));
				// TipoDocumento
				String tipoDocumento = null;
				value = getPerfilGUI().getValorForCampoID(
						ParamHelper.getInteger(ParamHelper.TIPO_DOCUMENTO, 24));
				if(value != null && !TextUtils.isEmpty(value.getValor()))
					tipoDocumento = value.getValor().split("\\|")[0];

				// WS
				AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
					private ProgressDialog dialog;
					private WebServiceController webService;

					@Override
					protected void onPreExecute() {
						this.dialog = ProgressDialog.show(context, "", context.getString(R.string.searching_msg));
						this.webService =  WebServiceController.getInstance(context);
					}

					@Override
					protected String doInBackground(String... params) {
						try {
							return webService.estadoLicencia(params[0], params[1], params[2], params[3]);
						} catch (Exception e) {
							Log.e(LOG_TAG, "**ERROR**", e);
							return "No pudo realizarse la consulta";
						}
					}

					@Override
					protected void onPostExecute(String result) {
						this.dialog.dismiss();
						//textBox.setError(result);
						final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("Información");
						String msg = result.replace("\"", "").replace("\\n", "%n");
						alertDialog.setMessage(String.format(msg));
						alertDialog.setCancelable(false);
						alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
								"OK",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								alertDialog.dismiss();
							}});
						alertDialog.show();
					}
				};
				task.execute(tipoDocumento,getValorView(), sexo, claseLicencia);
			}
		});

		if(appState.getOrientation() == LinearLayout.VERTICAL){
			LinearLayout documentoView = new LinearLayout(context);
			documentoView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,
					9));
			documentoView.setOrientation(LinearLayout.VERTICAL);
			documentoView.addView(label);
			documentoView.addView(textBox);

			LinearLayout searchView = new LinearLayout(context);
			searchView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,
					2));
			searchView.setOrientation(LinearLayout.VERTICAL);
			searchView.addView(searchButton);

			LinearLayout linearLayoutHorizontal = new LinearLayout(context);
			linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
			linearLayoutHorizontal.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			linearLayoutHorizontal.setBaselineAligned(true);
			linearLayoutHorizontal.addView(documentoView);
			linearLayoutHorizontal.addView(searchView);

			this.mainLayout.addView(linearLayoutHorizontal);
		}else{
			this.mainLayout.addView(label); 
			this.mainLayout.addView(textBox);
			this.mainLayout.addView(searchButton);
		}

		this.view = mainLayout;

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

		if(dirty){
			Log.d(LOG_TAG, String.format("%s dirty=true", getEtiqueta()));
		}
		return dirty;
	}

	@Override
	public boolean validate() {
		if (isObligatorio() && TextUtils.isEmpty(this.textBox.getText().toString())) {
			textBox.setError(context.getString(R.string.field_required, getEtiqueta()));
			textBox.requestFocus();
			return false;
		}

		return true;
	}

	@Override
	public View getEditViewForCombo(){
		return this.textBox;
	}

	@Override
	public void removeAllViewsForMainLayout(){
		this.mainLayout.removeAllViews();
	}

	@Override
	public String getPrinterValor() {
		if(!TextUtils.isEmpty(getValorView()))
			return getEtiqueta()+": "+getValorView();
		else
			return "";
	}
}