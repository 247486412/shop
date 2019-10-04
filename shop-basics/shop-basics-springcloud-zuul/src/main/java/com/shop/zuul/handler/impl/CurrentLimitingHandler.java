package com.shop.zuul.handler.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.context.RequestContext;
import com.shop.core.token.GenerateToken;
import com.shop.zuul.handler.AbstractGatewayHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Description 秒杀限流
 * @ClassName CurrentLimitingHandler
 * @Author Administrator
 * @CreateTime 2019/9/29 16:40
 * Version 1.0
 **/
@Component
@Slf4j
public class CurrentLimitingHandler extends AbstractGatewayHandler {
  //每秒创建100个令牌放入令牌桶中,该数一般为服务器请求的并发量/qps,RateLimiter必须是全局,不然每次方法执行都会创建100个令牌
  //默认该数就是令牌桶的最大容量，令牌桶最大容量等于令牌存在的秒数乘以每秒生成的令牌数,因为内部令牌桶中令牌最多存放多少秒是写死的1.0,
  private RateLimiter rateLimiter = RateLimiter.create(100);//该方法底层是异步创建的令牌
  @Autowired
  private GenerateToken generateToken;

  @Override
  public void invoke(RequestContext context, HttpServletRequest request, HttpServletResponse response) {
	//从令牌桶中获取一个令牌,获取到返回true,反之false,param 1.等待指定时间内还获取不到令牌就返回false,一般为0,不需要等待,2.时间单位
	boolean tryAcquire = rateLimiter.tryAcquire(0, TimeUnit.SECONDS);
	if (!tryAcquire) {
	  resultError(context, "服务器忙,请稍后再试！");
	}
	// 2.使用redis限制用户访问频率
	String seckillId = request.getParameter("seckillId");
	//如果没有seckillId参数说明不是从秒杀页面请求来的,就不走下面的代码
	if (StringUtils.isBlank(seckillId)) {
	  getNextGatewayHandler().invoke(context, request, response);
	  return;
	}
	//移除redis集合中的左边第一个元素,并返回该元素,相当于获取该商品集合中的第一个token后并删除该token
	String token = generateToken.getTokenFromList(seckillId + "");
	//为空说明没有获取到秒杀商品令牌,如果该商品只有10个可以秒杀，就只需生成10个秒杀令牌,只有获取到秒杀令牌才能秒杀成功
	if (StringUtils.isBlank(token)) {
	  resultError(context, "该秒杀商品已售空,请下次再来！");
	}
	getNextGatewayHandler().invoke(context, request, response);
  }
}
