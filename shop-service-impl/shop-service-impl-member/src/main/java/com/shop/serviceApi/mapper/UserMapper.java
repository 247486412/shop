package com.shop.serviceApi.mapper;

import com.shop.serviceApi.mapper.PO.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @ClassName UserMapper
 * @Author Administrator
 * @CreateTime 2019/9/1 17:58
 * Version 1.0
 **/
@Repository
public interface UserMapper {
  //用户注册
  @Insert("INSERT INTO `shop_user` VALUES (null,#{mobile}, null, #{password}, #{userName}, null, null, null, '1', null, null, null);")
  int register(UserPO userPO);

  //用户手机号是否存在
  @Select("SELECT * FROM shop_user WHERE MOBILE=#{mobile};")
  UserPO existMobile(@Param("mobile") String mobile);

  //用户登录，判断该手机号和密码是否存在
  @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID "
		  + "  FROM shop_user  WHERE MOBILE=#{mobile} and password=#{password};")
  UserPO login(@Param("mobile") String mobile, @Param("password") String password);

  //根据id查询用户信息
  @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID"
		  + " FROM shop_user WHERE user_Id=#{userId}")
  UserPO findByUserId(@Param("userId") Long userId);

  @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID"
          + " FROM shop_user WHERE qq_openid=#{qqOpenId}")
  UserPO findByOpenId(@Param("qqOpenId") String qqOpenId);

  @Update("update shop_user set QQ_OPENID =#{qqOpenId} WHERE USER_ID=#{userId}")
  int updateUserOpenId(@Param("qqOpenId") String qqOpenId, @Param("userId") Long userId);

}
