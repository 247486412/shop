package com.shop.pay.feign;

import com.shop.pay.serviceApi.PaymentTransactionInfoService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName PaymentTransactionInfoServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/15 9:38
 * Version 1.0
 **/
@FeignClient("app-shop-pay")
public interface PaymentTransactionInfoServiceFeign extends PaymentTransactionInfoService {
}
