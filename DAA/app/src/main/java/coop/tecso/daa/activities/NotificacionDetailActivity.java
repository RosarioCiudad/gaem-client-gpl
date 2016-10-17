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

package coop.tecso.daa.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import coop.tecso.daa.R;
import coop.tecso.daa.dao.DAOFactory;
import coop.tecso.daa.dao.NotificacionDAO;
import coop.tecso.daa.domain.notificacion.Notificacion;

/**
 * Detalle de Notificacion.
 * 
 *  @author Tecso Coop. Ltda.
 */
public class NotificacionDetailActivity extends Activity {

	public static final String PARAM_NOTIFICACION_ID = "PARAM_NOTIFICACION_ID";
	
	@Override
	public void onCreate(Bundle icicle) {
		Log.d(LOG_TAG, "onCreate init...");		
		super.onCreate(icicle);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout detail = (LinearLayout) inflater.inflate(R.layout.notificacion_detail, null , false);
		
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
			Integer notificacionID = extras.getInt(PARAM_NOTIFICACION_ID);
			
			NotificacionDAO notificacionDAO = DAOFactory.getInstance().getNotificacionDAO();
			Notificacion notif = notificacionDAO.findById(notificacionID);
			
			TextView descripcionReducidaLbl = (TextView) detail.findViewById(R.id.detail_descripcionReducidaLabel);
			descripcionReducidaLbl.setText("Descripción reducida: ");
			
			TextView descripcionReducida = (TextView) detail.findViewById(R.id.detail_descripcionReducida);
			descripcionReducida.setText(notif.getDescripcionReducida());
			
			TextView descripcionAmpliadaLbl = (TextView) detail.findViewById(R.id.detail_descripcionAmpliadaLabel);
			descripcionAmpliadaLbl.setText("Descripción ampliada: ");
			
			TextView descripcionAmpliada = (TextView) detail.findViewById(R.id.detail_descripcionAmpliada);
			descripcionAmpliada.setText(notif.getDescripcionAmpliada());
			
			Button confirmButton = (Button) detail.findViewById(R.id.detail_button);
			confirmButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					NotificacionDetailActivity.this.finish();
				}
			});
		}
		
		setContentView(detail);
		
	}
	
	// Implementation helpers

	private static final String LOG_TAG = NotificacionDetailActivity.class.getSimpleName();

}
