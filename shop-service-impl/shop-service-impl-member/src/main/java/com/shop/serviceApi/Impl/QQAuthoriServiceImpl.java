package com.shop.serviceApi.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.core.token.GenerateToken;
import com.shop.member.serviceApi.QQAuthoriService;
import com.shop.serviceApi.mapper.PO.UserPO;
import com.shop.serviceApi.mapper.UserMapper;
import com.shop.web.constants.WebConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QQAuthoriServiceImpl extends BaseApiService<JSONObject> implements QQAuthoriService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private GenerateToken generateToken;

  @Override
  public BaseResponse<JSONObject> findByOpenId(String qqOpenId) {
	if (StringUtils.isBlank(qqOpenId)) {
	  return setResultError("qqOpenId不能为空！");
	}
	UserPO userPO = userMapper.findByOpenId(qqOpenId);
	if (userPO == null) {
	  return setResultError(WebConstants.HTTP_RES_CODE_NOTUSER_203, "根据openId没有查询到用户信息！");
	}
	//如果能够查询到用户信息就生成对应的用户令牌
	String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + "QQ_LOGIN";
	Long userId = userPO.getUserid();
	String userToken = generateToken.createToken(keyPrefix, userId + "");
	JSONObject data = new JSONObject();
	data.put("token", userToken);
	return setResultSuccess(data);

  }
}
