package com.shop.pay.impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.core.token.GenerateToken;
import com.shop.core.utils.SnowflakeIdUtil;
import com.shop.dto.pay.input.PayTokenDto;
import com.shop.pay.mapper.PaymentTransactionMapper;
import com.shop.pay.mapper.entity.PaymentTransactionEntity;
import com.shop.pay.serviceApi.PaymentTransactionTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description 支付交易服务实现
 * @ClassName createPayToken
 * @Author Administrator
 * @CreateTime 2019/9/15 9:47
 * Version 1.0
 **/
@RestController
public class PaymentTransactionTokenServiceImpl extends BaseApiService<JSONObject> implements PaymentTransactionTokenService {
  @Autowired
  private PaymentTransactionMapper paymentTransactionMapper;
  @Autowired
  private GenerateToken generateToken;

  @Override
  public BaseResponse<JSONObject> createPayToken(PayTokenDto payTokenDto) {
	String orderId = payTokenDto.getOrderId();
	if (StringUtils.isBlank(orderId)) {
	  return setResultError("订单号码不能为空！");
	}
	Long payAmount = payTokenDto.getPayAmount();
	if (payAmount == null) {
	  return setResultError("支付金额不能为空！");
	}
	Long userId = payTokenDto.getUserId();
	if (userId == null) {
	  return setResultError("用户不能为空！");
	}
	PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();
	paymentTransactionEntity.setOrderId(orderId);
	paymentTransactionEntity.setPayAmount(payAmount);
	paymentTransactionEntity.setUserId(userId);
	//使用雪花算法生成全局id，支付id是给页面展示的最终付款的id,因为订单真实id不能给到页面展示,订单id是下单时的id，下单后可能有折扣，优惠，活动等。
	paymentTransactionEntity.setPaymentId(SnowflakeIdUtil.getId());
	//将此待支付记录插入数据库中
	int result = paymentTransactionMapper.insertPaymentTransaction(paymentTransactionEntity);
	if (!daoResult(result)) {
	  return setResultError("系统错误！");
	}
	//获取插入的此记录的id，通过@Options注解把id赋给参数对象的id属性
	Long payId = paymentTransactionEntity.getId();
	if (payId == null) {
	  return setResultError("系统错误！");
	}
	//生成支付令牌
	String payTokenPrefix = Constants.PAY_TOKEN_PREFIX;
	String payToken = generateToken.createToken(payTokenPrefix, payId + "");
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("payToken", payToken);
	//返回支付令牌
	return setResultSuccess(jsonObject);
  }
}
