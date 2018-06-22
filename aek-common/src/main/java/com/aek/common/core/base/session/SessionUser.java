package com.aek.common.core.base.session;

import java.io.Serializable;

/**
 * 会话user信息
 * 
 * @author Mr.han
 *
 */
public class SessionUser implements Serializable {

	private static final long serialVersionUID = 2523315933629179853L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 所在机构Id
	 */
	private Long tenantId;

	/**
	 * 所在机构名称
	 */
	private String tenantName;

	/**
	 * 姓名
	 */
	private String realName;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 当前所在机构Id(用户切换机构时动态切换)
	 */
	private Long currentTenantId;

	/**
	 * 当前所在机构名称
	 */
	private String currentTenantName;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the tenantName
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * @param tenantName
	 *            the tenantName to set
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName
	 *            the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the deptId
	 */
	public Long getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId
	 *            the deptId to set
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getCurrentTenantId() {
		return currentTenantId;
	}

	public void setCurrentTenantId(Long currentTenantId) {
		this.currentTenantId = currentTenantId;
	}

	public String getCurrentTenantName() {
		return currentTenantName;
	}

	public void setCurrentTenantName(String currentTenantName) {
		this.currentTenantName = currentTenantName;
	}

}
