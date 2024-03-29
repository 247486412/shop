package com.shop.elk.aop;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.shop.elk.kafka.KafkaSender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: ELK拦截日志信息
 */
@Aspect
@Component
public class AopLogAspect {
  @Autowired
  private KafkaSender<JSONObject> kafkaSender;

  // 申明一个切点 里面是 execution表达式
  @Pointcut("execution(* com.shop.service.impl.*.*(..))")
  private void serviceAspect() {
  }

  // 请求method前打印内容
  @Before(value = "serviceAspect()")
  public void methodBefore(JoinPoint joinPoint) {
	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes();
	HttpServletRequest request = requestAttributes.getRequest();

	JSONObject jsonObject = new JSONObject();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	jsonObject.put("request_time", df.format(new Date()));
	jsonObject.put("request_url", request.getRequestURL().toString());
	jsonObject.put("request_method", request.getMethod());
	jsonObject.put("signature", joinPoint.getSignature());
	jsonObject.put("ip", request.getLocalAddr());
	jsonObject.put("port", request.getLocalPort());
	jsonObject.put("request_args", Arrays.toString(joinPoint.getArgs()));
	JSONObject requestJsonObject = new JSONObject();
	requestJsonObject.put("request", jsonObject);
	kafkaSender.send(requestJsonObject);

  }

  // 在方法执行完结后打印返回内容
  @AfterReturning(returning = "o", pointcut = "serviceAspect()")
  public void methodAfterReturing(Object o) {
	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes();
	HttpServletRequest request = requestAttributes.getRequest();
	JSONObject respJSONObject = new JSONObject();
	JSONObject jsonObject = new JSONObject();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	jsonObject.put("response_time", df.format(new Date()));
	jsonObject.put("ip", request.getLocalAddr());
	jsonObject.put("port", request.getLocalPort());
	jsonObject.put("response_content", JSONObject.toJSONString(o));
	respJSONObject.put("response", jsonObject);
	kafkaSender.send(respJSONObject);

  }
}
