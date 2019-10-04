package com.shop.member.serviceApi;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import com.shop.dto.input.UserLoginInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description
 * @ClassName MemberLoginService
 * @Author Administrator
 * @CreateTime 2019/9/3 8:41
 * Version 1.0
 **/
@Api(tags = "用户登录接口")
public interface MemberLoginService {
  @ApiOperation("会员用户登录接口")
  @PostMapping("/login")
  public BaseResponse<JSONObject> login(UserLoginInputDTO userLoginInputDTO);
}
