package com.shop.pay.config;

import com.shop.constants.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Description RabbitMq队列和交换机配置,积分和支付补偿队列绑定同一个交换机
 * @ClassName RabbitMqConfig
 * @Author Administrator
 * @CreateTime 2019/9/20 17:47
 * Version 1.0
 **/
@Configuration
public class RabbitMqConfig {

  //定义添加积分队列
  @Bean
  public Queue integralAddQueue() {
	return new Queue(MqConstants.INTEGRAL_ADD_QUEUE);
  }

  //定义支付补偿队列
  @Bean
  public Queue paymentCompensateQueue() {
	return new Queue(MqConstants.PAYMENT_COMPENSATE_QUEUE);
  }

  //定义积分交换机,使用路由模式(直连交换机),一个交换机对应多个队列,根据路由键转发消息
  @Bean
  public DirectExchange integralExchange() {
	return new DirectExchange(MqConstants.INTEGRAL_EXCHANGE_NAME);
  }

  //绑定队列到交换机
  @Bean
  public Binding bindingExchangeIntegralAddQueue() {
	return BindingBuilder.bind(integralAddQueue()).to(integralExchange()).with(MqConstants.ROUTINGKEY_INTEGRAL);
  }

  @Bean
  public Binding bindingExchangePaymentCompensateQueue() {
	return BindingBuilder.bind(paymentCompensateQueue()).to(integralExchange()).with(MqConstants.ROUTINGKEY_INTEGRAL);
  }


}
