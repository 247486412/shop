package com.shop.integral.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.shop.constants.MqConstants;
import com.shop.integral.service.mapper.IntegralMapper;
import com.shop.integral.service.mapper.entity.IntegralEntity;
import com.shop.serviceApi.integral.IntegralConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Description 积分消费者服务实现
 * @ClassName IntegralConsumerServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/20 20:19
 * Version 1.0
 **/
@Component
@Slf4j
public class IntegralConsumerServiceImpl implements IntegralConsumerService {
  @Autowired
  private IntegralMapper integralMapper;

  @Override
  //监听添加积分队列
  @RabbitListener(queues = MqConstants.INTEGRAL_ADD_QUEUE)
  public void addIntegral(Message message, Map<java.lang.String, Object> headers, Channel channel) throws IOException {
	try {
	  //获取消息内容和消息id
	  String msg = new String(message.getBody(), StandardCharsets.UTF_8);
	  JSONObject jsonObject = JSONObject.parseObject(msg);
	  String paymentId = jsonObject.getString("paymentId");
	  if (StringUtils.isEmpty(paymentId)) {
		log.error("支付id不能为空:{}", paymentId);
		basicAck(message, channel);
		return;
	  }
	  //使用paymentId查询是否已经增加过积分 网络重试间隔
	  IntegralEntity integralEntity = integralMapper.findIntegral(paymentId);
	  if (integralEntity != null) {
		log.error("已经增加过积分:{}", paymentId);
		// 已经增加过积分，签收消息,通知MQ不要在继续重试。
		basicAck(message, channel);
		return;
	  }
	  Integer userId = jsonObject.getInteger("userId");
	  if (userId == null) {
		log.error(">>>>paymentId:{},对应的用户userId参数为空", paymentId);
		basicAck(message, channel);
		return;
	  }
	  Long integral = jsonObject.getLong("integral");
	  if (integral == null) {
		log.error(">>>>paymentId:{},对应的用户integral参数为空", integral);
		basicAck(message, channel);
		return;
	  }
	  IntegralEntity integralObj = new IntegralEntity();
	  integralObj.setPaymentId(paymentId);
	  integralObj.setIntegral(integral);
	  integralObj.setUserId(userId);
	  integralObj.setAvailability(1);
	  // 插入到数据库中
	  int insertIntegral = integralMapper.insertIntegral(integralObj);
	  if (insertIntegral > 0) {
		// 手动签收消息,通知mq服务器端删除该消息
		basicAck(message, channel);
	  }
	  //采用重试机制,如果数据库插入失败消息没有手动签收,会一直重试执行此方法,重试次数和间隔时间需配置

	} catch (IOException e) {
	  log.error("ERROR MSG:{}", e.getMessage());
	  //出现异常也签收此消息
	  basicAck(message, channel);
	}


  }

  private void basicAck(Message message, Channel channel) throws IOException {
	// 手动消息确认,默认自动确认/签收消息,手动签收需要配置acknowledge-mode: manual,
	// true可以一次性确认消息id小于等于当前值的所有消息,false只确认/签收当前消息
	channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	//手动消息拒绝/否定，1.为消息id/标识,2.是否拒绝消息id小于等于当前值的所有消息，3.被拒绝的消息是否重新入队列,false为丢弃该消息
	//消息丢弃，过期，队列满了等情况,消息会进入死信队列,对消息进入处理
//	channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
  }
}
