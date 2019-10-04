package com.shop.serviceApi.mapper;

import com.shop.serviceApi.mapper.PO.UserTokenPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


/**
 *
 * @description: 用户TokenMapper
 */
@Repository
public interface UserTokenMapper {

	/**
	 * 根据userid+loginType +is_availability=0 进行查询
	 * 
	 * @param userId
	 * @param loginType
	 * @return
	 */
	@Select("SELECT id as id ,token as token ,login_type as LoginType, device_infor as deviceInfor ,is_availability as isAvailability,user_id as userId"
			+ "" + ""
			+ " , create_time as createTime,update_time as updateTime   FROM shop_user_token WHERE user_id=#{userId} AND login_type=#{loginType} and is_availability ='0'; ")
	UserTokenPo selectByUserIdAndLoginType(@Param("userId") Long userId, @Param("loginType") String loginType);

  /**
   *
   * @param token
   * @return
   */
	@Update(" update shop_user_token set is_availability  ='1', update_time=now() where token=#{token}")
	int updateTokenNotAvailability(@Param("token") String token);

	/**
	 * token记录表中插入一条记录
	 * 
	 * @param userTokenPo
	 * @return
	 */
	@Insert("INSERT INTO `shop_user_token` VALUES (null, #{token},#{loginType}, #{deviceInfor}, 0, #{userId} ,now(),null ); ")
	int insertUserToken(UserTokenPo userTokenPo);
}
