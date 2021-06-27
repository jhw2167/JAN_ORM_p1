package com.revature.jack.ObjectMapper;

import java.util.HashMap;
import java.util.HashSet;

public class ObjectCache {
	private static HashSet<Object> objects = new HashSet<>();;
	private static HashMap<Class<?>, HashSet<Object>> cache = new HashMap<>();
//	/**
//	 * Retrieves the cache
//	 * @return Cache HashMap


	/**
	 * Adds Object obj into the cache
	 * 
	 * @param obj Object to be passed into the cache
	 */
	public void addObjectToCache(Object obj) {
		if (ObjectCache.cache.get(obj)==null) {
			objects.add(obj);
			cache.put(obj.getClass(), objects);
		} else {
			objects = ObjectCache.cache.get(obj.getClass());
			objects.add(obj);
			cache.put(obj.getClass(), objects);
		}

	}

	/**
	 * Deletes Object obj from the cache
	 * 
	 * @param obj Object to be deleted from the cache
	 */
	public void deleteObjectFromCache(Object obj) {
		if (ObjectCache.cache.get(obj.getClass()) == null) {
			
		}
		objects = ObjectCache.cache.get(obj.getClass());
		objects.remove(obj);
		cache.put(obj.getClass(), objects);
	}

	/**
	 * Return a hashset of objects in Cache of a given class
	 * 
	 * @param clazz Class that we want the objects for
	 * @return A hashset of objects of Class clazz in cache
	 */
	public HashSet<Object> getAllObjectsInCache(Class<?> clazz) {
		HashSet<Object> objects = ObjectCache.cache.get(clazz);
		return objects;
	}

	/**
	 * Deletes all objects in Cache of a given class
	 * 
	 * @param clazz Class that we want to remove all objects for
	 */
	public void deleteAllObjectsInCache(Class<?> clazz) {
		cache.put(clazz, null);
	}

}
