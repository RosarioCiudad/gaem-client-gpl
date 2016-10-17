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