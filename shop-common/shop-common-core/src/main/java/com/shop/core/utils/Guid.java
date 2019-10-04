package com.shop.core.utils;


import java.util.UUID;

public class Guid {
  public String appKey;

  /**
   * 随机获取key值
   */
  public String guid() {
	UUID uuid = UUID.randomUUID();
	String key = uuid.toString().replace("-", "");
	return key;
  }


  /**
   * 这是其中一个url的参数，是GUID的，全球唯一标志符
   */
  public String getAppId() {
	Guid g = new Guid();
	String guid = g.guid();
	appKey = guid;
	return appKey;
  }

  /**
   * 根据md5加密 appid+key 实现MD5
   */
  public String getAppSecret() {
	String mw = "key" + appKey;
	// 得到以后还要用MD5加密。
	String appSign = MD5Util.MD5(mw).toUpperCase();
	return appSign;
  }

}
