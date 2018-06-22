package com.aek.common.core.serurity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.serurity.enums.UserDetailResponseType;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.serurity.model.DataScopeBo;
import com.aek.common.core.serurity.model.RoleCustom;
import com.aek.common.core.serurity.model.SysDeptBo;
import com.aek.common.core.serurity.model.TokenInfo;
import com.aek.common.core.serurity.model.UserDetailResponse;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * Jwt Token工具类
 *
 * @author Honghui
 * @date 2017年7月5日
 * @version 1.0
 */
@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 3177875173057655206L;

	private static final String CLAIM_KEY_AUDIENCE = "aud";

	private static final String CLAIM_KEY_CREATED = "created";

	public static final String CLAIM_KEY_DEVICE_ID = "deviceId";

	private static final String AUDIENCE_UNKNOWN = "unknown";

	private static final String AUDIENCE_WEB = "web";

	private static final String AUDIENCE_MOBILE = "mobile";

	private static final String AUDIENCE_TABLET = "tablet";

	/**
	 * Token 类型
	 */
	public static final String TOKEN_TYPE_BEARER = "Bearer";

	/**
	 * 权限缓存前缀
	 */
	public static final String REDIS_PREFIX_AUTH = "auth:";

	/**
	 * 用户信息缓存前缀
	 */
	public static final String REDIS_PREFIX_USER = "user-details:";
	
	/**
	 * issuer
	 */
	private static final String OFFICIAL_ISSUER = "aek56.com";

	/**
	 * redis repository
	 */
	@Autowired
	private RedisRepository redisRepository;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	/**
	 * 获取设备
	 *
	 * @param  Token
	 * @return 设备信息
	 */
	public String getDeviceIdFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims != null ? claims.get(CLAIM_KEY_DEVICE_ID).toString() : null;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = getClaimsFromToken(token);
			audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	public Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);

	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		if (device.isNormal()) {
			audience = AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	private Boolean ignoreTokenExpiration(String token) {
		String audience = getAudienceFromToken(token);
		return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
	}

	/**
	 * 获得用户信息 Json 字符串
	 *
	 * @param token
	 *            Token
	 * @return String
	 */
	protected String getUserDetailsString(String token) {
		final String username = getUsernameFromToken(token);
		final String deviceid = getDeviceIdFromToken(token);
		String key = deviceid + ":" + REDIS_PREFIX_USER + username;
		return redisRepository.get(key);
	}

	/**
	 * 删除用户信息
	 *
	 * @param username
	 *            用户名
	 */
	public void delUserDetails(String username, Long tenantId) {
		String key = tenantId + ":" + REDIS_PREFIX_USER + username;
		redisRepository.del(key);
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	/**
	 * 获得用户信息 Json 字符串
	 *
	 * @param  Token
	 * @return UseDetailResponse
	 */
	public UserDetailResponse getAuthUserString(String token) {
		final String username = getUsernameFromToken(token);
		final String deviceid = getDeviceIdFromToken(token);
		String tokenKey = deviceid + ":" + REDIS_PREFIX_AUTH + username;
		String tokenKeyValue = redisRepository.get(tokenKey);
		TokenInfo tokenInfo = new Gson().fromJson(tokenKeyValue, TokenInfo.class);
		if (null == tokenInfo) {
			return new UserDetailResponse(false, UserDetailResponseType.INVALID_TOKEN.getCode());
		}
		String userDetailKey = tokenInfo.getTenantId() + ":" + REDIS_PREFIX_USER + username;
		String userDetailKeyValue = redisRepository.get(userDetailKey);
		if (null == userDetailKeyValue || tokenInfo.isNeedRefresh()) {
			return new UserDetailResponse(false, UserDetailResponseType.REDIRECT.getCode());
		}
		return new UserDetailResponse(true, UserDetailResponseType.SUCCESS.getCode(), userDetailKeyValue);
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	/**
	 * 生成 Token
	 *
	 * @param userDetails
	 *            用户信息
	 * @return String
	 */
	public String generateToken(AuthUser userDetails, Device device, List<Map<String,Object>> dataScopes) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_DEVICE_ID, userDetails.getDeviceId());
		
		String token = Jwts.builder().setClaims(claims).setSubject(userDetails.getMobile()) // 统一转换手机号
				.setAudience(generateAudience(device)).setIssuer(JwtTokenUtil.OFFICIAL_ISSUER).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();

		//统一改成手机号		
		String key = userDetails.getDeviceId() + ":" + REDIS_PREFIX_AUTH + userDetails.getMobile();
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setInTenantId(userDetails.getTenantId());// 所属租户
		tokenInfo.setTenantId(userDetails.getTenantId());// 当前租户
		tokenInfo.setToken(token);
		Date now = new Date();
		tokenInfo.setLoginTime(now);
		tokenInfo.setNeedRefresh(false);
		
		//添加数据权限
    	tokenInfo.setDataScope(convertDataScope(dataScopes));
    	
    	//添加用户所属部门
    	tokenInfo.setInDeptId(userDetails.getDeptId());
    	
    	//添加租户组织结构代码
    	tokenInfo.setTenantLicense(userDetails.getTenantLicense());
    	
    	//添加租户机构类型    [1=医疗机构,2=监管机构,3=设备供应商,4=设备维修商,5=配件供应商]
    	tokenInfo.setTenantType(userDetails.getTenantType());
    	
		redisRepository.setExpire(key, new Gson().toJson(tokenInfo), expiration);
		userDetails.setPassword(null);
		putUserDetails(userDetails, userDetails.getTenantId());
		return token;
	}
	
	/**
	 * 存储用户信息
	 *
	 * @param userDetails
	 *            用户信息
	 */
	public void putUserDetails(AuthUser userDetails, Long tenantId) {
		if (tenantId == null || tenantId < 1) {
			tenantId = userDetails.getTenantId();
		}
		String key = tenantId + ":" + REDIS_PREFIX_USER + userDetails.getMobile();

		redisRepository.setExpire(key, new Gson().toJson(userDetails), expiration);
	}
	
	/**
	 * 获取TokenInfo
	 * 
	 * @param  token
	 * @return TokenInfo
	 */
	public TokenInfo getTokenInfo(String token) {
		final String username = getUsernameFromToken(token);
		final String deviceid = getDeviceIdFromToken(token);
		String tokenKey = deviceid + ":" + REDIS_PREFIX_AUTH + username;
		String tokenKeyValue = redisRepository.get(tokenKey);
		return new Gson().fromJson(tokenKeyValue, TokenInfo.class);
	}

	/**
	 * 获取数据范围，格式：权限码：数据范围值
	 * 
	 * @param  token
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public DataScopeBo getDataScope(String permissionCode) {
		String token = WebSecurityUtils.getCurrentToken();
		final String username = getUsernameFromToken(token);
		final String deviceid = getDeviceIdFromToken(token);
		String tokenKey = deviceid + ":" + REDIS_PREFIX_AUTH + username;
		String tokenKeyValue = redisRepository.get(tokenKey);
		TokenInfo tokenInfo = new Gson().fromJson(tokenKeyValue, TokenInfo.class);
		
		List<Map<String, Object>> dataScopeMapList = tokenInfo.getDataScope();
		for (Map<String, Object> dataScopeMap : dataScopeMapList) {
			if(dataScopeMap.containsKey(permissionCode) && null != dataScopeMap.get(permissionCode)){
				Integer dataScope = dataScopeMap.get(permissionCode) == null ? null : ((Double)dataScopeMap.get(permissionCode)).intValue();
				List<Double> definedDeptIds = dataScopeMap.get("definedDeptIds") == null ? null : (List<Double>)dataScopeMap.get("definedDeptIds");
				List<Long> definedDeptIds2 = new ArrayList<Long>();
				if(null != definedDeptIds && definedDeptIds.size() > 0){
					for (Double deptId : definedDeptIds) {
						if(null != deptId){
							definedDeptIds2.add(deptId.longValue());
						}
					}
				}
				if(null != dataScope){
					DataScopeBo dataScopeBo = new DataScopeBo(dataScope,definedDeptIds2);
					return dataScopeBo;
				}
			}
		}
		return null;
	}
	
	/**
	 * 转换数据权限数据，组装为{{"SYS_USER_MANAGE":1,"definedDeptIds":[1,2,3]},{"SYS_USER_MANAGE":1,"definedDeptIds":[1,2,3]}}
	 * @param dataScopes
	 * @return
	 */
	public List<Map<String, Object>> convertDataScope(List<Map<String,Object>> dataScopes){
		List<Map<String,Object>> dataScopeMapList = new ArrayList<Map<String,Object>>();
    	for (Map<String, Object> map : dataScopes) {
    		map.put("custom_data", "{depts:[{deptId:1,deptName:111},{deptId:2,deptName:222}]}");
		}
    	for (Map<String, Object> dataScope : dataScopes) {
    		if(null != dataScope && null != dataScope.get("code") && null != dataScope.get("data_scope")){
    			Map<String, Object> dataScopeMap = new HashMap<>();
    			String codeValue = dataScope.get("code").toString();
    			Integer dataScopeValue = (Integer)dataScope.get("data_scope");
    			dataScopeMap.put(codeValue, dataScopeValue);
    			if(null != dataScope.get("custom_data")){
    				String customData = (String)dataScope.get("custom_data");
        			RoleCustom roleCustom = JSON.parseObject(customData, RoleCustom.class);
        			if(null != roleCustom){
        				List<SysDeptBo> sysDepts = roleCustom.getDepts();
        				List<Long> deptIds = Lists.newArrayList();
        				for (SysDeptBo sysDeptBo : sysDepts) {
        					deptIds.add(sysDeptBo.getDeptId());
    					}
        				dataScopeMap.put("definedDeptIds", deptIds);
        			}
    			}
    			dataScopeMapList.add(dataScopeMap);
    		}
		}
    	return dataScopeMapList;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}
}
