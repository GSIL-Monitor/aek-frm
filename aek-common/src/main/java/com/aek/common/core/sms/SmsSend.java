package com.aek.common.core.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestOperations;

import com.alibaba.fastjson.JSON;

/**
 * 短信发送工具类
 * 
 * @author Mr.han
 *
 */
public class SmsSend {

	public SmsSend() {
		super();
	}

	public SmsSend(SmsConfig config) {
		super();
		this.setConfig(config);
	}

	/**
	 * 基础配置
	 */
	private SmsConfig config;

	@Autowired
	private RestOperations restOperations;

	/**
	 * 发送验证码
	 * 
	 * @param mobile
	 * @param code
	 * @return
	 */
	public SmsResult sendCode(String mobile, String code) {
		return this.sendMsg(mobile, code, null, this.config.getCodeTpl());
	}

	/**
	 * 发送登录密码
	 * 
	 * @param mobile
	 * @param pwd
	 * @param expire
	 * @return
	 */
	public SmsResult sendLoginPwd(String mobile, String pwd, Integer expire) {
		return this.sendMsg(mobile, pwd, expire, this.config.getLoginTpl());
	}

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 * @param code
	 * @param expire
	 * @return
	 */
	private SmsResult sendMsg(String mobile, String code, Integer expire, int tpl) {
		// 必要检查
		if (!RegexValidateUtil.checkCellphone(mobile)) {
			throw new RuntimeException("the mobile number is wrong");
		}
		if (null == code || "".equals(code)) {
			throw new RuntimeException("the code is nont empty");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usr", config.getUsr());
		params.put("psw", config.getPsw());
		params.put("authkey", config.getAuthkey());
		params.put("mobile", mobile);
		params.put("tpl", tpl);

		// 模板不同参数不同
		if (tpl == config.getLoginTpl()) {
			params.put("text[vPass]", code);
			params.put("text[vExpire]", expire);

		} else if (tpl == config.getCodeTpl()) {
			params.put("text[vCode]", code);
		}

		// url 参数处理
		String url = this.urlDispose(this.getConfig().getUrl(), params);
		String result = this.restOperations.getForObject(url, String.class, params);
		return JSON.parseObject(result, SmsResult.class);
	}

	/**
	 * url处理
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private String urlDispose(String url, Map<String, Object> params) {
		StringBuilder builder = new StringBuilder(url);
		builder.append("?");
		Set<String> paramKey = params.keySet();
		for (String key : paramKey) {
			builder.append(key).append("=").append("{").append(key).append("}").append("&");
		}
		return builder.toString();
	}

	public SmsConfig getConfig() {
		return config;
	}

	public void setConfig(SmsConfig config) {
		this.config = config;
	}
}
