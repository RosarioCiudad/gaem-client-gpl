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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import coop.tecso.daa.R;
import coop.tecso.daa.domain.notificacion.Notificacion;

public class NotificacionArrayAdapter extends ArrayAdapter<Notificacion> {

	private static final String LOG_TAG = NotificacionArrayAdapter.class.getSimpleName();
		
	private Context context;
	private ImageView iconView;
	private TextView descripcionReducidaTextView;
	private TextView descripcionAmpliadaTextView;
	
	private List<Notificacion> data = new ArrayList<Notificacion>();

	public NotificacionArrayAdapter(Context context, int textViewResourceId, List<Notificacion> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.data = objects;
	}

	public int getCount() {
		return this.data.size();
	}

	public Notificacion getItem(int index) {
		return this.data.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			Log.d(LOG_TAG, "Starting XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) 
				getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_item_notificacion, parent, false);
			Log.d(LOG_TAG, "Successfully completed XML Row Inflation!");
		}

		// Get item
		Notificacion notificacion = getItem(position);
		
		// Get reference to ImageView 
		iconView = (ImageView) row.findViewById(R.id.notificacion_icon);		
		iconView.setImageResource(android.R.drawable.ic_dialog_info);

		// Get reference to TextViews
		descripcionReducidaTextView = (TextView) row.findViewById(R.id.notificacion_descripcionReducida);
		descripcionReducidaTextView.setText(notificacion.getDescripcionReducida());
		
		descripcionAmpliadaTextView = (TextView) row.findViewById(R.id.notificacion_descripcionAmpliada);
		descripcionAmpliadaTextView.setText(notificacion.getDescripcionAmpliada());
		return row;
	}
}