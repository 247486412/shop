package com.shop.error;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseApiService<JSONObject> {
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public BaseResponse<JSONObject> exceptionHandler(Exception e) {
		log.info("全局捕获异常,系统错误!", e);
		return setResultError("系统错误!");
	}
}
