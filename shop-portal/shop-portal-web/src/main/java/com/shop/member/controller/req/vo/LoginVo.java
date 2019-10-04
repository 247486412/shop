package com.shop.member.controller.req.vo;

import lombok.Data;

/**
 * 登录参数
 */
@Data
public class LoginVo {

	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 手机密码
	 */
	private String password;
	/**
	 * 图形验证码
	 */
	private String graphicCode;

}
