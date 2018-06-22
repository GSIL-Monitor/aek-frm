package com.aek.common.core.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.aek.common.core.serurity.JwtTokenUtil;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.serurity.model.DataScopeBo;
import com.google.common.collect.Lists;

/**
 */
@Component
@Aspect
public class PreAuthorizeAspect {

	private static final Logger logger = Logger.getLogger(PreAuthorizeAspect.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	private String regex="\'(.*?)\'";
	private Pattern pattern = Pattern.compile(regex);

	@Pointcut("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
	public void aspect() {
	}

	/**
	 */
	@Before("aspect()")
	public void before(JoinPoint joinPoint) {
		Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
		logger.debug(method.getDeclaringClass().getName() + " - " + method.getName());
		try {
			PreAuthorize anno = method.getDeclaredAnnotation(PreAuthorize.class);
			if(StringUtils.isNotBlank(anno.value())){
				List<DataScopeBo> dataScopeBos = new ArrayList<DataScopeBo>();
				Matcher matcher = pattern.matcher(anno.value());
				while(matcher.find()){
					String permission = matcher.group().substring(1, matcher.group().length()-1);
					//Integer dataScope = jwtTokenUtil.getDataScope(permission);
					DataScopeBo dataScopeBo = jwtTokenUtil.getDataScope(permission);
					if( null != dataScopeBo){
						dataScopeBos.add(dataScopeBo);
					}
				}
				if(dataScopeBos.size() > 0){
					List<Integer> dataScopes = Lists.newArrayList();
					for (DataScopeBo dataScopeBo : dataScopeBos) {
						dataScopes.add(dataScopeBo.getDataScope());
					}
					//取最小数值权限
					Integer dataScope = Collections.min(dataScopes);
					WebSecurityUtils.getCurrentUser().setDataScope(dataScope);
					//设置自定义部门数据
					for (DataScopeBo dataScopeBo : dataScopeBos) {
						if(dataScope.equals(dataScopeBo.getDataScope())){
							WebSecurityUtils.getCurrentUser().setDefinedDeptIds(dataScopeBo.getDefinedDeptIds());
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("ex.", e);
		}
	}

	@After("aspect()")
	public void after(JoinPoint point) {
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		if(null != authUser){
			WebSecurityUtils.getCurrentUser().setDataScope(null);
			WebSecurityUtils.getCurrentUser().setDefinedDeptIds(null);
		}
	}
}
