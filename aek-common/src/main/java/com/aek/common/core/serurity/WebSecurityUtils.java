package com.aek.common.core.serurity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.util.SpringWebUtil;

public final class WebSecurityUtils {

	private WebSecurityUtils() {
	}

	/**
	 * 获取当前登录者对象
	 */
	public static AuthUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof AuthUser) {
			AuthUser authUser = (AuthUser) principal;
			authUser.setAuthoritiesStr(authUser.getAuthoritiesStr());
			return authUser;
		}
		return null;
	}

	/**
	 * 获取当前token
	 */
	public static String getCurrentToken() {
		String tokenKey = "X-AEK56-Token";
		HttpServletRequest request = SpringWebUtil.getRequest();
		String token = request.getHeader(tokenKey );
		if(StringUtils.isBlank(token)){
    		Cookie[] cookies = request.getCookies();
    		if(cookies!=null&&cookies.length>0){
    			for (Cookie cookie : cookies) {
					if(tokenKey.equals(cookie.getName())){
						token = cookie.getValue();
						break;
					}
				}
    		}
    	}
		return token;
	}

}
