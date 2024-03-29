package com.shop.serviceApi.authorization;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * auto2.0授权接口
 */
public interface AuthorizationService {
	/**
	 * 机构申请 获取appid 和appsecret
	 */
	@GetMapping("/applyAppInfo")
	public BaseResponse<JSONObject> applyAppInfo(@RequestParam("appName") String appName);

	/**
	 * 使用appid 和appsecret密钥获取AccessToken
	 */
	@GetMapping("/getAccessToken")
	public BaseResponse<JSONObject> getAccessToken(@RequestParam("appId") String appId,
			@RequestParam("appSecret") String appSecret);

	/**
	 * 验证Token是否失效
	 */
	@GetMapping("/getAppInfo")
	public BaseResponse<JSONObject> getAppInfo(@RequestParam("accessToken") String accessToken);

}
