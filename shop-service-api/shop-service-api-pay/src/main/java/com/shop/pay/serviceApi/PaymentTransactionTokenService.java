package com.shop.pay.serviceApi;

import com.shop.base.BaseResponse;
import com.shop.dto.pay.input.PayTokenDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付交易服务接口
 */
public interface PaymentTransactionTokenService {
	/**
	 * 创建支付令牌
	 */
	@GetMapping("/cratePayToken")
	public BaseResponse<JSONObject> createPayToken(@Validated PayTokenDto payTokenDto);

}
