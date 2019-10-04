package com.shop.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 用户输入参数dto
 * @ClassName UserInputDTO
 * @Author Administrator
 * @CreateTime 2019/9/2 10:54
 * Version 1.0
 **/
@Data
@ApiModel(value = "用户输入参数dto")
public class UserInputDTO {
  /**
   * userid
   */
  @ApiModelProperty(value = "用户id")
  private Long userId;
  /**
   * 手机号码
   */
  @ApiModelProperty(value = "手机号码")
  private String mobile;
  /**
   * 邮箱
   */
  @ApiModelProperty(value = "邮箱")
  private String email;
  /**
   * 密码
   */
  @ApiModelProperty(value = "密码")
  private String password;
  /**
   * 用户名称
   */
  @ApiModelProperty(value = "用户名称")
  private String userName;
  /**
   * 性别 0 男 1女
   */
  @ApiModelProperty(value = "用户性别")
  private char sex;
  /**
   * 年龄
   */
  @ApiModelProperty(value = "用户年龄")
  private Long age;
  /**
   * 用户头像
   */
  @ApiModelProperty(value = " 用户头像")
  private String picImg;
  /**
   * 用户关联 QQ 开放ID
   */
  @ApiModelProperty(value = "用户关联 QQ 开放ID")
  private String qqOpenid;
  /**
   * 用户关联 微信 开放ID
   */
  @ApiModelProperty(value = "用户关联 微信 开放ID")
  private String wXOpenId;

}
