package com.shop.serviceApi.impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.core.utils.RedisUtil;
import com.shop.weixin.api.VerificationCodeServiceApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @ClassName VerificationCodeServiceApiImpl
 * @Author Administrator
 * @CreateTime 2019/8/31 20:02
 * Version 1.0
 **/
@RestController
public class VerificationCodeServiceApiImpl extends BaseApiService<JSONObject> implements VerificationCodeServiceApi {
  @Autowired
  private RedisUtil redisUtil;

  @Override
  public BaseResponse<JSONObject> verificationCode(String phone, String weixinCode) {
	if (StringUtils.isBlank(phone)) {
	  return setResultError("手机号不能为空！");
	}
	if (StringUtils.isBlank(weixinCode)) {
	  return setResultError("注册码不能为空");
	}
	String weixinPhone = Constants.WEIXINCODE_KEY + phone;
	String code = redisUtil.getString(weixinPhone);
	if (code == null) {
	  return setResultError("手机号错误或注册码无效或已过期");
	}
	if (!weixinCode.equals(code)) {
	  return setResultError("注册码输入错误！");
	}
	return setResultSuccess("注册码比对正确");
  }
}
