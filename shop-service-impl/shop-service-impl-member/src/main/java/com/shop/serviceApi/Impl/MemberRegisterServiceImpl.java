package com.shop.serviceApi.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.constants.Constants;
import com.shop.core.utils.MD5Util;
import com.shop.core.utils.RedisUtil;
import com.shop.dto.input.UserInputDTO;
import com.shop.member.serviceApi.MemberRegisterService;
import com.shop.serviceApi.mapper.PO.UserPO;
import com.shop.serviceApi.mapper.UserMapper;
import com.shop.weixin.api.VerificationCodeServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @ClassName MemberRegisterServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/1 18:00
 * Version 1.0
 **/
@RestController
@Slf4j
public class MemberRegisterServiceImpl extends BaseApiService<JSONObject> implements MemberRegisterService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private VerificationCodeServiceApi verificationCodeServiceApi;
  @Autowired
  private RedisUtil redisUtil;

  @Override
  public BaseResponse<JSONObject> register(@RequestBody UserInputDTO userInputDTO, String registCode) {
	// 1.验证参数,注册时不用输入用户名,使用手机号注册
//	String userName = userInputDTO.getUserName();
//	if (StringUtils.isEmpty(userName)) {
//	  return setResultError("用户名称不能为空!");
//	}
	String mobile = userInputDTO.getMobile();
	if (StringUtils.isEmpty(mobile)) {
	  return setResultError("手机号码不能为空!");
	}
	String password = userInputDTO.getPassword();
	if (StringUtils.isEmpty(password)) {
	  return setResultError("密码不能为空!");
	}
	BaseResponse<JSONObject> verificationCode = verificationCodeServiceApi.verificationCode(mobile, registCode);
	if (!verificationCode.getCode().equals(Constants.HTTP_RES_CODE_200)) {
	  return setResultError(verificationCode.getMsg());
	}

	String newPassWord = MD5Util.MD5(password);
	// 将密码采用MD5加密
	userInputDTO.setPassword(newPassWord);
	//将dto转化为do
	UserPO userPO = MyBeanUtils.copyProperties(userInputDTO, UserPO.class);
	int registerResult = userMapper.register(userPO);
	if (!(registerResult == 1)) {
	  return setResultError("注册失败");
	}
	String weixinPhone = Constants.WEIXINCODE_KEY + mobile;
	redisUtil.delete(weixinPhone);
	return setResultSuccess("注册成功");

  }

}
