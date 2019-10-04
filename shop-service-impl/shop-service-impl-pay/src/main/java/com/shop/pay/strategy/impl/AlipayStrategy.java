package com.shop.pay.strategy.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.shop.dto.pay.output.PaymentTransactionDTO;
import com.shop.pay.mapper.entity.PaymentChannelEntity;
import com.shop.pay.strategy.PayStrategy;

import java.math.BigDecimal;

/**
 * @Description 支付宝支付渠道实现
 * @ClassName AlipayStrategy
 * @Author Administrator
 * @CreateTime 2019/9/16 15:15
 * Version 1.0
 **/
public class AlipayStrategy implements PayStrategy {
  @Override
  public String toPayHtml(PaymentChannelEntity paymentChannel, PaymentTransactionDTO paymentTransaction) {
//获得初始化的AlipayClient
	AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

	//设置请求参数
	AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
	alipayRequest.setReturnUrl(AlipayConfig.return_url);
	alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

	//商户订单号，商户网站订单系统中唯一订单号，必填
	String out_trade_no = new String(paymentTransaction.getPaymentId());
	//付款金额，必填
	String total_amount = changeF2Y(String.valueOf(paymentTransaction.getPayAmount()));
	//订单名称，必填
	String subject = new String("测试商品！");
	//商品描述，可空
	String body = new String("测试商品！");

	alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
			+ "\"total_amount\":\"" + total_amount + "\","
			+ "\"subject\":\"" + subject + "\","
			+ "\"body\":\"" + body + "\","
			+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

	//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
	//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
	//		+ "\"total_amount\":\""+ total_amount +"\","
	//		+ "\"subject\":\""+ subject +"\","
	//		+ "\"body\":\""+ body +"\","
	//		+ "\"timeout_express\":\"10m\","
	//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
	//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

	//请求
	try {
	  String htmlResult = alipayClient.pageExecute(alipayRequest).getBody();
	  //输出
	  return htmlResult;
	} catch (AlipayApiException e) {
	  e.printStackTrace();
	  return null;
	}
  }
  public static String changeF2Y(String amount) {
	return new BigDecimal(amount).divide(new BigDecimal("100")).toString();
  }
}
