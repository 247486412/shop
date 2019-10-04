package com.shop.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import com.shop.pay.feign.PayContextServiceFeign;
import com.shop.web.base.BaseWebController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @ClassName ChannelController
 * @Author Administrator
 * @CreateTime 2019/9/16 20:23
 * Version 1.0
 **/
@RestController
public class ChannelController extends BaseWebController {
  @Autowired
  private PayContextServiceFeign payContextServiceFeign;

  @RequestMapping("/channel")
  public String channel(String channelId, String payToken, Model model, HttpServletResponse response) {
	if (StringUtils.isBlank(channelId)) {
	  setErrorMsg(model, "支付渠道id不能为空！");
	  return ERROR_500_FTL;
	}
	if (StringUtils.isBlank(payToken)) {
	  setErrorMsg(model, "支付令牌不能为空！");
	  return ERROR_500_FTL;
	}
	BaseResponse<JSONObject> payHtml = payContextServiceFeign.toPayHtml(channelId, payToken);
	if(!isSuccess(payHtml)){
	  setErrorMsg(model, "系统错误！");
	  return ERROR_500_FTL;
	}
	JSONObject payHtmlData = payHtml.getData();
	String toPayHtml = payHtmlData.getString("payHtml");
	String html = toPayHtml.replace("\\", "");
	return html;
  }
}
