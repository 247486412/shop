package com.shop.member.serviceApi;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import com.shop.dto.input.UserInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @ClassName MemberRegisterService
 * @Author Administrator
 * @CreateTime 2019/9/1 9:27
 * Version 1.0
 **/
@Api(tags = "会员注册接口")
public interface MemberRegisterService {
  @PostMapping("/register")
  @ApiOperation(value = "会员用户注册信息接口")
  BaseResponse<JSONObject> register(@RequestBody UserInputDTO userInputDTO,
									@RequestParam("registCode") String registCode);


}
