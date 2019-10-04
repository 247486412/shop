package com.shop.dto.output;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @ClassName WeiXinOutputDTO
 * @Author Administrator
 * @CreateTime 2019/9/2 11:24
 * Version 1.0
 **/
@ApiModel(value = "微信")
@AllArgsConstructor
@NoArgsConstructor
public class WeiXinOutputDTO {
  /**
   * appid
   */
  private String appId;
  /**
   * 应用名称
   */
  private String appName;

}
