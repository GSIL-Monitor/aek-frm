package com.aek.common.core.email;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.aek.common.core.support.email.Email;
import com.aek.common.core.util.EmailUtil;

/**
 * 邮件发送
 * 
 * @author Mr.han
 *
 */
@Configuration
@ConfigurationProperties("email")
public class EmailSend {

	/**
	 * 服务器地址
	 */
	private String host = "smtp.exmail.qq.com";

	/**
	 * 登录名
	 */
	private String name = "noreply@aek56.com";

	/**
	 * 登录密码
	 */
	private String password = "aek.ser639";

	/**
	 * 发件人
	 */
	private String from = "爱医康";

	/**
	 * 邮件发送
	 * 
	 * @param email
	 * @return
	 */
	public boolean send(Email email) {

		if (StringUtils.isBlank(email.getHost())) {
			email.setHost(this.host);
		}
		if (StringUtils.isBlank(email.getName())) {
			email.setName(this.name);
			email.setPassword(this.password);
		}
		email.setFrom(from);

		return EmailUtil.sendEmail(email);
	}
}
