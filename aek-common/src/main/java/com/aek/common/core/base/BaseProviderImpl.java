package com.aek.common.core.base;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.Constants;
import com.aek.common.core.util.DataUtil;
import com.aek.common.core.util.InstanceUtil;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 业务逻辑层基类<br/>
 * 继承基类后必须配置CacheConfig(cacheNames="")
 *
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:19:19
 */
public class BaseProviderImpl<T extends BaseModel> implements BaseProvider<T> {

	protected Logger logger = LogManager.getLogger(getClass());
	@Autowired
	protected BaseMapper<T> mapper;

	private static final String PAGE_NUM = "pageNum";
	private static final String PAGE_SIZE = "pageSize";
	private static final String ORDER_BY = "orderBy";

	/**
	 * 分页查询
	 */
	public static Page<Long> getPage(Map<String, Object> params) {

		Integer current = 1;
		Integer size = 10;
		String orderBy = "id";

		if (DataUtil.isNotEmpty(params.get(PAGE_NUM))) {
			current = Integer.valueOf((String) params.get(PAGE_NUM));
		}
		if (DataUtil.isNotEmpty(params.get(PAGE_SIZE))) {
			size = Integer.valueOf((String) params.get(PAGE_SIZE));
		}
		if (DataUtil.isNotEmpty(params.get(ORDER_BY))) {
			orderBy = (String) params.get(ORDER_BY);
			params.remove(ORDER_BY);
		}

		if (size == -1) {
			return new Page<Long>();
		}
		Page<Long> page = new Page<Long>(current, size, orderBy);
		page.setAsc(false);

		return page;
	}

	@Transactional
	public void delete(Long id) {
		try {
			mapper.deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Transactional
	public T insert(T record) {
		try {
			mapper.insert(record);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
		return record;
	}

	@Transactional
	public T update(T record) {
		try {
			if (record.getId() == null) {
				mapper.insert(record);
			} else {
				T org = queryById(record.getId());
				T update = InstanceUtil.getDiff(org, record);
				update.setId(record.getId());
				mapper.updateById(update);

			}
			record = mapper.selectById(record.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
		return record;
	}

	@Transactional
	public T queryById(Long id) {
		try {
			return mapper.selectById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取缓存键值
	 */
	protected String getCacheKey(Object id) {
		String cacheName = null;
		CacheConfig cacheConfig = getClass().getAnnotation(CacheConfig.class);
		if (cacheConfig == null || cacheConfig.cacheNames() == null || cacheConfig.cacheNames().length < 1) {
			cacheName = getClass().getName();
		} else {
			cacheName = cacheConfig.cacheNames()[0];
		}
		return new StringBuilder(Constants.CACHE_NAMESPACE).append(cacheName).append(":").append(id).toString();
	}

}
