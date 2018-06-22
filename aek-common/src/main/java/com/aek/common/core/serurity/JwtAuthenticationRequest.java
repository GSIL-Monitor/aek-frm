package com.aek.common.core.serurity;

import java.io.Serializable;

/**
 * JwtAuthenticationRequest
 * 
 * @author Honghui
 * @date 2017年7月5日
 * @version 1.0
 */
public class JwtAuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 3574528526318907246L;

	private String username;

	private String password;

	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public JwtAuthenticationRequest() {
		super();
	}

	public JwtAuthenticationRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
