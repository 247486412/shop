package com.shop.pay.factory;

import com.shop.pay.strategy.PayStrategy;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 支付策略工厂
 * @ClassName StrategyFactory
 * @Author Administrator
 * @CreateTime 2019/9/16 15:22
 * Version 1.0
 **/
public class StrategyFactory {
  public static Map<String, PayStrategy> strategyMap = new ConcurrentHashMap<String, PayStrategy>();

  //传入支付策略实现地址,返回对应的支付策略实现类
  public static PayStrategy getPayStrategy(String classAddr) {
	if (StringUtils.isBlank(classAddr)) {
	  return null;
	}
	//使用map集合维护支付策略，避免每次都初始化
	PayStrategy strategy = strategyMap.get(classAddr);
	if (strategy != null) {
	  return strategy;
	}
	try {
	  Class<?> classObj = Class.forName(classAddr);
	  PayStrategy payStrategy = (PayStrategy) classObj.newInstance();
	  strategyMap.put(classAddr, payStrategy);
	  return payStrategy;
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return null;
  }
}
