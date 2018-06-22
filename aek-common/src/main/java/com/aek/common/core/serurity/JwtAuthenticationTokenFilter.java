package com.aek.common.core.serurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aek.common.core.Result;
import com.aek.common.core.serurity.enums.UserDetailResponseType;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.serurity.model.UserDetailResponse;
import com.google.gson.Gson;

/**
 * Jwt拦截器
 *
 * @author Honghui
 * @date 2017年7月5日
 * @version 1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.header}")
	private String tokenHeader;
	
	@Value("${cookie.domain:aek.com}")
	private String cookieDomain;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOGGER.debug("1.Step into JwtAuthenticationTokenFilter");
		LOGGER.debug("2.Current request Uri : " + request.getRequestURI());
		String authToken = request.getHeader(this.tokenHeader);
		if(StringUtils.isBlank(authToken)){
    		Cookie[] cookies = request.getCookies();
    		if(cookies!=null&&cookies.length>0){
    			for (Cookie cookie : cookies) {
					if(tokenHeader.equals(cookie.getName())){
						authToken = cookie.getValue();
						break;
					}
				}
    		}
    	}
		LOGGER.debug("3.Current token : " + authToken);
		String username = jwtTokenUtil.getUsernameFromToken(authToken);
		LOGGER.debug("4.Check authentication for user : " + username);
		if(StringUtils.isNotBlank(username) && null == SecurityContextHolder.getContext().getAuthentication()){
			UserDetailResponse userDetailResponse = jwtTokenUtil.getAuthUserString(authToken);
			if(userDetailResponse.isSuccess() && UserDetailResponseType.SUCCESS.getCode() == userDetailResponse.getCode()){
				String userDetailString = userDetailResponse.getUserDetail();
				UserDetails userDetails = null;
				if(StringUtils.isNotBlank(userDetailString)){
					LOGGER.debug("#.Current user : [" + username + "] 's userdetail cache is " + userDetailString);
					userDetails = new Gson().fromJson(userDetailString, AuthUser.class);
				}
				if(jwtTokenUtil.validateToken(authToken, userDetails)){
					UsernamePasswordAuthenticationToken authentication = 
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.debug("#.Authenticated user :[" + username + "], setting security context.");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}else{
				Result<String> result = null;
				if(UserDetailResponseType.INVALID_TOKEN.getCode() == userDetailResponse.getCode()){
					//result = new Result<String>(String.valueOf(userDetailResponse.getCode()),UserDetailResponseType.INVALID_TOKEN.getMsg()); 
					LOGGER.debug("#.Current token : [" + authToken + "] is a invalid token.");
					//450无效token时，清除浏览器cookie token
					Cookie[] cookies = request.getCookies();
		    		if(cookies!=null&&cookies.length>0){
		    			for (Cookie cookie : cookies) {
							if(tokenHeader.equals(cookie.getName())){
								authToken = cookie.getValue();
								Cookie newCookie=new Cookie(tokenHeader,null); 
								newCookie.setMaxAge(0);        //立即删除型
								newCookie.setPath("/");        //项目所有目录均有效，这句很关键，否则不敢保证删除
								newCookie.setDomain(cookieDomain);
								response.addCookie(newCookie); //重新写入，将覆盖之前的
							}
						}
		    		}
				}else if(UserDetailResponseType.REDIRECT.getCode() == userDetailResponse.getCode()){
					result = new Result<String>(String.valueOf(userDetailResponse.getCode()),UserDetailResponseType.REDIRECT.getMsg());
					LOGGER.debug("#.Current user : [" + username + "] 's userdetail cache is null,302 redirect.");
				}
				if(null != result){
					response.setHeader("Content-type", "application/json;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(new Gson().toJson(result));
                    response.getWriter().flush();
                    return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}
