package com.shop.pay.impl;

import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.core.token.GenerateToken;
import com.shop.dto.pay.output.PaymentTransactionDTO;
import com.shop.pay.mapper.PaymentTransactionMapper;
import com.shop.pay.mapper.entity.PaymentTransactionEntity;
import com.shop.pay.serviceApi.PaymentTransactionInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @ClassName PaymentTransactionInfoImpl
 * @Author Administrator
 * @CreateTime 2019/9/16 10:34
 * Version 1.0
 **/
@RestController
public class PaymentTransactionInfoImpl extends BaseApiService<PaymentTransactionDTO> implements PaymentTransactionInfoService {
  @Autowired
  private GenerateToken generateToken;
  @Autowired
  private PaymentTransactionMapper paymentTransactionMapper;
  @Override
  //根据此token获取支付信息
  public BaseResponse<PaymentTransactionDTO> tokenByPaymentTransaction(String token) {
    if(StringUtils.isBlank(token)){
      return setResultError("token不能为空！");
	}
	String payId = generateToken.getToken(token);
    if (StringUtils.isBlank(payId)){
      return setResultError("该token无效或已过期！");
	}
//    generateToken.removeToken(token);
	Long payID = Long.parseLong(payId);
	PaymentTransactionEntity paymentTransactionEntity = paymentTransactionMapper.selectById(payID);
	if (paymentTransactionEntity==null){
	  return setResultError("该交易记录不存在！");
	}
	PaymentTransactionDTO paymentTransactionDTO = MyBeanUtils.copyProperties(paymentTransactionEntity, PaymentTransactionDTO.class);
	return setResultSuccess(paymentTransactionDTO);
  }
}
