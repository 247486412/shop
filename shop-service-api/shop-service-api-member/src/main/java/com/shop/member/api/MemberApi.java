package com.shop.member.api;

import com.shop.entity.WeiXinEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
@Api(tags = "会员服务接口")
public interface MemberApi {
  @ApiOperation(value = "会员服务调用微信服务")
  @GetMapping("/memberInvokeWeiXin")
  public WeiXinEntity memberInvokeWeiXin();
}
