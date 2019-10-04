package com.shop.weixin.api;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 微信服务注册验证接口
 * @ClassName VerificationCodeServiceApi
 * @Author Administrator
 * @CreateTime 2019/8/31 17:17
 * Version 1.0
 **/
@Api(tags = "微信服务注册验证接口")
public interface VerificationCodeServiceApi {
  @ApiOperation(value = "验证手机号和注册码是否正确")
  @ApiImplicitParams({
		  @ApiImplicitParam(name = "phone",value = "手机号",paramType = "query",dataType = "String",required = true),
		  @ApiImplicitParam(name = "weixinCode",value = "注册码",paramType = "query",dataType = "String",required = true)})
  @PostMapping("/verificationCode")
  public BaseResponse<JSONObject> verificationCode(@RequestParam("phone") String phone,@RequestParam("weixinCode") String registCode);
}
