package com.shop.seckill.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.shop.base.BaseApiService;
import com.shop.seckill.mapper.OrderMapper;
import com.shop.seckill.mapper.SeckillMapper;
import com.shop.seckill.mapper.entity.OrderEntity;
import com.shop.seckill.mapper.entity.SeckillEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Description
 * @ClassName SeckillCommodityConsumer
 * @Author Administrator
 * @CreateTime 2019/9/28 18:23
 * Version 1.0
 **/
@Component
@Slf4j
public class SeckillCommodityConsumer extends BaseApiService<JSONObject> {
  @Autowired
  private SeckillMapper seckillMapper;
  @Autowired
  private OrderMapper orderMapper;

  @RabbitListener(queues = "modify_inventory_queue")
  @Transactional
  public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws UnsupportedEncodingException {
	String messageId = message.getMessageProperties().getMessageId();
	String msg = new String(message.getBody(), "UTF-8");
	JSONObject jsonObject = JSONObject.parseObject(msg);
	Long seckillId = jsonObject.getLong("seckillId");
	SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
	if (seckillEntity == null) {
	  log.info("商品信息不存在！seckillId:{}", seckillId);
	  return;
	}
	Long version = seckillEntity.getVersion();
	//扣除库存,库存减一,版本号加一
	int inventoryDeduction = seckillMapper.inventoryDeduction(seckillId, version);
	if (!daoResult(inventoryDeduction)) {
	  log.info("秒杀失败，扣除库存失败！seckillId:{}", seckillId);
	  return;
	}
	OrderEntity orderEntity = new OrderEntity();
	String phone = jsonObject.getString("phone");
	orderEntity.setUserPhone(phone);
	orderEntity.setSeckillId(seckillId);
	int insertOrder = orderMapper.insertOrder(orderEntity);
	if (!daoResult(insertOrder)) {
	  log.info("添加秒杀商品订单失败！seckillId:{}", seckillId);
	  return;
	}
	log.info("秒杀成功seckillId:{},订单信息为:{}", seckillId, orderEntity);
  }
}
