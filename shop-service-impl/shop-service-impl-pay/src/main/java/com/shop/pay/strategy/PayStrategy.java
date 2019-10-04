package com.shop.pay.strategy;

import com.shop.dto.pay.output.PaymentTransactionDTO;
import com.shop.pay.mapper.entity.PaymentChannelEntity;

/**
 * @Description
 * @ClassName PayStrategy
 * @Author Administrator
 * @CreateTime 2019/9/16 15:09
 * Version 1.0
 **/
public interface PayStrategy {
  /**
   *传入支付渠道和支付交易对象
   * @param paymentChannel
   * @param paymentTransaction
   * @return
   */
  public String toPayHtml(PaymentChannelEntity paymentChannel, PaymentTransactionDTO paymentTransaction);
}
