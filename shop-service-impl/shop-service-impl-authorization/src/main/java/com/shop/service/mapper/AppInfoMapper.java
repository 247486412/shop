package com.shop.service.mapper;

import com.shop.service.mapper.entity.AppInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AppInfoMapper {

	@Insert("INSERT INTO `app_info` VALUES (null,#{appName}, #{appId}, #{appSecret}, '0', null, null, null, null, null);")
	public int insertAppInfo(AppInfo appInfo);

	@Select("SELECT ID AS ID ,app_id as appId, app_name AS appName ,app_secret as appSecret  FROM app_info where app_id=#{appId} and app_secret=#{appSecret}; ")
	public AppInfo selectByAppInfo(@Param("appId") String appId, @Param("appSecret") String appSecret);

	@Select("SELECT ID AS ID ,app_id as appId, app_name AS appName ,app_secret as appSecret  FROM app_info where app_id=#{appId}  ")
	public AppInfo findByAppId(@Param("appId") String appId);
}
