package com.shop.elk.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 
 * @description: 发送消息到kafka
 */
@Component
@Slf4j
public class KafkaSender<T> {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * kafka 发送消息
	 *
	 * @param obj
	 *            消息对象
	 */
	public void send(T obj) {
		String jsonObj = JSON.toJSONString(obj);
		log.info("------------ message = {}", jsonObj);

		// 发送消息  my_log最好写在配置文件内
		ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("my_log", jsonObj);
		future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
			@Override
			public void onFailure(Throwable throwable) {
				log.info("Produce: The message failed to be sent:" + throwable.getMessage());
			}

			@Override
			public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
				log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
			}
		});
	}
}
