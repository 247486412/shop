package com.shop.portal.controller;

import com.shop.base.BaseResponse;
import com.shop.dto.output.UserOutputDTO;
import com.shop.feign.MemberServiceFeign;
import com.shop.web.base.BaseWebController;
import com.shop.web.utils.CookieUtils;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.JedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xxl.sso.core.conf.Conf;

/**
 * @Description 首页
 * @ClassName IndexController
 * @Author Administrator
 * @CreateTime 2019/9/3 21:16
 * Version 1.0
 **/
@Controller
public class IndexController extends BaseWebController {
  //首页地址
  private static final String INDEX_FTL = "index";
  @Autowired
  private MemberServiceFeign memberServiceFeign;

  //默认跳转首页
  @RequestMapping("/")
  public String index(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {

	//从cookie 中 获取sessionid
	String sessionId = CookieUtils.getCookieValue(request, Conf.SSO_SESSIONID, true);
	//获取从授权中心返回的sessionid
	String parameter = request.getParameter("xxl_sso_sessionid");
	String sessionID = sessionId == null ? parameter : sessionId;
	if (StringUtils.isBlank(sessionID)) {
	  session.removeAttribute("desensMobile");
	  return "redirect:/index";
	}
	String splitUserId = sessionID.split("_")[0];
	//前缀加userid拼接redis key
	XxlSsoUser xxlSsoUser = (XxlSsoUser) JedisUtil.getObjectValue(Conf.SSO_SESSIONID + "#" + splitUserId);
	if (xxlSsoUser == null) {
	  setErrorMsg(model, "sessionId");
	  return INDEX_FTL;
	}
	String userid = xxlSsoUser.getUserid();
	if (!StringUtils.isBlank(userid)) {
	  BaseResponse<UserOutputDTO> userInfo = memberServiceFeign.getInfo(userid);
	  if (isSuccess(userInfo)) {
		UserOutputDTO data = userInfo.getData();
		if (data != null) {
		  String mobile = data.getMobile();
		  // 对手机号码实现脱敏
		  String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		  session.setAttribute("desensMobile", desensMobile);
		}
	  }
	}
	return INDEX_FTL;
  }

  //跳转首页
  @RequestMapping("/index")
  public String toIndex() {
	return INDEX_FTL;
  }

}
