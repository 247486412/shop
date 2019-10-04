package com.shop.zuul.feign;

import com.shop.serviceApi.authorization.AuthorizationService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @ClassName AuthorizationServiceFeign
 * @Author Administrator
 * @CreateTime 2019/9/24 17:53
 * Version 1.0
 **/
@FeignClient("app-shop-authorization")
public interface AuthorizationServiceFeign extends AuthorizationService {

}
