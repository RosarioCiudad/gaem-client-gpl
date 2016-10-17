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

package coop.tecso.aid.helpers;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.entities.EstadoTipoFormulario;
import coop.tecso.aid.entities.Formulario;

public class SearchPageAdapter extends ArrayAdapter<Formulario> {
	private Activity context;
	private List<Formulario> formList;

	static public class ViewHolder {
		public TextView  fechaFormularioTextView;
		public TextView  nroFormularioTextView;
		public TextView  dominioTextView;
		public TextView  tipoText;
		public ImageView estadoAtencionImageView;
		public TextView  estadoAtencionTextView;
		public TextView  syncronizedTextView;
	}
	public SearchPageAdapter(Activity context, List<Formulario> formList) {
		super(context, R.layout.fila_im, formList);
		this.context = context;
		this.formList = formList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("SearchPageAdapter", "getView: enter");
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.fila_im, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.estadoAtencionImageView = (ImageView) rowView.findViewById(R.id.estadoFormularioImage);
			viewHolder.estadoAtencionTextView = (TextView) rowView.findViewById(R.id.estadoAtencionText);
			viewHolder.fechaFormularioTextView = (TextView) rowView.findViewById(R.id.fechaFormularioText);
			viewHolder.nroFormularioTextView = (TextView) rowView.findViewById(R.id.nroFormularioText);
			viewHolder.dominioTextView = (TextView) rowView.findViewById(R.id.dominioText);
			viewHolder.tipoText = (TextView) rowView.findViewById(R.id.tipoText);
			viewHolder.syncronizedTextView = (TextView) rowView.findViewById(R.id.syncronizedText);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		Formulario formulario = formList.get(position);
//		holder.fechaFormularioTextView.setText(DateFormat.getDateInstance().format(formulario.getFechaInicio()));
		holder.fechaFormularioTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(formulario.getFechaInicio()));
		holder.nroFormularioTextView.setText(String.format("Número: %s", formulario.getNumero()));
		holder.dominioTextView.setText(formulario.getDominio());
		holder.tipoText.setText(formulario.getTipoFormulario().getDescripcion());
		holder.estadoAtencionTextView.setText(formulario.getEstadoTipoFormulario().getDescripcion());
		if (formulario.getEstado() == 0)
			holder.syncronizedTextView.setText(R.string.synchronized_item_label);
		else
			holder.syncronizedTextView.setText(R.string.unsynchronized_item_label);

		// Cambiamos el icono segun el estado de la atencion
		if (EstadoTipoFormulario.ID_EN_PREPARACION == formulario.getEstadoTipoFormulario().getId()) {
			holder.estadoAtencionImageView.setImageResource(R.drawable.presence_online);
		} else if (EstadoTipoFormulario.ID_CERRADA_PROVISORIA == formulario.getEstadoTipoFormulario().getId()) {
			holder.estadoAtencionImageView.setImageResource(R.drawable.presence_away);
		} else if (EstadoTipoFormulario.ID_ANULADA == formulario.getEstadoTipoFormulario().getId()) {
			holder.estadoAtencionImageView.setImageResource(R.drawable.presence_busy);
		} else {
			holder.estadoAtencionImageView.setImageResource(R.drawable.ic_lock_lock);
		}

		return rowView;
	}
}