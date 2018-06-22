package com.aek.common.core.aspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 获取数据源
 */
public class ChooseDataSource extends AbstractRoutingDataSource {
	public static final Map<String, List<String>> METHOD_TYPE = new HashMap<String, List<String>>();

	// 获取数据源名称
	protected Object determineCurrentLookupKey() {
		return HandleDataSource.getDataSource();
	}

	// 设置方法名前缀对应的数据源
	private void setMethodType(Map<String, String> map) {
		for (String key : map.keySet()) {
			List<String> v = new ArrayList<String>();
			String[] types = map.get(key).split(",");
			for (String type : types) {
				if (StringUtils.isNotBlank(type)) {
					v.add(type);
				}
			}
			METHOD_TYPE.put(key, v);
		}
	}

	@Autowired
	@Qualifier("readDataSource")
	private DruidDataSource readDataSource;

	@Autowired
	@Qualifier("writeDataSource")
	private DruidDataSource writeDataSource;
	
	@PostConstruct
	private void initTargetDataSource(){
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		targetDataSources.put("read", readDataSource);
		targetDataSources.put("write", writeDataSource);
		this.setTargetDataSources(targetDataSources);
		this.setDefaultTargetDataSource(writeDataSource);
		Map<String, String> methodMap = new HashMap<String, String>();
		methodMap.put("read", ",get,select,count,list,query,find,");
		methodMap.put("write", ",add,insert,create,update,delete,remove,");
		this.setMethodType(methodMap);
	}
}
