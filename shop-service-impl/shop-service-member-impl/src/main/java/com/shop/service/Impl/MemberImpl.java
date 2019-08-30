package com.shop.service.Impl;

import com.shop.entity.WeiXinEntity;
import com.shop.member.api.MemberApi;
import com.shop.service.feign.WeiXinApiFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberImpl implements MemberApi {
  @Autowired
  private WeiXinApiFeign weiXinApiFeign;

  @Override
  public WeiXinEntity memberInvokeWeiXin() {
	return weiXinApiFeign.getWeiXin();
  }
}
