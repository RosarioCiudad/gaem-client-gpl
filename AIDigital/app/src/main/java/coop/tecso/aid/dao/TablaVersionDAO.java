package coop.tecso.aid.dao;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.daa.domain.base.TablaVersion;

/**
 * 
 * @author leonardo.fagnano
 *
 */
public class TablaVersionDAO extends GenericDAO<TablaVersion> {

	/**
	 * 
	 */
	public TablaVersionDAO(Context context) {
		super(context);
	}

	

	/**
	 * 
	 * @param tableName
	 * @return
	 */
	public TablaVersion findByTableName(String tableName){
		Log.i(LOG_TAG, "findByTableName: enter");
			   
		TablaVersion tablaVersion = null;
		QueryBuilder<TablaVersion, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			queryBuilder.where().rawComparison(
					"tabla", "COLLATE NOCASE LIKE", tableName).and().eq("estado", 1);
			
			Log.d(LOG_TAG, queryBuilder.prepareStatementString());
			// Return the first element
			tablaVersion = queryBuilder.queryForFirst();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "findByTableName: ***ERROR***", e);
		}
		Log.i(LOG_TAG, "findByTableName: exit");
		return tablaVersion;
	}
}