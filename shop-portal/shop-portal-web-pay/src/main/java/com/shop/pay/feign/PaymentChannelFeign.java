package com.shop.pay.feign;

import com.shop.pay.serviceApi.PaymentChannelService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("app-shop-pay")
public interface PaymentChannelFeign extends PaymentChannelService {

}
