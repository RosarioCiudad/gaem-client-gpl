package coop.tecso.daa.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import coop.tecso.daa.base.DAAApplication;
import coop.tecso.daa.domain.base.AbstractEntity;

/**
 * 
 * @author tecso.coop
 *
 * @param <T>
 */
public abstract class GenericDAOX<T extends AbstractEntity, ID extends Number> extends BaseDaoImpl<T, ID> {

	protected GenericDAOX(ConnectionSource connectionSource,
			DatabaseTableConfig<T> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	protected final String LOG_TAG = getDataClass().getSimpleName()+"DAO";
	protected Context context;
	protected DAAApplication appState;

//	private Class<T> domainClass = getDomainClass();
//	private OrmLiteSqliteOpenHelper databaseHelper;


//	/**
//	 * 
//	 */
//	public T findById(int id) {
//		return getDAO().queryForId(id);
//	}
//
//	/**
//	 * 
//	 */
//	public int delete(T entity) {
//		return getDAO().delete(entity);
//	}
//
//	/**
//	 * 
//	 */
//	public int delete(Collection<T> entities) {
//		return getDAO().delete(entities);
//	}
	
	/**
	 * 
	 */
	public int deleteAll() {
		
		DeleteBuilder<T, ID> entityDb = deleteBuilder();
		try {
			return delete(entityDb.prepare());
		} catch (SQLException e) {
			Log.e(LOG_TAG, "**ERROR**", e);
			return 0;
		}
	}
	
//	/**
//	 * 
//	 */
//	public int refresh(T entity) {
//		return getDAO().refresh(entity);
//	}
//
//	/**
//	 * 
//	 */
//	public boolean idExists(int id) {
//		return getDAO().idExists(id);
//	}
	
	/**
	 * 
	 */
	public int create(T entity) {
		// Data for audit
		entity.setFechaUltMdf(new Date());
		entity.setUsuario(appState.getCurrentUser()==null?"anonimo":appState.getCurrentUser().getNombre());
		entity.setEstado(1);
		return create(entity);
	}

	/**
	 * 
	 */
	public int update(T entity) {
		// Data for audit
		entity.setFechaUltMdf(new Date());
		entity.setUsuario(appState.getCurrentUser()==null?"anonimo":appState.getCurrentUser().getNombre());
		return update(entity);
	}
	
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public CreateOrUpdateStatus createOrUpdate(T entity) throws SQLException {
		if (entity.getId() > 0) {
			// Call update function
			return new CreateOrUpdateStatus(false, true, update(entity));
		}else{
			// Call create function
			return new CreateOrUpdateStatus(true, false, create(entity));
		}
	}
	
	/**
	 * Retrieves all active entities ("estado" is 1). 
	 * @throws SQLException 
	 */
	public List<T> findAllActive() throws SQLException{
		// Filters
		Map<String,Object> mFilter = new HashMap<String, Object>();
		mFilter.put("estado", 1);

		return queryForFieldValuesArgs(mFilter);
	}
	
	/**
	 * Retrieves all inactive entities ("estado" is 0). 
	 * @throws SQLException 
	 */
	public List<T> findAllInactive() throws SQLException{
		// Filters
		Map<String,Object> mFilter = new HashMap<String, Object>();
		mFilter.put("estado", 0);

		return queryForFieldValuesArgs(mFilter);
	}
	

//	/**
//	 * 
//	 */
//	protected RuntimeExceptionDao<T, Integer> getDAO(){
//		return databaseHelper.getRuntimeExceptionDao(domainClass);
//	}

//	/**
//	 * Method to return the class of the domain object
//	 */
//	@SuppressWarnings("unchecked")
//	private Class<T> getDomainClass() {
//		if (null == domainClass) {
//			ParameterizedType thisType = (ParameterizedType) getClass()
//					.getGenericSuperclass();
//			domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
//		}
//		return domainClass;
//	}
}