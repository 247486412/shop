package com.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.core.token.GenerateToken;
import com.shop.core.utils.Guid;
import com.shop.service.mapper.AppInfoMapper;
import com.shop.service.mapper.entity.AppInfo;
import com.shop.serviceApi.authorization.AuthorizationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description auto2.0授权接口实现
 * @ClassName AuthorizationServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/23 13:56
 * Version 1.0
 **/
@Service
public class AuthorizationServiceImpl extends BaseApiService<JSONObject> implements AuthorizationService {
  @Autowired
  private AppInfoMapper appInfoMapper;
  @Autowired
  private GenerateToken generateToken;

  @Override
  //appName一般为商户或机构名称,实际申请时需要审核,审核成功把是否可用字段改为1表示可用
  public BaseResponse<JSONObject> applyAppInfo(String appName) {
	if (StringUtils.isBlank(appName)) {
	  return setResultError("机构名称不能为空！");
	}
	Guid guid = new Guid();
	String appId = guid.getAppId();
	String appSecret = guid.getAppSecret();
	AppInfo appInfo = new AppInfo();
	appInfo.setAppName(appName);
	appInfo.setAppId(appId);
	appInfo.setAppSecret(appSecret);
	//把申请的信息和生成的信息插入数据库
	int insertAppInfo = appInfoMapper.insertAppInfo(appInfo);
	if (!daoResult(insertAppInfo)) {
	  return setResultError("申请失败！");
	}
	JSONObject data = new JSONObject();
	data.put("appId", appId);
	data.put("appSecret", appSecret);
	//把信息返回给客户端
	return setResultSuccess(data);
  }


  @Override
  public BaseResponse<JSONObject> getAccessToken(String appId, String appSecret) {
	if (StringUtils.isBlank(appId)) {
	  return setResultError("appId不能为空！");
	}
	if (StringUtils.isBlank(appSecret)) {
	  return setResultError("appSecret不能为空！");
	}
	AppInfo appInfo = appInfoMapper.selectByAppInfo(appId, appSecret);
	if (appInfo == null) {
	  setResultError("appId或appSecret有误!");
	}
	String id = appInfo.getAppId();
	//生成访问令牌
	String token = generateToken.createToken("auto", id, 60 * 60 * 24 * 7L);
	JSONObject data = new JSONObject();
	data.put("token", token);
	return setResultSuccess(data);
  }

  @Override
  public BaseResponse<JSONObject> getAppInfo(String accessToken) {
	if (StringUtils.isBlank(accessToken)) {
	  return setResultError("AccessToken cannot be empty");
	}
	String appId = generateToken.getToken(accessToken);
	if (StringUtils.isBlank(appId)) {
	  return setResultError("accessToken is Error");
	}
	AppInfo appInfo = appInfoMapper.findByAppId(appId);
	if (appInfo == null) {
	  return setResultError("AccessToken  invalid");
	}
	JSONObject data = new JSONObject();
	data.put("appInfo", appInfo);
	return setResultSuccess(data);
  }
}
