package org.vpac.ndg.storage.dao;

public interface BasicDao<T> {
	T create(T ds);
	void update(T ds);
	void delete(T ds);
	T retrieve(String id);
}
