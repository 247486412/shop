package com.shop.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import com.shop.bean.MyBeanUtils;
import com.shop.dto.input.UserInputDTO;
import com.shop.feign.MemberRegisterServiceFeign;
import com.shop.member.controller.req.vo.RegisterVo;
import com.shop.web.base.BaseWebController;
import com.shop.web.utils.RandomValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @Description 注册
 * @ClassName RegisterController
 * @Author Administrator
 * @CreateTime 2019/9/3 21:28
 * Version 1.0
 **/
@Controller
public class RegisterController extends BaseWebController {
  private static final String MB_REGISTER_FTL = "member/register";
  private static final String MB_LOGIN_FTL = "member/login";
  @Autowired
  private MemberRegisterServiceFeign memberRegisterServiceFeign;

  @GetMapping("/register")
  public String getRegister() {
	return MB_REGISTER_FTL;
  }

  @PostMapping("/register")
  //@Validated在实体类里通用验证,BindingResult用于获取验证错误消息,参数位置需在对应实体类后面
  public String postRegister(@ModelAttribute("registerVo") @Validated RegisterVo registerVo, BindingResult bindingResult,
							 Model model, HttpSession httpSession) {
	if (bindingResult.hasErrors()) {
	  //获取错误，只要有一个错误就返回
	  String errorMsg = bindingResult.getFieldError().getDefaultMessage();
	  setErrorMsg(model, errorMsg);
	  return MB_REGISTER_FTL;
	}
	//判断图形验证码
	String graphicCode = registerVo.getGraphicCode();
	Boolean checkVerify = RandomValidateCodeUtil.checkVerify(graphicCode, httpSession);
	if (!checkVerify) {
	  setErrorMsg(model, "图形验证码不正确！");
	  return MB_REGISTER_FTL;
	}


	//把vo转换为dto
	UserInputDTO userInputDTO = MyBeanUtils.copyProperties(registerVo, UserInputDTO.class);
	//调用会员的注册功能
	BaseResponse<JSONObject> register = memberRegisterServiceFeign.register(userInputDTO, registerVo.getRegistCode());
	//父类判断返回是否为200
	if (!isSuccess(register)) {
	  setErrorMsg(model, register.getMsg());
	  return MB_REGISTER_FTL;
	}
//注册成功转发到登录
	return MB_LOGIN_FTL;
  }
}
