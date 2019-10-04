package com.shop.serviceApi.Impl;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.constants.Constants;
import com.shop.core.token.GenerateToken;
import com.shop.core.transaction.RedisAndDBTransaction;
import com.shop.core.utils.MD5Util;
import com.shop.dto.input.UserLoginInputDTO;
import com.shop.dto.output.UserOutputDTO;
import com.shop.member.serviceApi.MemberLoginService;
import com.shop.serviceApi.mapper.PO.UserPO;
import com.shop.serviceApi.mapper.PO.UserTokenPo;
import com.shop.serviceApi.mapper.UserMapper;
import com.shop.serviceApi.mapper.UserTokenMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 用户登录接口实现
 * @ClassName MemberLoginServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/3 8:48
 * Version 1.0
 **/
@RestController
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements MemberLoginService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserTokenMapper userTokenMapper;
  @Autowired
  private GenerateToken generateToken;
  @Autowired
  private RedisAndDBTransaction redisAndDBTransaction;

  @Override
  public BaseResponse<JSONObject> login(@RequestBody UserLoginInputDTO userLoginInputDTO) {
	String mobile = userLoginInputDTO.getMobile();
	if (StringUtils.isBlank(mobile)) {
	  return setResultError("手机号不能为空！");
	}
	String password = userLoginInputDTO.getPassword();
	if (StringUtils.isBlank(password)) {
	  return setResultError("密码不能为空！");
	}
	String loginType = userLoginInputDTO.getLoginType();
	if (StringUtils.isBlank(loginType)) {
	  return setResultError("登录类型不能为空！");
	}
	if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
			|| loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
	  return setResultError("登录类型错误！");
	}
	// 设备信息
	String deviceInfo = userLoginInputDTO.getDeviceInfo();
	if (StringUtils.isEmpty(deviceInfo)) {
	  return setResultError("设备信息不能为空!");
	}
	String md5Password = MD5Util.MD5(password);
	UserPO userPO = userMapper.login(mobile, md5Password);
	if (userPO == null) {
	  return setResultError("手机号或密码错误！");
	}
	Long userid = userPO.getUserid();
	//如果根据userid和登录设备类型可以查询到说明登录过
	UserTokenPo userTokenPo = userTokenMapper.selectByUserIdAndLoginType(userid, loginType);

	try {
	  //开启数据库和redis事务
	  redisAndDBTransaction.begin();
	  if (userTokenPo != null) {
		String token = userTokenPo.getToken();
		//redis事务开启时删除都是返回false,因为事务没有提交
		generateToken.removeToken(token);
		//登录过的话更改上次登录的token状态为1,表示该token不可用
		int tokenNotAvailability = userTokenMapper.updateTokenNotAvailability(token);
		if (!daoResult(tokenNotAvailability)) {
		  redisAndDBTransaction.rollback();
		  return setResultError("系统错误！");
		}
	  }
	  //创建新的userToken对象并赋值
	  UserTokenPo newUserTokenPo = new UserTokenPo();
	  //生成对应的新的token放在redis中
	  String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + loginType;
	  String newToken = generateToken.createToken(keyPrefix, userid + "");
	  newUserTokenPo.setToken(newToken);
	  newUserTokenPo.setLoginType(loginType);
	  newUserTokenPo.setDeviceInfor(deviceInfo);
	  newUserTokenPo.setUserId(userid);
	  //把新的userToken对象存入数据库
	  int insertUserToken = userTokenMapper.insertUserToken(newUserTokenPo);
	  if (!daoResult(insertUserToken)) {
		redisAndDBTransaction.rollback();
		return setResultError("系统错误！");
	  }
	  JSONObject data = new JSONObject();
	  UserOutputDTO userOutputDTO = MyBeanUtils.copyProperties(userPO, UserOutputDTO.class);
	  String jsonString = JSONObject.toJSONString(userOutputDTO);
	  data.put("userOutputDTO", jsonString);
	  data.put("token", newToken);
	  redisAndDBTransaction.commit();
	  return setResultSuccess(data);
	} catch (Exception e) {
	  redisAndDBTransaction.rollback();
	}
	return setResultError("系统错误！");
  }
}
