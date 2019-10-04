package com.shop.dto.pay.input;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PayTokenDto {
	/**
	 * 支付金额
	 */
	@NotNull(message = "支付金额不能为空")
	private Long payAmount;
	/**
	 * 订单号码
	 */
	@NotNull(message = "订单号码不能为空")
	private String orderId;

	/**
	 * userId
	 */
	@NotNull(message = "userId不能空")
	private Long userId;
}
