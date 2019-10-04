package com.shop.serviceApi.mapper.PO;


import com.shop.base.BasePo;
import lombok.Data;

@Data
public class UserTokenPo extends BasePo {
  /**
   * 用户token
   */
  private String token;
  /**
   * 登陆类型
   */
  private String loginType;

  /**
   * 设备信息
   */
  private String deviceInfor;
  /**
   * 用户userId
   */
  private Long userId;

}
