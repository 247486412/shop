package com.shop.constants;

/**
 * @Description
 * @ClassName MqConstants
 * @Author Administrator
 * @CreateTime 2019/9/20 18:55
 * Version 1.0
 **/
public interface MqConstants {
  //添加积分队列
  String INTEGRAL_ADD_QUEUE = "integral_add_queue";
  //支付补偿队列
  String PAYMENT_COMPENSATE_QUEUE = "payment_compensate_queue";
  //积分交换机
  String INTEGRAL_EXCHANGE_NAME = "integral_exchange";
  //积分路由键
  String ROUTINGKEY_INTEGRAL = "routingKeyIntegral";


}
