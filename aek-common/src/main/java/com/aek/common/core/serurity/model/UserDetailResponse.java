package com.aek.common.core.serurity.model;

/**
 * 
 * 获取用户userdetail信息返回值
 *
 * @author Honghui
 * @date 2017年7月5日
 * @version 1.0
 */
public class UserDetailResponse {
	
	private boolean success = true; //是否成功获取到用户detail缓存信息，当code=302或450时，success=false

	private Integer code;       //返回码，如302：用户usedetail缓存不存在，需重新拉取权限；450：token失效

	private String userDetail; //用户userdetail信息json格式字符串

	public UserDetailResponse(boolean success, Integer code, String userDetail) {
		this.success = success;
		this.code = code;
		this.userDetail = userDetail;
	}

	public UserDetailResponse(boolean success, Integer code) {
		this.success = success;
		this.code = code;
	}

	public String getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(String userDetail) {
		this.userDetail = userDetail;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
