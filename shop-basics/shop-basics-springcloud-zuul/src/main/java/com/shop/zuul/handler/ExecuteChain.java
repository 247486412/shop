package com.shop.zuul.handler;

import com.netflix.zuul.context.RequestContext;
import com.shop.zuul.handler.factory.HandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 执行handler
 * @ClassName ExecuteChain
 * @Author Administrator
 * @CreateTime 2019/9/25 19:21
 * Version 1.0
 **/
@Component
public class ExecuteChain {
  @Autowired
  private HandlerFactory handlerFactory;

  public void execute(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
	//获取第一个handler,执行第一个会链式调用后面的所有handler,直到return
	AbstractGatewayHandler firstHandler = handlerFactory.getFirstHandler();
	//由于数据库或者代码原因getFirstHandler()方法有可能返回null
	if (firstHandler == null) {
	  responseResult(context, "网关拦截功能异常！");
	}
	firstHandler.invoke(context, request, response);
  }

  public void responseResult(RequestContext context, String errorMsg) {
	context.setResponseStatusCode(500);
	// 网关响应为false 不会转发服务
	context.setSendZuulResponse(false);
	context.setResponseBody(errorMsg);
  }
}
