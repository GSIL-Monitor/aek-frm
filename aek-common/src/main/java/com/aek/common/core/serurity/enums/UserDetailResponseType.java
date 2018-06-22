package com.aek.common.core.serurity.enums;

/**
 * 
 * 获取用户缓存返回类型302/450
 *
 * @author  Honghui
 * @date    2017年7月5日
 * @version 1.0
 */
public enum UserDetailResponseType {
	
	/**
	 * 302:重定向
	 */
	REDIRECT(302,"验证失败，302跳转"),
	
	/**
	 * 450:无效token
	 */
	INVALID_TOKEN(450,"无效的token"),
	
	/**
	 * 请求成功
	 */
	SUCCESS(200,"请求成功");
	
	private Integer code;  //返回码
	private String  msg;   //描述信息
	
	private UserDetailResponseType(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
