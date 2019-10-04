package com.shop.dto.pay.output;

import java.util.Date;

import lombok.Data;

/**
 *支付事件dto
 */
@Data
public class PaymentTransactionDTO {

  /**
   * 主键ID
   */
  private Long id;
  /**
   * 支付金额
   */
  private Long payAmount;
  /**
   * 支付状态;0待支付1支付完成 2支付超时3支付失败
   */
  private Integer paymentStatus;
  /**
   * 用户ID
   */
  private Long userId;
  /**
   * 商品订单号码
   */
  private String orderId;
  /**
   * 创建时间
   */
  private Date createdTime;
  /**
   * 第三方支付id 支付宝、银联支付成功后返回的订单id
   */
  private String partyPayId;
  /**
   * 使用雪花算法生产 本支付系统支付id
   */
  private String paymentId;

}
