package com.shop.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 用户返回参数dto
 * @ClassName UserOutputDTO
 * @Author Administrator
 * @CreateTime 2019/9/2 10:55
 * Version 1.0
 **/
@Data
@ApiModel(value = "用户返回参数dto")
public class UserOutputDTO {
  /**
   * userid
   */
  @ApiModelProperty(value = "用户id")
  private Long userid;
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
   * 注册时间
   */
  @ApiModelProperty(value = "创建时间")
  private Date createTime;
  /**
   * 修改时间
   *
   */
  @ApiModelProperty(value = "修改时间")
  private Date updateTime;
  /**
   * 账号是否可以用 1 正常 0冻结
   */
  @ApiModelProperty(value = "账号是否可以用 1 正常 0冻结")
  private char isAvailable;
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
