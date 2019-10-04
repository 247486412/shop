package com.shop.zuul.handler.impl;

import com.netflix.zuul.context.RequestContext;
import com.shop.zuul.handler.AbstractGatewayHandler;
import com.shop.zuul.mapper.BlacklistMapper;
import com.shop.zuul.mapper.entity.MeiteBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 黑名单拦截
 * @ClassName BlackListHandler
 * @Author Administrator
 * @CreateTime 2019/9/25 18:05
 * Version 1.0
 **/
@Component
public class BlackListHandler extends AbstractGatewayHandler {
  @Autowired
  private BlacklistMapper blacklistMapper;

  @Override
  public void invoke(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
	String ipAddr = getIpAddr(request);
	//查询黑名单表,如果此请求被添加进了黑名单就不允许访问,网站访问量大黑名单存放在redis或apollo内
	MeiteBlacklist blacklist = blacklistMapper.findBlacklist(ipAddr);
	if (blacklist != null) {
	  insufficientAuthority(context, "permission denied");
	  return;
	}
	//执行下一个拦截功能
	nextGatewayHandler.invoke(context, request, response);
  }

  /**
   * 获取Ip地址
   * 由于请求如果经过转发后RemoteAddr就会变为转发的服务器ip,所以需要判断请求头中携带的客户端原始ip
   *
   * @param request
   * @return
   */
  public String getIpAddr(HttpServletRequest request) {
	String ip = request.getHeader("X-Forwarded-For");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	  ip = request.getHeader("Proxy-Client-IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	  ip = request.getHeader("WL-Proxy-Client-IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	  ip = request.getHeader("HTTP_CLIENT_IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	  ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	  ip = request.getRemoteAddr();
	}
	return ip;
  }

}
