package com.shop.feign;

import com.shop.member.serviceApi.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName MemberServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/4 17:26
 * Version 1.0
 **/
@FeignClient("app-shop-member")
public interface MemberServiceFeign extends MemberService {
}
