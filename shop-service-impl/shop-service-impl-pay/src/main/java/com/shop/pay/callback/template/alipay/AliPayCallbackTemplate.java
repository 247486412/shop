package com.shop.pay.callback.template.alipay;

import com.shop.pay.callback.template.AbstractPayCallbackTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description
 * @ClassName AliPayCallbackTemplate
 * @Author Administrator
 * @CreateTime 2019/9/19 14:09
 * Version 1.0
 **/
public class AliPayCallbackTemplate extends AbstractPayCallbackTemplate {

  @Override
  public Map<String, String> verifySignature(HttpServletRequest request, HttpServletResponse response) {
	return null;
  }
  @Override
  public String asyncCallbackService(Map<String, String> verifySignature) {
	return null;
  }

  @Override
  public String verifyFail() {
	return "支付宝支付异步回调执行失败！";
  }


}
