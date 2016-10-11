package coop.tecso.aid.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;

import coop.tecso.aid.entities.Formulario;
import coop.tecso.aid.entities.FormularioDetalle;

public class FormularioDetalleDAO extends GenericDAO<FormularioDetalle> {

	public FormularioDetalleDAO(Context context) {
		super(context);
	}
	
	/**
	 * 
	 */
	public int deleteByAfiliacion(Formulario afiliacion){
		Log.d(LOG_TAG, "deleteByAfiliacion: enter");
		DeleteBuilder<FormularioDetalle, Integer> db = getDAO().deleteBuilder();
		try {
			db.where().eq("afiliacion_id", afiliacion.getId());
			Log.d(LOG_TAG, "deleteByAfiliacion: exit");
			return delete(db.prepare());
		} catch (Exception e) {
			Log.e(LOG_TAG, "deleteByAfiliacion: ***ERROR***", e);
			return 0;
		}
	}

}
