package coop.tecso.daa.domain.base;

import java.lang.reflect.Type;

/**
 * 
 * @author tecso.coop
 *
 */
public interface Jsoner {
	/**
	 * 
	 * @param obj
	 * @return
	 */
	String toJson(Object obj);
	
	/**
	 * 
	 * @param json
	 * @param klass
	 * @return
	 */
	<T> T fromJson(String json, Class<T> klass);
	
	/**
	 * 
	 * @param json
	 * @param typeOf
	 * @return
	 */
	<T> T fromJson(String json, Type typeOf);
}