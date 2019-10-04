package com.xxl.sso.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import com.shop.constants.Constants;
import com.shop.dto.input.UserLoginInputDTO;
import com.shop.dto.output.UserOutputDTO;
import com.shop.web.base.BaseWebController;
import com.xxl.sso.core.conf.Conf;
import com.xxl.sso.core.login.SsoWebLoginHelper;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.server.feign.MemberLogiinServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * sso server (for web)
 *
 * @author xuxueli 2017-08-01 21:39:47
 */
@Controller
public class WebController extends BaseWebController {
  @Value("${xxl.sso.path.logout}")
  private String logoutPath;
  @Value("${xxl.sso.path.login}")
  private String loginPath;


  @Autowired
  private MemberLogiinServiceFeign memberLogiinServiceFeign;

  @RequestMapping("/")
  public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

	// login check
	XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);

	if (xxlUser == null) {
	  return "redirect:/login";
	} else {
	  model.addAttribute("xxlUser", xxlUser);
	  return "index";
	}
  }

  /**
   * Login page
   *
   * @param model
   * @param request
   * @return
   */
  @RequestMapping(Conf.SSO_LOGIN)
  public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

	// login check
	XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);

	if (xxlUser != null) {

	  // success redirect
	  String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
	  if (redirectUrl != null && redirectUrl.trim().length() > 0) {

		String sessionId = SsoWebLoginHelper.getSessionIdByCookie(request);
		String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
		;

		return "redirect:" + redirectUrlFinal;
	  } else {
		return "redirect:/";
	  }
	}

	model.addAttribute("errorMsg", request.getParameter("errorMsg"));
	model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
	return "login";
  }

  /**
   * Login
   *
   * @param request
   * @param redirectAttributes
   * @param password
   * @return
   */
  @RequestMapping("/doLogin")
  public String doLogin(HttpServletRequest request,
						HttpServletResponse response,
						RedirectAttributes redirectAttributes,
						String mobile,
						String password,
						String ifRemember) {

	boolean ifRem = (ifRemember != null && "on".equals(ifRemember)) ? true : false;
	UserLoginInputDTO userLoginInputDTO = new UserLoginInputDTO();
	userLoginInputDTO.setMobile(mobile);
	userLoginInputDTO.setPassword(password);
	userLoginInputDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
	//获取请求浏览器的信息
	String browserInfo = webBrowserInfo(request);
	userLoginInputDTO.setDeviceInfo(browserInfo);

	//调用会员的登录系统
	BaseResponse<JSONObject> login = memberLogiinServiceFeign.login(userLoginInputDTO);
	if (!isSuccess(login)) {
	  redirectAttributes.addAttribute("errorMsg", login.getMsg());
	  redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
	  return "redirect:/login";
	}
	JSONObject data = login.getData();
	String userJsonString = data.getString("userOutputDTO");
	UserOutputDTO userOutputDTO = JSONObject.parseObject(userJsonString, UserOutputDTO.class);
	// 1、make xxl-sso user
	XxlSsoUser xxlUser = new XxlSsoUser();
	xxlUser.setUserid(String.valueOf(userOutputDTO.getUserid()));
	xxlUser.setUsername(userOutputDTO.getUserName());
	xxlUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
	xxlUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
	xxlUser.setExpireFreshTime(System.currentTimeMillis());


	// 2、make session id
	String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);

	// 3、login, store storeKey + cookie sessionId
	SsoWebLoginHelper.login(response, sessionId, xxlUser, ifRem);

	// 4、return, redirect sessionId
//	String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
	String redirectUrl = loginPath;
	if (redirectUrl != null && redirectUrl.trim().length() > 0) {
	  String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
	  return "redirect:" + redirectUrlFinal;
	} else {
	  return "redirect:/";
	}

  }

  /**
   * Logout
   *
   * @param request
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(Conf.SSO_LOGOUT)
  public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

	// logout
	SsoWebLoginHelper.logout(request, response);

	redirectAttributes.addAttribute(Conf.REDIRECT_URL, logoutPath);
	return "redirect:/login";
  }


}
