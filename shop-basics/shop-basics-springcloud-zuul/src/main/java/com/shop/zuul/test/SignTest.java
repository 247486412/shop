package com.shop.zuul.test;

import com.shop.core.utils.HttpClientUtils;
import com.shop.sign.SignUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @ClassName SignTest
 * @Author Administrator
 * @CreateTime 2019/9/22 17:22
 * Version 1.0
 **/
public class SignTest {
  public static void main(String[] args) {
	Map<String, String> sParaTemp = new HashMap<String, String>();
	sParaTemp.put("payAmount", "30000");
	sParaTemp.put("orderId", "2019010203501502");
	sParaTemp.put("userId", "30");
	String result = HttpClientUtils.doPost("http://127.0.0.1/app-pay/cratePayToken", SignUtil.sign(sParaTemp));
	System.out.println("result:" + result);
  }
}
