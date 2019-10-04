package com.shop.zuul.build.impl;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.sign.SignUtil;
import com.shop.zuul.build.GateWayBuild;
import com.shop.zuul.feign.AuthorizationServiceFeign;
import com.shop.zuul.mapper.BlacklistMapper;
import com.shop.zuul.mapper.entity.MeiteBlacklist;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description 参数验证建造者实现
 * @ClassName VerificationBuilder
 * @Author Administrator
 * @CreateTime 2019/9/22 18:26
 * Version 1.0
 **/
@Component
public class VerificationBuilder implements GateWayBuild {
  @Autowired
  private BlacklistMapper blacklistMapper;
  @Resource
  private AuthorizationServiceFeign authorizationServiceFeign;

  @Override
  public Boolean blackList(RequestContext currentContext, HttpServletRequest request, HttpServletResponse response) {
	String ipAddr = getIpAddr(request);
	//查询黑名单表,如果此请求被添加进了黑名单就不允许访问,网站访问量大黑名单存放在redis或apollo内
	MeiteBlacklist blacklist = blacklistMapper.findBlacklist(ipAddr);
	if (blacklist != null) {
	  insufficientAuthority(currentContext, "permission denied");
	  return false;
	}
	return true;
  }

  @Override
  public Boolean verifySign(RequestContext currentContext, HttpServletRequest request) {
//请求参数Map转换为验证签名Map,false表示不转码为UTF-8
	Map<String, String> verifyMap = SignUtil.toVerifyMap(request.getParameterMap(), false);
	//对参数进行验签,需要先使用SignUtil.sign对参数进行加签,sign方法添加了timestamp和sign两个参数
	//加签时使用MD5对请求参数加mykey123456(盐)形成加密签名为sign参数的值,
	// 此处验签时获取sign参数的值使用MD5对请求参数(排除了sign参数)加mykey123456(盐)的值和sign参数的值进行比对,比对失败说明数据被篡改
	if (!SignUtil.verify(verifyMap)) {
	  resultError(currentContext, "sign fail");
	  return false;
	}
	return true;
  }

  @Override
  public Boolean apiAuthority(RequestContext currentContext, HttpServletRequest request) {
	String servletPath = request.getServletPath();
	//如果url以public开头就需要有accessToken才能访问,别人使用我们接口时必须传accessToken才能访问
	if (!servletPath.substring(0, 7).equals("/public")) {
	  return true;
	}
	String accessToken = request.getParameter("accessToken");
	if (StringUtils.isEmpty(accessToken)) {
	  resultError(currentContext, "AccessToken cannot be empty");
	  return false;
	}
	// 调用接口验证accessToken是否失效
	BaseResponse<JSONObject> appInfo = authorizationServiceFeign.getAppInfo(accessToken);
	if (!isSuccess(appInfo)) {
	  resultError(currentContext, appInfo.getMsg());
	  return false;
	}
	return true;
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

  // ip地址存在一个问题,401没有访问权限
  private void insufficientAuthority(RequestContext ctx, String errorMsg) {
	baseResultErrorBase(ctx, 401, errorMsg);
  }

  private void resultError(RequestContext ctx, String errorMsg) {
	baseResultErrorBase(ctx, 500, errorMsg);
  }

  private void baseResultErrorBase(RequestContext ctx, int code, String errorMsg) {
	ctx.setResponseStatusCode(500);
	// 网关响应为false 不会转发服务
	ctx.setSendZuulResponse(false);
	ctx.setResponseBody(errorMsg);
  }
}
