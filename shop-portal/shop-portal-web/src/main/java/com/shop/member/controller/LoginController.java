package com.shop.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.constants.Constants;
import com.shop.dto.input.UserLoginInputDTO;
import com.shop.feign.MemberLoginServiceFeign;
import com.shop.web.constants.WebConstants;
import com.shop.member.controller.req.vo.LoginVo;
import com.shop.web.base.BaseWebController;
import com.shop.web.utils.CookieUtils;
import com.shop.web.utils.RandomValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description 登录
 * @ClassName LoginController
 * @Author Administrator
 * @CreateTime 2019/9/3 21:22
 * Version 1.0
 **/
@Controller
public class LoginController extends BaseWebController {
  private static final String MB_LOGIN_FTL = "member/login";
  @Autowired
  private MemberLoginServiceFeign memberLoginServiceFeign;

  //接收get请求用于跳转页面
  @GetMapping("/login")
  public String getLogin() {
	return MB_LOGIN_FTL;
  }

  //接收post请求,用于接收登录参数
  @PostMapping("/login")
  public String postLogin(@ModelAttribute("loginVo") @Validated LoginVo loginVo, HttpServletRequest request,
						  Model model, HttpSession httpSession, HttpServletResponse response) {
	//判断图形验证码
	String graphicCode = loginVo.getGraphicCode();
	Boolean checkVerify = RandomValidateCodeUtil.checkVerify(graphicCode, httpSession);
	if (!checkVerify) {
	  setErrorMsg(model, "图形验证码不正确！");
	  return MB_LOGIN_FTL;
	}
	//把vo转为dto调用会员接口
	UserLoginInputDTO userLoginInputDTO = MyBeanUtils.copyProperties(loginVo, UserLoginInputDTO.class);
	userLoginInputDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
	//获取请求浏览器的信息
	String browserInfo = webBrowserInfo(request);
	userLoginInputDTO.setDeviceInfo(browserInfo);
	BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInputDTO);
	if (!isSuccess(login)) {
	  setErrorMsg(model, login.getMsg());
	  return MB_LOGIN_FTL;
	}
	//登录成功后把token存入到cookie里,返回用户信息到页面
	JSONObject data = login.getData();
	String token = data.getString("token");
	CookieUtils.setCookie(request, response, WebConstants.LOGIN_TOKEN_COOKIENAME, token);
	return "/";
  }
}
