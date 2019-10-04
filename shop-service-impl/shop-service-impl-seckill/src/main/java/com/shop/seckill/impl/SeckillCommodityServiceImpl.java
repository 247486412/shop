package com.shop.seckill.impl;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.core.token.GenerateToken;
import com.shop.core.utils.RedisUtil;
import com.shop.seckill.mq.producer.SeckillCommodityProducer;
import com.shop.seckill.serviceApi.SeckillCommodityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @ClassName SeckillCommodityServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/26 15:11
 * Version 1.0
 **/
@RestController
public class SeckillCommodityServiceImpl extends BaseApiService<JSONObject> implements SeckillCommodityService {
  @Autowired
  private GenerateToken generateToken;
  @Autowired
  private SeckillCommodityProducer seckillCommodityProducer;
  @Autowired
  private RedisUtil redisUtil;

  //此方法需使用事务保证一致性
  @Override
  @Transactional
  //使用该注解默认该接口会使用单独的一个线程池/服务隔离,超过配置的并发量后/默认为10,会执行seckillFallback方法，进行服务的降级
  @HystrixCommand(fallbackMethod = "seckillFallback")
  public BaseResponse<JSONObject> seckill(String phone, Long seckillId) {
	if (StringUtils.isBlank(phone)) {
	  return setResultError("手机号不能为空！");
	}
	if (seckillId == null) {
	  return setResultError("秒杀库存商品id不能为空！");
	}

	//让同一个手机号用户5秒才能抢购一次
	Boolean nx = redisUtil.setNx(phone, seckillId + "", 5L);
	if (!nx) {
	  return setResultError("5秒钟只能抢购一次,请稍后再试！");
	}

	//发送该秒杀商品id和手机号到mq中
	sendSeckillMsg(seckillId, phone);
	return setResultSuccess("正在排队中！");
  }

  public BaseResponse<JSONObject> seckillFallback(){
    return setResultError("服务器忙，请稍后再试！");
  }

  //异步发送秒杀消息,获取到秒杀token之后，异步放入mq中实现修改商品的库存
  @Async
  public void sendSeckillMsg(Long seckillId, String phone) {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("seckillId", seckillId);
	jsonObject.put("phone", phone);
	seckillCommodityProducer.send(jsonObject);

  }

  @Override//生成秒杀商品令牌,需要提前生成对应的秒杀令牌，令牌的数量需要和秒杀商品的库存数量一致
  public BaseResponse<JSONObject> addSeckillToken(Long seckillId, Long tokenQuantity) {
	if (seckillId == null) {
	  return setResultError("秒杀商品id不能为空！");
	}
	if (tokenQuantity == null) {
	  return setResultError("token数量不能为空！");
	}
	//生成令牌
	createSeckillToken(seckillId, tokenQuantity);
	return setResultSuccess("令牌已生成!");
  }

  //异步创建token ，一件秒杀商品对应多个token/多个库存
  @Async
  public void createSeckillToken(Long seckillId, Long tokenQuantity) {
	generateToken.createTokenList(Constants.SECKILL_TOKEN_PREFIX, seckillId + "", tokenQuantity);
  }

}
