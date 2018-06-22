package com.aek.common.core.serurity.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TokenInfo {
	
	private String token;             //token

	private Long tenantId;            //当前租户 ID

	private Long inTenantId;          //所属租户ID
	
	private String tenantLicense;   //当前机构组织机构代码
	
	private Integer tenantType;     //当前机构类型 

	private String deviceType;        //设置类型

	private Date loginTime;           //登录时间  
	
	private boolean needRefresh;      //是否需要刷新
	
	private Long inDeptId;            //所属部门ID
	
	private List<Map<String,Object>> dataScope; //数据范围集合
	
    private List<Long> availableTenants;  //可用结构

	public List<Long> getAvailableTenants() {
		return availableTenants;
	}

	public void setAvailableTenants(List<Long> availableTenants) {
		this.availableTenants = availableTenants;
	}

	public boolean isNeedRefresh() {
		return needRefresh;
	}

	public void setNeedRefresh(boolean needRefresh) {
		this.needRefresh = needRefresh;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getInTenantId() {
		return inTenantId;
	}

	public void setInTenantId(Long inTenantId) {
		this.inTenantId = inTenantId;
	}

	public Long getInDeptId() {
		return inDeptId;
	}

	public void setInDeptId(Long inDeptId) {
		this.inDeptId = inDeptId;
	}

	public List<Map<String, Object>> getDataScope() {
		return dataScope;
	}

	public void setDataScope(List<Map<String, Object>> dataScope) {
		this.dataScope = dataScope;
	}

	public String getTenantLicense() {
		return tenantLicense;
	}

	public void setTenantLicense(String tenantLicense) {
		this.tenantLicense = tenantLicense;
	}

	public Integer getTenantType() {
		return tenantType;
	}

	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}
	
}
