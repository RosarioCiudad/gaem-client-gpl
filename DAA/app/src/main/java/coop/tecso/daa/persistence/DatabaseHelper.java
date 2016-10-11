package coop.tecso.daa.persistence;


import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import coop.tecso.daa.R;
import coop.tecso.daa.dao.AplicacionDAO;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String LOG_TAG = DatabaseHelper.class.getName();	
	private static final String DATABASE_NAME = "daa-mr.db";
	private static final int	DATABASE_VERSION = 35;
	private final Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.db_config);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		Log.i(LOG_TAG, "Creating database...");   
		try {
			for (Class<?> clazz: DatabaseConfigUtil.PERSISTENT_CLASSES) {
				TableUtils.createTable(connectionSource, clazz);    
			}   			
			Log.i(LOG_TAG, "Se ha creado la base de datos");
			// 
			initialImport(db);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}  
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. 
	 * This allows you to adjust the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.i(LOG_TAG, String.format("onUpgrade, oldVersion=[%s], newVersion=[%s]", oldVersion, newVersion));
		try {
			// Loop round until newest version has been reached and add the appropriate migration
			while (oldVersion <= newVersion) {
				switch (oldVersion) {
				case 52: {
					// Add migration for version 52 if required
					UpgradeHelper.addUpgrade(oldVersion);
					break;}
				}
				oldVersion++;
			}
			// Loop to create tables for persistent classes
			for (Class<?> clazz: DatabaseConfigUtil.PERSISTENT_CLASSES) {
				if(UpgradeHelper.canUpgrade(clazz)){
					// Drop Table
					TableUtils.dropTable(connectionSource, clazz, true);  
					// Create Table
					TableUtils.createTable(connectionSource, clazz);
				}
			}
			// Get all the available updates
			List<String> updates = UpgradeHelper.availableUpdates(this.context.getResources());
			Log.d(LOG_TAG, String.format("Found a total of %s update statements", updates.size()));
			for (String statement : updates) {
				db.beginTransaction();
				try {
					Log.d(LOG_TAG, String.format("Executing statement: %s", statement));
					db.execSQL(statement);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}
			// 
			initialImport(db);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Can't migrate databases, bootstrap database, data will be lost", e);
			onCreate(db, connectionSource);
		}
	}
	
	/**
	 * 
	 * @param db
	 */
	private void initialImport(SQLiteDatabase db){
		// Get all the available inserts
		List<String> inserts = UpgradeHelper.availableInserts(this.context.getResources());
		Log.d(LOG_TAG, String.format("Found a total of %s insert statements", inserts.size()));
		for (String statement : inserts) {
			db.beginTransaction();
			try {
				Log.d(LOG_TAG, String.format("Executing statement: %s", statement));
				db.execSQL(statement);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}

	
	private AplicacionDAO aplicacionDAO = null;
	public AplicacionDAO getAplicacionDAO() throws SQLException {
		if (aplicacionDAO == null) {
//			aplicacionDAO = getDaoX(Aplicacion.class);
		}
		
		return aplicacionDAO;
	}
	
	private <D extends BaseDaoImpl<T, Integer>, T> D getDaoX(Class<T> clazz) throws SQLException {
		// lookup the dao, possibly invoking the cached database config
		BaseDaoImpl<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
		if (dao == null) {
			// try to use our new reflection magic
			DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
			if (tableConfig == null) {
				/**
				 * TODO: we have to do this to get to see if they are using the deprecated annotations like
				 * {@link DatabaseFieldSimple}.
				 */
				dao = (BaseDaoImpl<T, ?>) DaoManager.createDao(connectionSource, clazz);
			} else {
				dao = (BaseDaoImpl<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
			}
		}

		@SuppressWarnings("unchecked")
		D castDao = (D) dao;
		return castDao;
	}
	
	/**
	 * Close the database connections.
	 */
	@Override
	public void close() {
		super.close();
	}
}