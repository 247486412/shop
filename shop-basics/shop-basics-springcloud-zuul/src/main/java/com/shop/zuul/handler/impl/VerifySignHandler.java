package com.shop.zuul.handler.impl;

import com.netflix.zuul.context.RequestContext;
import com.shop.sign.SignUtil;
import com.shop.zuul.handler.AbstractGatewayHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description 验证签名拦截
 * @ClassName VerifySignHandler
 * @Author Administrator
 * @CreateTime 2019/9/25 18:53
 * Version 1.0
 **/
@Component
public class VerifySignHandler extends AbstractGatewayHandler {
  @Override
  public void invoke(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
	//请求参数Map转换为验证签名Map,false表示不转码为UTF-8
	Map<String, String> verifyMap = SignUtil.toVerifyMap(request.getParameterMap(), false);
	//对参数进行验签,需要先使用SignUtil.sign对参数进行加签,sign方法添加了timestamp和sign两个参数
	//加签时使用MD5对请求参数加mykey123456(盐)形成加密签名为sign参数的值,
	// 此处验签时获取sign参数的值使用MD5对请求参数(排除了sign参数)加mykey123456(盐)的值和sign参数的值进行比对,比对失败说明数据被篡改
	if (!SignUtil.verify(verifyMap)) {
	  resultError(context, "sign fail");
	  return;
	}
	nextGatewayHandler.invoke(context, request, response);
  }
}
