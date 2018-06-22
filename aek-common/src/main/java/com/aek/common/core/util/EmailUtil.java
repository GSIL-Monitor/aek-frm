package com.aek.common.core.util;

import org.apache.commons.lang3.StringUtils;

import com.aek.common.core.support.email.Email;
import com.aek.common.core.support.email.EmailSender;

/**
 * 发送邮件辅助类
 * 
 * @author ShenHuaJie
 * @version $Id: MailUtil.java, v 0.1 2014年12月4日 下午8:22:43 ShenHuaJie Exp $
 */
public final class EmailUtil {
	private EmailUtil() {
	}

	/**
	 * 发送邮件
	 */
	public static final boolean sendEmail(Email email) {
		// 初始化邮件引擎
		EmailSender sender = new EmailSender(email.getHost());
		sender.setNamePass(email.getName(), email.getPassword(), email.getKey());
		if (sender.setFrom(email.getFrom()) == false)
			return false;
		if (sender.setTo(email.getSendTo()) == false)
			return false;
		if (email.getCopyTo() != null && sender.setCopyTo(email.getCopyTo()) == false)
			return false;
		if (sender.setSubject(email.getTopic()) == false)
			return false;
		if (sender.setBody(email.getBody()) == false)
			return false;
		if (email.getFileAffix() != null) {
			for (int i = 0; i < email.getFileAffix().length; i++) {
				if (sender.addFileAffix(email.getFileAffix()[i]) == false)
					return false;
			}
		}
		// 发送
		return sender.sendout();
	}

	public static void main(String[] args) {

		Email email = new Email();
		email.setHost("imap.exmail.qq.com");
		email.setName("hanqinglin@aek56.com");
		email.setPassword("Hanqinlin8240846");
		email.setFrom("hanqinglin@aek56.com");

		email.setTopic("这里是主题");
		email.setSendTo("18972227683@163.com");
		email.setBody("<h1>这里是邮件测试</h1>" 
				+ "<h1>这里是邮件测试</h1>" 
				+ "<h1>这里是邮件测试</h1>" 
				+ "<h1>这里是邮件测试</h1>"
				+ "<a href='ss'>这里是a标签</a>"
				+ "<img src='http://oolexqe7v.bkt.clouddn.com/1496626852234/登录2_spec_spec.png'/>");
		EmailUtil.sendEmail(email);
	}
}
