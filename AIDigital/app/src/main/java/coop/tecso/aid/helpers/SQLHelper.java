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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import coop.tecso.aid.persistence.DatabaseHelper;

public class SQLHelper {
	
	private static final String LOG_TAG = SQLHelper.class.getSimpleName();
	
	private SQLiteDatabase db;
	private Context context;
	
	public SQLHelper(Context context) {
		this.context = context;
	}
	
	/**
	 * Open Custom Database from external
	 * @param dbName
	 */
	public void openDatabase(String dbName){
		String path = context.getDir("db", Context.MODE_PRIVATE) + "/" + dbName;
        try {
        	db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS|SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
        	Log.e(LOG_TAG, "**ERROR**", e);
        }
	}
	
	/**
	 * Open connection whit readable database
	 * @param dbName
	 */
	public void openWritableDatabase(){
		db = new DatabaseHelper(context).getWritableDatabase();
	}
	
	public void openReadableDatabase(){
		db = new DatabaseHelper(context).getReadableDatabase();
	}
	
	public void beginTransaction(){
		db.beginTransaction();
	}
	
	public void setTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	
	public void endTransaction(){
		db.endTransaction();
	}
	
	/**
	 * 
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, String having, String orderBy){
		return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	/**
	 * 
	 * @return
	 */
	public Cursor rawQuery(String sql, String... selectionArgs){
		return db.rawQuery(sql, selectionArgs);
	}
	
	/**
	 * 
	 * @return
	 */
	public long insert(String table, ContentValues values){
		return db.insert(table, null, values);
	}
	
	/**
	 * 
	 * @return
	 */
	public long delete(String table, String whereClause, String[] whereArgs){
		return db.delete(table, whereClause, whereArgs);
	}
	
	public void closeDatabase(){
		try {
			db.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "**ERROR**", e);
		}
	}
}