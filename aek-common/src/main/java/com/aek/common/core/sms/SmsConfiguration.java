package com.aek.common.core.sms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信配置类
 * 
 * @author Mr.han
 *
 */
@Configuration
public class SmsConfiguration {

	@Bean
	public Sms sms() {
		return new Sms();
	}

	/**
	 * 基础发送他工具类
	 * 
	 * @param smsConfig
	 * @return
	 */
	@Bean
	public SmsSend SmsSend(SmsConfig smsConfig) {
		return new SmsSend(smsConfig);
	}
}
