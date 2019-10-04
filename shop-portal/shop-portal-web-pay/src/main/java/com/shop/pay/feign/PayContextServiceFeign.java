package com.shop.pay.feign;

import com.shop.pay.serviceApi.PayContextService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName PayContextServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/16 20:25
 * Version 1.0
 **/
@FeignClient("app-shop-pay")
public interface PayContextServiceFeign extends PayContextService {
}
