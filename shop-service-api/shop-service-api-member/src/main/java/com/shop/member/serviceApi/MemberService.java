package com.shop.member.serviceApi;

import com.shop.base.BaseResponse;
import com.shop.dto.output.UserOutputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "会员服务接口")
public interface MemberService {
  @ApiOperation(value = "判断该手机号是否存在")
  @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", paramType = "query", dataType = "String", required = true)})
  //需要feign客户端调用的接口有参数的话底层默认发送post请求,所以需要PostMapping,参数必须有@RequestParam注解
  @PostMapping("/existMobile")
  public BaseResponse<UserOutputDTO> existMobile(@RequestParam("mobile") String mobile);

  @ApiOperation(value = "根据token对应的userid获取用户信息")
  @PostMapping("/getInfo")
  public BaseResponse<UserOutputDTO> getInfo(@RequestParam("userid") String userid);

}
