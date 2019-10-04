package com.shop.base;

import com.shop.bean.MyBeanUtils;
import com.shop.constants.Constants;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 基本响应消息
 *
 * @param <T>
 */
@Data
@Component
public class BaseApiService<T> {

  public BaseResponse<T> setResultError(Integer code, String msg) {
	return setResult(code, msg, null);
  }

  // 返回错误，可以传msg
  public BaseResponse<T> setResultError(String msg) {
	return setResult(Constants.HTTP_RES_CODE_500, msg, null);
  }

  // 返回成功，可以传data值
  public BaseResponse<T> setResultSuccess(T data) {
	return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
  }

  // 返回成功，沒有data值
  public BaseResponse<T> setResultSuccess() {
	return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
  }

  // 返回成功，沒有data值
  public BaseResponse<T> setResultSuccess(String msg) {
	return setResult(Constants.HTTP_RES_CODE_200, msg, null);
  }

  // 通用封装
  public BaseResponse<T> setResult(Integer code, String msg, T data) {
	return new BaseResponse<T>(code, msg, data);
  }

  //判断数据库返回的结果
  public Boolean daoResult(int result) {
	return result > 0 ? true : false;
  }
  // 接口直接返回true 或者false
  public Boolean isSuccess(BaseResponse<?> baseResp) {
	if (baseResp == null) {
	  return false;
	}
	if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
	  return false;
	}
	return true;
  }

}
