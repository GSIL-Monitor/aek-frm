package com.aek.common.core.serurity.model;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Security User
 *
 * @author zj@aek56.com
 */
public abstract class AbstractAuthUser implements UserDetails {

	private static final long serialVersionUID = -6074035771649874852L;

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
