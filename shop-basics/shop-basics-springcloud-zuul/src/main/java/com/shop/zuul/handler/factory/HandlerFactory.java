package com.shop.zuul.handler.factory;

import com.shop.bean.SpringContextUtil;
import com.shop.zuul.handler.AbstractGatewayHandler;
import com.shop.zuul.mapper.GatewayHandlerEntity;
import com.shop.zuul.mapper.GatewayHandlerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 责任链工厂类，通过数据库动态加载每个handler/拦截功能，如果不需要某个功能只需把is_open设为不开放
 * @ClassName HandlerFactory
 * @Author Administrator
 * @CreateTime 2019/9/25 19:21
 * Version 1.0
 **/
@Component
@Slf4j
public class HandlerFactory {
  @Autowired
  private GatewayHandlerMapper gatewayHandlerMapper;

  private AbstractGatewayHandler firstGatewayHandler;

  //获取每个bean的实例
  // 实际开发时最好把每个实现AbstractGatewayHandler的bean名称存到数据库中，使用循环获取每个bean调用下一个bean返回第一个bean
//  public static AbstractGatewayHandler getFirstHandler() {
//	AbstractGatewayHandler gatewayHandler1 = SpringContextUtil.getBean("blackListHandler");
//	AbstractGatewayHandler gatewayHandler2 = SpringContextUtil.getBean("verifySignHandler");
//	gatewayHandler1.invokeNext(gatewayHandler2);
//	AbstractGatewayHandler gatewayHandler3 = SpringContextUtil.getBean("authorityTokenHandler");
//	gatewayHandler2.invokeNext(gatewayHandler3);
//	return gatewayHandler1;
//  }
  public AbstractGatewayHandler getFirstHandler() {
	if (this.firstGatewayHandler != null) {
	  return this.firstGatewayHandler;
	}
	//获取第一个handler，没有上一个handler的就是第一个,没有下一个handler的就是最后一个
	GatewayHandlerEntity firstGatewayHandlerEntity = gatewayHandlerMapper.getFirstGatewayHandler();
	if (firstGatewayHandlerEntity == null) {
	  return null;
	}
	//获取handler的bean的id
	String firstHandlerId = firstGatewayHandlerEntity.getHandlerId();
	if (StringUtils.isBlank(firstHandlerId)) {
	  return null;
	}
	//通过spring实例化该handler, 直接把第一个Handler 放入到缓存中..
	firstGatewayHandler = SpringContextUtil.getBean(firstHandlerId);
	if (firstGatewayHandler == null) {
	  return null;
	}
	//获取下一个HandlerId
	String nextHandlerId = firstGatewayHandlerEntity.getNextHandlerId();
	// 记录每一次循环控制的hanlder
	AbstractGatewayHandler tempGatewayHandler = firstGatewayHandler;
	while (!StringUtils.isEmpty(nextHandlerId)) {
	  // 3.查询数据库下一个Handler信息
	  GatewayHandlerEntity nextGatewayHandlerEntity = gatewayHandlerMapper.getByHandler(nextHandlerId);
	  if (nextGatewayHandlerEntity == null) {
		break;
	  }
	  // 根据id从spring中获取下一个handler bean实例
	  String gatewayHandlerId = nextGatewayHandlerEntity.getHandlerId();
	  AbstractGatewayHandler nextGatewayHandler = SpringContextUtil.getBean(gatewayHandlerId);
	  // 设置指向下一个handerl
	  tempGatewayHandler.invokeNext(nextGatewayHandler);
	  tempGatewayHandler = nextGatewayHandler;
	  // 执行下一个hanlder
	  nextHandlerId = nextGatewayHandlerEntity.getNextHandlerId();
	}
	return firstGatewayHandler;
  }

}
