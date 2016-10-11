package coop.tecso.aid.dao;

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
import com.j256.ormlite.stmt.QueryBuilder;

import coop.tecso.aid.application.AIDigitalApplication;
import coop.tecso.aid.persistence.DatabaseHelper;
import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author leonardo.fagnano
 *
 * @param <T>
 */
public abstract class GenericDAO<T extends AbstractEntity> {

	protected final String LOG_TAG = getDomainClass().getSimpleName();
	protected Context context;
	protected AIDigitalApplication appState;

	private Class<T> domainClass = getDomainClass();
	private OrmLiteSqliteOpenHelper databaseHelper;

	/**
	 * Constructor
	 */
	public GenericDAO(Context context) {
		this.context = context;
		this.databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		this.appState = (AIDigitalApplication) context.getApplicationContext();
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
		entity.setUsuario(appState.getCurrentUser().getNombre());
		entity.setEstado(1);
		return getDAO().create(entity);
	}
	
	/**
	 * 
	 */
	public int createOrUpdate(T entity) {
		// Data for audit
		entity.setFechaUltMdf(new Date());
		entity.setUsuario(appState.getCurrentUser().getNombre());
		entity.setEstado(1);
		return getDAO().createOrUpdate(entity).getNumLinesChanged();
	}

	/**
	 * 
	 */
	public int update(T entity) {
		// Data for audit
		entity.setFechaUltMdf(new Date());
		entity.setUsuario(appState.getCurrentUser().getNombre());
		return getDAO().update(entity);
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
	 * Retrieves all inactive entities ("estado" is 0). 
	 */
	public List<T> findAllInactive(){
		// Filters
		Map<String,Object> mFilter = new HashMap<String, Object>();
		mFilter.put("estado", 0);

		return getDAO().queryForFieldValuesArgs(mFilter);
	}
	
	/**
	 * Retrieves all active entities ("estado" is 1). 
	 */
	public Long countOfActive(){
		// Filters
		QueryBuilder<T, Integer> queryBuilder = getDAO().queryBuilder();
		try {
			return queryBuilder.where().eq("estado", 1).countOf();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			return null;
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public int deleteAllInactive() throws SQLException {

		DeleteBuilder<T, Integer> deleteBuilder = getDAO().deleteBuilder();
		deleteBuilder.where().eq("estado", 0);

		return  delete(deleteBuilder.prepare());
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