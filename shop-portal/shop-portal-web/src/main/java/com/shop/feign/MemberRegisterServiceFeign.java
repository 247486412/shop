package com.shop.feign;

import com.shop.member.serviceApi.MemberRegisterService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName MemberRegisterServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/4 12:45
 * Version 1.0
 **/
@FeignClient("app-shop-member")
public interface MemberRegisterServiceFeign extends MemberRegisterService {
}
