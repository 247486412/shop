package com.shop.service.impl;

import com.shop.entity.WeiXinEntity;
import com.shop.weixin.api.WeiXinApi;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeiXinImpl implements WeiXinApi {
  @Override
  public WeiXinEntity getWeiXin() {
    WeiXinEntity weiXinEntity = new WeiXinEntity();
    weiXinEntity.setId("6");
    weiXinEntity.setName("张三");
	return weiXinEntity;
  }
}
