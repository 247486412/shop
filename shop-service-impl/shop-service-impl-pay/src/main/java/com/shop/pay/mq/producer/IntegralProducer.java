package com.shop.pay.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.shop.constants.MqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Description 积分生产者
 * @ClassName IntegralProducer
 * @Author Administrator
 * @CreateTime 2019/9/20 18:42
 * Version 1.0
 **/
@Component
@Slf4j
public class IntegralProducer implements RabbitTemplate.ConfirmCallback {
  @Autowired
  private RabbitTemplate rabbitTemplate;

  //发送消息
  public void sendMsg(JSONObject jsonObject) {
	String jsonString = jsonObject.toJSONString();
	String paymentId = jsonObject.getString("paymentId");
	// 封装消息,设置消息id为支付id
	Message message = MessageBuilder.withBody(jsonString.getBytes())
			.setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(paymentId).build();
	/**
	 * mandatory：交换器无法根据自身类型和路由键找到一个符合条件的队列时的处理方式
	 * true：RabbitMQ会调用Basic.Return命令将消息返回给生产者
	 * false：RabbitMQ会把消息直接丢弃
	 */
	this.rabbitTemplate.setMandatory(true);
	//是否需要confirm回调,在当前类回调
	this.rabbitTemplate.setConfirmCallback(this);
	// 构建回调返回的数据
	CorrelationData correlationData = new CorrelationData(jsonString);
	//发送到integral_exchange交换机,使用routingKeyIntegral路由键
	rabbitTemplate.convertAndSend(MqConstants.INTEGRAL_EXCHANGE_NAME, MqConstants.ROUTINGKEY_INTEGRAL, message, correlationData);

  }

  // 生产消息确认机制 生产者往服务器端发送消息的时候，采用应答机制,消息没有成功进入交换机ack为false
  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
	String jsonString = correlationData.getId();
	System.out.println("消息id:" + correlationData.getId());
	//消息发送成功ack为ture,correlationData为发送的相关数据,getId()获取发送的消息id
	if (ack) {
	  log.info("使用MQ消息确认机制确保消息一定要投递到MQ中成功");
	  return;
	}
	JSONObject jsonObject = JSONObject.parseObject(jsonString);
	// 生产者消息投递失败的话，采用递归重试机制,为避免一直重试，可以使用全局变量限制重试次数,分布式时使用redis等
	sendMsg(jsonObject);
	log.info("使用MQ消息确认机制投递到MQ中失败");
  }
}
