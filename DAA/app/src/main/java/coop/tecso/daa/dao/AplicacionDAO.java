package coop.tecso.daa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.GenericRawResults;

import coop.tecso.daa.domain.aplicacion.Aplicacion;
import coop.tecso.daa.utils.Constants;


/**
 * 
 * @author tecso.coop
 *
 */
public final class AplicacionDAO extends GenericDAO<Aplicacion> {

	/**
	 * The AplicacionDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public AplicacionDAO(Context context) {
		super(context);
	}


	/**
	 * Find Aplicacion by codigo.
	 * 
	 * @param codigo
	 * @return aplicacion instance.
	 */
	public Aplicacion findByCodigo(String codigo) {
		Log.d(LOG_TAG, "findByCodigo: enter");
		Aplicacion aplicacion = null;

		// Filters
		Map<String,Object> mFilter = new HashMap<String, Object>();
		mFilter.put("codigo", codigo);
		mFilter.put("estado", 1);

		// Aplicacion
		List<Aplicacion> result = new ArrayList<Aplicacion>();
		result = getDAO().queryForFieldValuesArgs(mFilter);
		if(!result.isEmpty()) aplicacion = result.get(0);

		Log.d(LOG_TAG, "getAplicacionByCodigo: exit");
		return aplicacion;
	}
	
	/**
	 * Find Aplicacion by usuarioId.
	 * 
	 * @param usuarioId
	 * @return
	 */
	public List<Aplicacion> findAllByUsuarioId(int usuarioId) {
		Log.i(LOG_TAG, "findAllByUsuarioId: enter");
		
		//TODO: 
		StringBuilder query = new StringBuilder();
		query.append(" SELECT pap.aplicacion_id FROM apm_perfilAccesoAplicacion pap, ");
		query.append(" apm_perfilAcceso pa, apm_perfilAccesoUsuario pau ");
		query.append(" WHERE pap.perfilAcceso_id = pa.id AND pa.id = pau.perfilAcceso_id ");
		query.append(" AND pau.usuarioAPM_id = "+usuarioId);
		query.append(" AND pap.estado = 1");

		Log.d(LOG_TAG, query.toString());

		// Find PerfilAccesoAplicacion
		List<Aplicacion> result = new ArrayList<Aplicacion>();
		try {
			GenericRawResults<String[]> rawResults = getDAO().queryRaw(query.toString());
			for (String[] cols : rawResults.getResults()) {
				Integer id = Integer.valueOf(cols[0]);
				result.add(getDAO().queryForId(id));
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}

		Log.i(LOG_TAG, "findAllByUsuarioId: exit");
		return result;
	}
	
	
	/**
	 * 
	 * @param usuarioID
	 * @param codAplicacion
	 * @return
	 */
	public boolean hasAccess(int usuarioID, String codAplicacion) {
		Log.d(LOG_TAG, "hasAccess: enter");
		if(Constants.COD_DAA.equals(codAplicacion)) return true;

		boolean hasAccess = false;
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT ap.id FROM apm_aplicacion ap, apm_perfilAccesoAplicacion pap, ");
		query.append(" apm_perfilAcceso pa, apm_perfilAccesoUsuario pau ");
		query.append(" WHERE pap.perfilAcceso_id = pa.id AND pa.id = pau.perfilAcceso_id ");
		query.append(" AND pap.aplicacion_id = ap.id ");
		query.append(" AND pau.usuarioAPM_id = "+usuarioID);
		query.append(" AND ap.codigo COLLATE NOCASE LIKE ? ");
		query.append(" AND ap.estado = 1");

		Log.d(LOG_TAG, query.toString());

		try {
			GenericRawResults<String[]> rawResults = getDAO().queryRaw(query.toString(), codAplicacion);
			hasAccess = !rawResults.getResults().isEmpty();
		} catch (Exception e) {
			Log.e(LOG_TAG, "hasAccess: **ERROR**", e);
		};

		Log.i(LOG_TAG, "hasAccess: exit");
		return hasAccess;
	}
}