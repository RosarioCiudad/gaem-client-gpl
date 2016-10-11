package coop.tecso.aid.activities;

import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.dao.DAOFactory;
import coop.tecso.aid.entities.MotivoAnulacionTipoFormulario;
import coop.tecso.aid.utils.Constants;

public class AnularFormActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.dialog_anular_form);
		setTitle(R.string.anular_form_title);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_menu_delete);
		// MotivoCierreTipoFormulario
		Spinner spinner = (Spinner) findViewById(R.id.spinnerMotivoAnulacionTipoFormulario);
		//
		List<MotivoAnulacionTipoFormulario> listMotivo =
				DAOFactory.getInstance().getMotivoAnulacionTipoFormularioDAO().findAllActive();
		ArrayAdapter<MotivoAnulacionTipoFormulario> adapterMotivo = 
				new ArrayAdapter<MotivoAnulacionTipoFormulario>(this , 
						android.R.layout.simple_spinner_item, listMotivo) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(AnularFormActivity.this, android.R.layout.simple_spinner_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
			@Override
			public View getDropDownView(int position, View convertView,	ViewGroup parent) {
				TextView holder;
				if (convertView == null) {
					convertView = View.inflate(AnularFormActivity.this, android.R.layout.simple_spinner_dropdown_item, null);
					holder = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder);
				} else {
					holder = (TextView) convertView.getTag();
				}
				holder.setText(this.getItem(position).getDescripcion());
				return convertView;
			}
		};
		adapterMotivo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterMotivo);
		spinner.requestFocus();
	}

	/**
	 * 
	 * @param v
	 */
	public void DoSave(View v){

		Spinner spinner = (Spinner) findViewById(R.id.spinnerMotivoAnulacionTipoFormulario);

		MotivoAnulacionTipoFormulario motivo = 
				(MotivoAnulacionTipoFormulario) spinner.getSelectedItem();

		Intent intent = new Intent();
		intent.putExtra(Constants.COD_MOTIVO_ANULACION, motivo.getId());

		setResult(Activity.RESULT_OK, intent);
		super.finish();
	}

}