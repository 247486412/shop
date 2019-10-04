package com.shop.serviceApi.Impl;

import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.constants.Constants;
import com.shop.core.token.GenerateToken;
import com.shop.core.utils.TypeCastHelper;
import com.shop.dto.output.UserOutputDTO;
import com.shop.member.serviceApi.MemberService;
import com.shop.serviceApi.mapper.PO.UserPO;
import com.shop.serviceApi.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberServiceImpl extends BaseApiService<UserOutputDTO> implements MemberService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private GenerateToken generateToken;

  @Override
  public BaseResponse<UserOutputDTO> existMobile(String mobile) {
	if (StringUtils.isBlank(mobile)) {
	  return setResultError("手机号不能为空！");
	}
	UserPO userPO = userMapper.existMobile(mobile);
	if (userPO == null) {
	  return setResultError(Constants.HTTP_RES_CODE_203, "用户信息不存在！");
	}
	//把userPo转化为dto
	UserOutputDTO userOutputDTO = MyBeanUtils.copyProperties(userPO, UserOutputDTO.class);
	return setResultSuccess(userOutputDTO);
  }

  @Override
  public BaseResponse<UserOutputDTO> getInfo(String userid) {
	if (StringUtils.isBlank(userid)) {
	  return setResultError("userid不能为空！");
	}
	Long userId = TypeCastHelper.toLong(userid);
	UserPO userPO = userMapper.findByUserId(userId);
	if (userPO == null) {
	  return setResultError("用户不存在！");
	}
	UserOutputDTO userOutputDTO = MyBeanUtils.copyProperties(userPO, UserOutputDTO.class);
	return setResultSuccess(userOutputDTO);
  }
}
