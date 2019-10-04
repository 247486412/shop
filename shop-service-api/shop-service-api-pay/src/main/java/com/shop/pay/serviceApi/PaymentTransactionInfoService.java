package com.shop.pay.serviceApi;

import com.shop.base.BaseResponse;
import com.shop.dto.pay.output.PaymentTransactionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
public interface PaymentTransactionInfoService {
	@GetMapping("/tokenByPaymentTransaction")
	public BaseResponse<PaymentTransactionDTO> tokenByPaymentTransaction(@RequestParam("token") String token);
}
