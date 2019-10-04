package com.shop.serviceApi.feign;

import com.shop.member.serviceApi.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName MemberServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/1 21:03
 * Version 1.0
 **/
@FeignClient("app-shop-member")
public interface MemberServiceFeign extends MemberService {
}
