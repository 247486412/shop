package com.shop.feign;

import com.shop.member.serviceApi.MemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName MemberLoginServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/4 16:16
 * Version 1.0
 **/
@FeignClient("app-shop-member")
public interface MemberLoginServiceFeign extends MemberLoginService {
}
