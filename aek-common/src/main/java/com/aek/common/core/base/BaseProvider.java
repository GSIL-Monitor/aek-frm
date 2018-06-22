package com.aek.common.core.base;

public interface BaseProvider<T extends BaseModel> {

	void delete(Long id);

	public T insert(T record);

	T update(T record);

	T queryById(Long id);

}
