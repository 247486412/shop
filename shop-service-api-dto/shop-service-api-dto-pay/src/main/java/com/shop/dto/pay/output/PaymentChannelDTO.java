package com.shop.dto.pay.output;

import lombok.Data;

/**
 * 支付通道dto,支付通道包括银联支付，微信支付，支付宝支付
 */
@Data
public class PaymentChannelDTO {
  /**
   * ID
   */
  private Integer id;
  /**
   * 渠道名称
   */
  private String channelName;
  /**
   * 渠道ID
   */
  private String channelId;
  /**
   * 商户id
   */
  private String merchantId;
  /**
   * 同步回调URL
   */
  private String syncUrl;
  /**
   * 异步回调URL
   */
  private String asynUrl;
  /**
   * 公钥
   */
  private String publicKey;
  /**
   * 私钥
   */
  private String privateKey;
  /**
   * 接口实现地址
   */
  private String classAddres;

}
