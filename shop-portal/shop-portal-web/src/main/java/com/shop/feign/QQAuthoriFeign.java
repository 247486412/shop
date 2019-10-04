package com.shop.feign;

import com.shop.member.serviceApi.QQAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName QQAuthoriFeign
 * @Author Administrator
 * @CreateTime 2019/9/5 10:07
 * Version 1.0
 **/
@FeignClient("app-shop-member")
public interface QQAuthoriFeign extends QQAuthoriService {
}
