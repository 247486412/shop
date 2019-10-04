package com.shop.serviceApi.feign;

import com.shop.weixin.api.VerificationCodeServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName VerificationCodeServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/1 22:37
 * Version 1.0
 **/
@FeignClient("app-shop-weixin")
public interface VerificationCodeServiceFeign extends VerificationCodeServiceApi {
}
