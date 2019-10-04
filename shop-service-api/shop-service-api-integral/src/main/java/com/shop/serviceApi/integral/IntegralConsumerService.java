package com.shop.serviceApi.integral;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Headers;

import java.io.IOException;
import java.util.Map;

/**
 * @Description 增加积分接口,使用mq
 * @ClassName IntegralConsumerService
 * @Author Administrator
 * @CreateTime 2019/9/20 19:52
 * Version 1.0
 **/
public interface IntegralConsumerService {
  public void addIntegral(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException;
}
