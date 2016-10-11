package coop.tecso.daa.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.GenericRawResults;

import coop.tecso.daa.domain.aplicacion.AplicacionBinarioVersion;


/**
 * 
 * @author tecso.coop
 *
 */
public final class AplicacionBinarioVersionDAO extends GenericDAO<AplicacionBinarioVersion> {

	/**
	 * The AplicacionBinarioVersionDAO constructor.
	 * 
	 * @param context The Android context.
	 */
	public AplicacionBinarioVersionDAO(Context context) {
		super(context);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getListPathBy(String codAplicacion, String tipoBinario){
		Log.d(LOG_TAG, "getListPathBy: enter");
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT(bin.ubicacion) ");
		query.append(" FROM apm_aplicacionbinarioversion bin, ");
		query.append(" apm_aplicacion apm, apm_aplicacionTipoBinario tip ");
		query.append(" WHERE apm.codigo COLLATE NOCASE LIKE ? ");
		query.append(" AND bin.aplicacion_id = apm.id ");
		query.append(" AND bin.aplTipoBinario_id = tip.id ");
		query.append(" AND tip.descripcion COLLATE NOCASE LIKE ? ");
		query.append(" AND bin.estado = 1");

		Log.d(LOG_TAG, query.toString());

		GenericRawResults<String[]> rawResults;
		rawResults = getDAO().queryRaw(query.toString(), codAplicacion, tipoBinario);
		
		List<String> result = new ArrayList<String>();
		try {
			for (String[] cols : rawResults.getResults()) {
				result.add(cols[0]);
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "getListPathBy: **ERROR**", e);
		}
		
		Log.d(LOG_TAG, "getListPathBy: exit");
		return result;
	}
}