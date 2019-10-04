package com.shop.pay.impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.dto.pay.output.PaymentTransactionDTO;
import com.shop.pay.factory.StrategyFactory;
import com.shop.pay.mapper.PaymentChannelMapper;
import com.shop.pay.mapper.entity.PaymentChannelEntity;
import com.shop.pay.serviceApi.PayContextService;
import com.shop.pay.strategy.PayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 根据不同的支付渠道调用不同的支付接口, 提交不同的支付表单
 * @ClassName PayContextServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/16 12:35
 * Version 1.0
 **/
@RestController
public class PayContextServiceImpl extends BaseApiService<JSONObject> implements PayContextService {
  @Autowired
  private PaymentChannelMapper paymentChannelMapper;
  @Autowired
  private PaymentTransactionInfoImpl paymentTransactionInfo;

  @Override
  public BaseResponse<JSONObject> toPayHtml(String channelId, String payToken) {
	//根据渠道id获取对应的支付渠道
	PaymentChannelEntity paymentChannelEntity = paymentChannelMapper.selectBychannelId(channelId);
	if (paymentChannelEntity == null) {
	  return setResultError("没有对应的支付渠道！");
	}
	BaseResponse<PaymentTransactionDTO> paymentTransaction = paymentTransactionInfo.tokenByPaymentTransaction(payToken);
	if (!isSuccess(paymentTransaction)) {
	  return setResultError(paymentTransaction.getMsg());
	}
	PaymentTransactionDTO PaymentTransactionData = paymentTransaction.getData();
	String classAddres = paymentChannelEntity.getClassAddres();
	PayStrategy payStrategy = StrategyFactory.getPayStrategy(classAddres);
	//根据支付渠道和支付交易对象获取对应的支付表单
	String payHtml = payStrategy.toPayHtml(paymentChannelEntity, PaymentTransactionData);
	if (payHtml==null){
	  return setResultError("支付参数有误或参数组装失败!");
	}
	JSONObject data = new JSONObject();
	data.put("payHtml", payHtml);
	return setResultSuccess(data);
  }
}
