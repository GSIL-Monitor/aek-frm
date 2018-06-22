package com.aek.common.core.sms;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 短信发送使用类
 * 
 * @author Mr.han
 *
 */
public class Sms {

	@Autowired
	private SmsSend smsSend;

	/**
	 * 发送验证码
	 * 
	 * @param mobile
	 * @param code
	 * @return
	 */
	public SmsResult sendCode(String mobile, String code) {
		return smsSend.sendCode(mobile, code);
	}

	/**
	 * 发送验证码 (异步发送)
	 * 
	 * @param mobile
	 * @param code
	 */
	public void sendCodeAsyn(final String mobile, final String code) {
		new Thread() {
			@Override
			public void run() {
				SmsResult result = smsSend.sendCode(mobile, code);
				if (result.getSuccess() != 1) {
					throw new RuntimeException("sms fail in send error: " + result.getMsg());
				}
			}
		}.start();
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
		return this.smsSend.sendLoginPwd(mobile, pwd, expire);
	}

	/**
	 * 发送登录密码(异步发送)
	 * 
	 * @param mobile
	 * @param pwd
	 * @param expire
	 *            有效期 分钟[1,2,3 等]
	 */
	public void sendLoginPwdAsyn(final String mobile, final String pwd, final Integer expire) {
		new Thread() {
			@Override
			public void run() {
				SmsResult result = smsSend.sendLoginPwd(mobile, pwd, expire);
				if (result.getSuccess() != 1) {
					throw new RuntimeException("sms fail in send error: " + result.getMsg());
				}
			}
		}.start();
	}

	/**
	 * 
	 * /** 生成随机数
	 * 
	 * @param length
	 *            随机数长度
	 * @return
	 */
	public final String randomCode(int length) {
		StringBuffer tempB = new StringBuffer(length);

		for (int i = 0; i < length; i++) {
			double d = Math.random();
			int index = (int) (Math.floor(d * length) / 1);
			tempB.append(index);
		}
		return tempB.toString();
	}

}
