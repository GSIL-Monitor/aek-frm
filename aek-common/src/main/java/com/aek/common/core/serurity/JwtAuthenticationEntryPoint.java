package com.aek.common.core.serurity;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * ExceptionTranslationFilter工作流程中的辅助类，在处理未认证用户的请求中，自定义下一步需要做些什么操作。
 *
 * @author  Honghui
 * @date    2017年7月5日
 * @version 1.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint,Serializable{

	private static final long serialVersionUID = 2591032178184809221L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
    	response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    	Map<String,String> retVal = new HashMap<>();
    	retVal.put("code", "401");
    	retVal.put("msg", "未登录.");
        response.getWriter().print(new Gson().toJson(retVal));
        response.getWriter().flush();
    }

}
