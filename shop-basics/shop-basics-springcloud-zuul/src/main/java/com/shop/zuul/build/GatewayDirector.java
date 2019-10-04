package com.shop.zuul.build;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 建造者连接/管理者 类
 * @ClassName GatewayDirector
 * @Author Administrator
 * @CreateTime 2019/9/22 18:27
 * Version 1.0
 **/
@Component
public class GatewayDirector {
  @Resource(name = "verificationBuilder")
  private GateWayBuild gatewayBuild;

  //建造者模式可以让代码有一定顺序执行
  public void Director(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
    Boolean blackList = gatewayBuild.blackList(context, request, response);
    if (!blackList){
      return;
    }
    Boolean verifySign = gatewayBuild.verifySign(context, request);
    if (!verifySign){
      return;
    }
    Boolean authority = gatewayBuild.apiAuthority(context, request);
    if (!authority){
      return;
    }
  }
}
