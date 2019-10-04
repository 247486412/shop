package com.shop.zuul.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.shop.base.BaseResponse;
import com.shop.zuul.feign.AuthorizationServiceFeign;
import com.shop.zuul.handler.AbstractGatewayHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @ClassName AuthorityTokenHandler
 * @Author Administrator
 * @CreateTime 2019/9/25 18:59
 * Version 1.0
 **/
@Component
public class AuthorityTokenHandler extends AbstractGatewayHandler {
  @Resource
  private AuthorizationServiceFeign authorizationServiceFeign;

  @Override
  public void invoke(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
	String servletPath = request.getServletPath();
	//如果url以public开头就需要有accessToken才能访问,别人使用我们接口时必须传accessToken才能访问
	if (!servletPath.substring(0, 7).equals("/public")) {
	  //如果是/public开头说明访问的接口需要accessToken才能访问
	  return;
	}
	String accessToken = request.getParameter("accessToken");
	if (StringUtils.isEmpty(accessToken)) {
	  //token为空直接响应错误信息,结束该方法
	  resultError(context, "AccessToken cannot be empty");
	  return;
	}
	// 调用接口验证accessToken是否失效
	BaseResponse<JSONObject> appInfo = authorizationServiceFeign.getAppInfo(accessToken);
	if (!isSuccess(appInfo)) {
	  //token无效,返回错误信息,结束方法
	  resultError(context, appInfo.getMsg());
	}
	//说明token有效,不用直接响应,网关会把请求转发到具体的接口服务

	//如果还有其他的网关拦截功能就继续执行下一个
  }
}
