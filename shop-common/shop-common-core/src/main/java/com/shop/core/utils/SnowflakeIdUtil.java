package com.shop.core.utils;

/**
 * @Description
 * @ClassName SnowflakeIdUtil
 * @Author Administrator
 * @CreateTime 2019/9/15 10:00
 * Version 1.0
 **/
public class SnowflakeIdUtil {
  private static SnowflakeIdWorker idWorker;

  static {
	//使用静态代码块只加载一次
	idWorker = new SnowflakeIdWorker(1, 1);
  }

  public static String getId() {
	return idWorker.nextId() + "";
  }
}
