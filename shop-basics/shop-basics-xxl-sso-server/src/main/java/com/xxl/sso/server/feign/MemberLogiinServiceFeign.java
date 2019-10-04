package com.xxl.sso.server.feign;

import com.shop.member.serviceApi.MemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName MemberLogiinServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/7 10:48
 * Version 1.0
 **/
@FeignClient("app-shop-member")
public interface MemberLogiinServiceFeign extends MemberLoginService {
}
