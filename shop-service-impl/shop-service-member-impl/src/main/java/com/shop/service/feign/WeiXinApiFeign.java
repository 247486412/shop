package com.shop.service.feign;

import com.shop.weixin.api.WeiXinApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-shop-weixin")
public interface WeiXinApiFeign extends WeiXinApi {
}
