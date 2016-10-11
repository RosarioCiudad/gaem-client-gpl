package coop.tecso.daa.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import coop.tecso.daa.domain.base.AbstractEntity;
import coop.tecso.daa.persistence.DatabaseHelper;

/**
 * 
 * @author tecso.coop
 *
 * @param <T>
 */
public abstract class GenericDAO<T extends AbstractEntity> {

	protected final String LOG_TAG = getDomainClass().getSimpleName()+"DAO";
	protected Context context;

	private Class<T> domainClass = getDomainClass();
	private OrmLiteSqliteOpenHelper databaseHelper;

	/**
	 * Constructor
	 */
	public GenericDAO(Context context) {
		this.context = context;
		this.databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
//		this.appState = (SADigitalApplication) context.getApplicationContext();
	}

	/**
	 * 
	 */
	public T findById(int id) {
		return getDAO().queryForId(id);
	}

	/**
	 * 
	 */
	public int delete(T entity) {
		return getDAO().delete(entity);
	}

	/**
	 * 
	 */
	public int delete(Collection<T> entities) {
		return getDAO().delete(entities);
	}
	
	/**
	 * 
	 */
	public int delete(PreparedDelete<T> preparedDelete) {
		return getDAO().delete(preparedDelete);
	}
	
	/**
	 * 
	 */
	public int deleteAll() {
		DeleteBuilder<T, Integer> entityDb = getDAO().deleteBuilder();
		try {
			return getDAO().delete(entityDb.prepare());
		} catch (SQLException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			return 0;
		}
	}
	
	/**
	 * 
	 */
	public int refresh(T entity) {
		return getDAO().refresh(entity);
	}

	/**
	 * 
	 */
	public boolean idExists(int id) {
		return getDAO().idExists(id);
	}
	
	/**
	 * 
	 */
	public int create(T entity) {
		// Data for audit
		entity.setFechaUltMdf(new Date());
//		entity.setModificationUser(appState.getCurrentUser().getNombre());
		entity.setEstado(1);
		return getDAO().create(entity);
	}

	/**
	 * 
	 */
	public int update(T entity) {
		// Data for audit
		entity.setFechaUltMdf(new Date());
//		entity.setModificationUser(appState.getCurrentUser().getNombre());
		return getDAO().update(entity);
	}
	
	
	/**
	 * 
	 */
	public int createOrUpdate(T entity) {
		if (idExists(entity.getId())) {
			// Call update function
			return update(entity);
		}else{
			// Call create function
			return create(entity);
		}
	}
	
	/**
	 * Retrieves all active entities ("estado" is 1). 
	 */
	public List<T> findAllActive(){
		// Filters
		Map<String,Object> mFilter = new HashMap<String, Object>();
		mFilter.put("estado", 1);

		return getDAO().queryForFieldValuesArgs(mFilter);
	}
	
	/**
	 * Retrieves all inactive entities ("estado" is 1). 
	 */
	public List<T> findAllInactive(){
		// Filters
		Map<String,Object> mFilter = new HashMap<String, Object>();
		mFilter.put("estado", 0);
		getDAO().getDataClass().getSimpleName();

		return getDAO().queryForFieldValuesArgs(mFilter);
	}
	

	/**
	 * 
	 */
	protected RuntimeExceptionDao<T, Integer> getDAO(){
		return databaseHelper.getRuntimeExceptionDao(domainClass);
	}

	/**
	 * Method to return the class of the domain object
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getDomainClass() {
		if (null == domainClass) {
			ParameterizedType thisType = (ParameterizedType) getClass()
					.getGenericSuperclass();
			domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
		}
		return domainClass;
	}
}