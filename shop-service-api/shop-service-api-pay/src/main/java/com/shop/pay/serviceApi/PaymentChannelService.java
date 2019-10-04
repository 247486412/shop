package com.shop.pay.serviceApi;

import java.util.List;

import com.shop.dto.pay.output.PaymentChannelDTO;
import org.springframework.web.bind.annotation.GetMapping;


public interface PaymentChannelService {
	/**
	 * 查询所有支付渠道
	 */
	@GetMapping("/selectAll")
	public List<PaymentChannelDTO> selectAll();
}
