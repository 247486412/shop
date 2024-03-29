package com.shop.serviceApi.mp.handler;

import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.core.utils.RedisUtil;
import com.shop.core.utils.RegexUtils;
import com.shop.dto.output.UserOutputDTO;
import com.shop.serviceApi.feign.MemberServiceFeign;
import com.shop.serviceApi.mp.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
@SuppressWarnings("static-access")
public class MsgHandler extends AbstractHandler {
  // 用户发送手机验证码提示
  @Value("${shop.weixin.registration.code.message}")
  private String registrationCodeMessage;
  // 默认用户发送验证码提示
  @Value("${shop.weixin.default.registration.code.message}")
  private String defaultRegistrationCodeMessage;

  @Autowired
  private RedisUtil redisUtil;
  @Autowired
  private MemberServiceFeign memberServiceFeign;

  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
								  WxSessionManager sessionManager) {

	if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
	  // TODO 可以选择将消息保存到本地
	}

	// 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
	try {
	  if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
			  && weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
		return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
				.toUser(wxMessage.getFromUser()).build();
	  }
	} catch (WxErrorException e) {
	  e.printStackTrace();
	}
	// 1. 获取微信客户端发送的消息
	String mobile = wxMessage.getContent();
	// 2.使用正则表达式验证消息是否为手机号码格式
	if (RegexUtils.checkMobile(mobile)) {
	  BaseResponse<UserOutputDTO> userInfo = memberServiceFeign.existMobile(mobile);
	  if (userInfo.getCode().equals(Constants.HTTP_RES_CODE_200)){
		return new TextBuilder().build("手机号码"+mobile+"已注册", wxMessage, weixinService);
	  }
	  if (!userInfo.getCode().equals(Constants.HTTP_RES_CODE_203)){
	    return new TextBuilder().build("用户信息已存在", wxMessage, weixinService);
	  }

	  // 3.如果是手机号码格式的话,随机生产4位数字注册码
	  int registCode = registCode();
//	  %s相当于registrationCodeMessage.replace("%s", registCode + "");
	  String content = String.format(registrationCodeMessage, registCode);
	  // 将注册码存入在redis中 key为手机号码
	  redisUtil.setString(Constants.WEIXINCODE_KEY + mobile, registCode + "", Constants.WEIXINCODE_TIMEOUT);
	  return new TextBuilder().build(content, wxMessage, weixinService);
	}
	// 否则情况下返回默认消息 调用第三方机器人接口
	return new TextBuilder().build(defaultRegistrationCodeMessage, wxMessage, weixinService);

  }

  // 获取注册码
  private int registCode() {
	int registCode = (int) (Math.random() * 9000 + 1000);
	return registCode;
  }

}
