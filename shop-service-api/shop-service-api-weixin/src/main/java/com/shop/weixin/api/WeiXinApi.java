package com.shop.weixin.api;

import com.shop.entity.WeiXinEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 微信接口
 */
@Api(tags = "微信服务接口")
public interface WeiXinApi {
  /**
   * 获取微信信息
   *
   * @return
   */
  @ApiOperation(value = "获取微信引用信息")
  @GetMapping("/getWeiXin")
  public WeiXinEntity getWeiXin();
}
