package com.shop.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.shop.zuul.handler.ExecuteChain;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 网关过滤器
 * @ClassName GatewayFilter
 * @Author Administrator
 * @CreateTime 2019/9/22 14:33
 * Version 1.0
 **/
//继承ZuulFilter(网关过滤器)实现网关拦截策略
@Component
public class GatewayFilter extends ZuulFilter {
  //  @Autowired
//  private GatewayDirector gatewayDirector;
  @Autowired
  private ExecuteChain executeChain;



  //网关过滤类型
  // pre： 可以在请求被路由之前调用
  //route  在路由请求时候被调用
  //post： 在route和error过滤器之后被调用
  //error：处理请求时发生错误时被调用
  @Override
  public String filterType() {
	return "pre";
  }

  // 过滤器执行顺序,当一个请求在同一个阶段的时候存在多个过滤器的时候，多个过滤器执行顺序，数字越大，优先级越低
  @Override
  public int filterOrder() {
	return 0;
  }

  // 是否执行该过滤器，为true，说明需要执行,false不会执行,可以写逻辑判断
  @Override
  public boolean shouldFilter() {
	return true;
  }

  //过滤器拦截的具体逻辑代码
  @Override
  public Object run() throws ZuulException {
	RequestContext currentContext = RequestContext.getCurrentContext();
	HttpServletRequest request = currentContext.getRequest();
	HttpServletResponse response = currentContext.getResponse();
	//使用建造者模式,具体逻辑由内部实现
//	gatewayDirector.Director(currentContext, request, response);
	//使用责任链模式
	executeChain.execute(currentContext, request, response);
	//过滤参数
	filterParameters(request, currentContext);
	return null;
  }

  /**
   * 过滤参数
   */
  private void filterParameters(HttpServletRequest request, RequestContext requestContext) {
	Map<String, List<String>> requestQueryParams = requestContext.getRequestQueryParams();
	if (requestQueryParams == null) {
	  requestQueryParams = new HashMap<>();
	}
	Enumeration em = request.getParameterNames();
	while (em.hasMoreElements()) {
	  String name = (String) em.nextElement();
	  String value = request.getParameter(name);
	  ArrayList<String> arrayList = new ArrayList<>();
	  // 将参数转化为html参数 防止xss攻击 < 转为&lt;
	  arrayList.add(StringEscapeUtils.escapeHtml(value));
	  requestQueryParams.put(name, arrayList);
	}
	requestContext.setRequestQueryParams(requestQueryParams);
  }


}
