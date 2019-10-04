package com.shop.zuul.build;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 使用建造者模式
 * @ClassName GateWayBuild
 * @Author Administrator
 * @CreateTime 2019/9/22 17:55
 * Version 1.0
 **/
public interface GateWayBuild {
  /**
   * 黑名单拦截
   */
  Boolean blackList(RequestContext currentContext, HttpServletRequest request, HttpServletResponse response);

  /**
   * 参数验证
   */
  Boolean verifySign(RequestContext currentContext, HttpServletRequest request);

  /**
   * api权限控制
   */
  Boolean apiAuthority(RequestContext currentContext, HttpServletRequest request);
}
