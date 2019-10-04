package com.shop.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.constants.Constants;
import com.shop.dto.input.UserLoginInputDTO;
import com.shop.feign.MemberLoginServiceFeign;
import com.shop.feign.QQAuthoriFeign;
import com.shop.member.controller.req.vo.LoginVo;
import com.shop.web.base.BaseWebController;
import com.shop.web.constants.WebConstants;
import com.shop.web.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description 使用qq授权登录
 * @ClassName QQAuthoriController
 * @Author Administrator
 * @CreateTime 2019/9/4 19:02
 * Version 1.0
 **/
@Controller
@Slf4j
public class QQAuthoriController extends BaseWebController {
  private static final String MB_QQ_QQLOGIN = "member/qqlogin";
  @Autowired
  private MemberLoginServiceFeign memberLoginServiceFeign;
  @Autowired
  private QQAuthoriFeign qqAuthoriFeign;
  /**
   * 重定向到首页
   */
  private static final String REDIRECT_INDEX = "redirect:/";

  /**
   * 生成授权链接
   */
  @RequestMapping("/qqAuth")
  public String qqAuth(HttpServletRequest request) {
	try {
	  String authorizeURL = new Oauth().getAuthorizeURL(request);
	  log.info("authorizeURL:{}", authorizeURL);
	  return "redirect:" + authorizeURL;
	} catch (Exception e) {
	  return ERROR_500_FTL;
	}
  }

  @RequestMapping("/qqLoginBack")
  public String qqLoginBack(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	try {
	  // 使用授权码获取accessToken
	  AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
	  if (accessTokenObj == null) {
		return ERROR_500_FTL;
	  }
	  String accessToken = accessTokenObj.getAccessToken();
	  if (StringUtils.isBlank(accessToken)) {
		return ERROR_500_FTL;
	  }
	  OpenID openIDObj = new OpenID(accessToken);
	  String openID = openIDObj.getUserOpenID();
	  if (StringUtils.isBlank(openID)) {
		return ERROR_500_FTL;
	  }
	  //使用openId查询数据库是否有关联的账号
	  BaseResponse<JSONObject> byOpenId = qqAuthoriFeign.findByOpenId(openID);
	  if (!isSuccess(byOpenId)) {
		return ERROR_500_FTL;
	  }
	  //如果返回203说明没有关联账号
	  if (byOpenId.getCode().equals(WebConstants.HTTP_RES_CODE_NOTUSER_203)) {
		//页面展示qq头像
		UserInfo userInfo = new UserInfo(accessToken, openID);
		UserInfoBean info = userInfo.getUserInfo();
		if (info == null) {
		  return ERROR_500_FTL;
		}
		//获取用户头像,100像素
		String avatarURL100 = info.getAvatar().getAvatarURL100();
		request.setAttribute("avatarURL100", avatarURL100);
		//把openid存入session
		session.setAttribute(WebConstants.LOGIN_QQ_OPENID, openID);
		//跳转到绑定qq openid页面进行绑定
		return MB_QQ_QQLOGIN;
	  }
	  //如果可以查询到用户信息则自动登录
	  String token = byOpenId.getData().getString("token");
	  CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
	  return REDIRECT_INDEX;
	} catch (QQConnectException e) {
	  return ERROR_500_FTL;
	}
  }

  @RequestMapping("/qqJointLogin")
  public String qqJointLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model, HttpServletRequest request,
							 HttpServletResponse response, HttpSession httpSession) {

	// 1.获取用户openid
	String qqOpenId = (String) httpSession.getAttribute(WebConstants.LOGIN_QQ_OPENID);
	if (StringUtils.isEmpty(qqOpenId)) {
	  return ERROR_500_FTL;
	}

	// 2.将vo转换dto调用会员登陆接口
	UserLoginInputDTO userLoginInpDTO = MyBeanUtils.copyProperties(loginVo, UserLoginInputDTO.class);
	userLoginInpDTO.setQqOpenId(qqOpenId);
	userLoginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
	String info = webBrowserInfo(request);
	userLoginInpDTO.setDeviceInfo(info);
	BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
	if (!isSuccess(login)) {
	  setErrorMsg(model, login.getMsg());
	  return MB_QQ_QQLOGIN;
	}
	// 3.登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
	JSONObject data = login.getData();
	String token = data.getString("token");
	CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
	return REDIRECT_INDEX;
  }

}
