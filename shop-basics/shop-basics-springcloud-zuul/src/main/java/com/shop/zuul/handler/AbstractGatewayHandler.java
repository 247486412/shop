package com.shop.zuul.handler;

import com.netflix.zuul.context.RequestContext;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 使用责任链模式, 链式调用网关拦截功能, 只要其中一个功能返回false就直接响应错误, 不去执行具体服务 此处使用抽象类, 因为都需要公共的方法
 * @ClassName GatewayHandler
 * @Author Administrator
 * @CreateTime 2019/9/25 17:55
 * Version 1.0
 **/
@Component
public abstract class AbstractGatewayHandler {
  protected AbstractGatewayHandler nextGatewayHandler;

  //每个功能具体的业务逻辑
  public abstract void invoke(RequestContext context, HttpServletRequest request, HttpServletResponse response);

  //调用下一个功能,传入具体的实现
  public void invokeNext(AbstractGatewayHandler nextGatewayHandler) {
	this.nextGatewayHandler = nextGatewayHandler;
  }

  //获取AbstractGatewayHandler对象
  public AbstractGatewayHandler getNextGatewayHandler() {
	return this.nextGatewayHandler;
  }


  // 接口直接返回true 或者false
  public Boolean isSuccess(BaseResponse<?> baseResp) {
	if (baseResp == null) {
	  return false;
	}
	if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
	  return false;
	}
	return true;
  }

  // 401没有访问权限
  public void insufficientAuthority(RequestContext ctx, String errorMsg) {
	responseResult(ctx, 401, errorMsg);
  }

  //系统错误,代码有问题
  public void resultError(RequestContext ctx, String errorMsg) {
	responseResult(ctx, 500, errorMsg);
  }

  public void responseResult(RequestContext ctx, int code, String errorMsg) {
	ctx.setResponseStatusCode(code);
	// 网关响应为false 不会转发服务
	ctx.setSendZuulResponse(false);
	ctx.setResponseBody(errorMsg);
  }
}
