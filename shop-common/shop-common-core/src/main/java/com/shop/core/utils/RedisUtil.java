package com.shop.core.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis存取删除字符串类型数据
 */
@Component
public class RedisUtil {
  @Autowired
  private StringRedisTemplate stringRedisTemplate;
  @Autowired
  private RedisTemplate redisTemplate;


  public StringRedisTemplate getStringRedisTemplate() {
	return stringRedisTemplate;
  }

  public RedisTemplate getRedisTemplate() {
	return redisTemplate;
  }

  /**
   * 存放string类型
   *
   * @param key     key
   * @param data    数据
   * @param timeout 超时间
   */
  public void setString(String key, String data, Long timeout) {
	stringRedisTemplate.opsForValue().set(key, data);
	if (timeout != null) {
	  setTimeout(key, timeout);
	}
  }

  /**
   * 存放string类型
   *
   * @param key  key
   * @param data 数据
   */
  public void setString(String key, String data) {
	setString(key, data, null);
  }

  public void setTimeout(String key, Long timeout) {
	stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
  }


  /**
   * 存放string类型
   *
   * @param key
   * @param value
   * @param timeout
   * @return
   */

  public Boolean setNx(String key, String value, Long timeout) {
	//插入的值如果存在返回false,不存在返回true
	Boolean ifAbsent = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	if (timeout != null) {
	  setTimeout(key, timeout);
	}
	return ifAbsent;
  }

  public void setList(String seckillKey, List<String> tokenList) {
	stringRedisTemplate.opsForList().leftPushAll(seckillKey, tokenList);
  }

  public String getStringFromList(String key) {
    //leftPop为移除左边第一个元素并返回该元素
	return stringRedisTemplate.opsForList().leftPop(key);
  }

  /**
   * 根据key查询string类型
   *
   * @param key
   * @return
   */
  public String getString(String key) {
	String value = stringRedisTemplate.opsForValue().get(key);
	return value;
  }

  /**
   * 根据key获取对象
   */
  public Object getObj(String key) {
	return redisTemplate.boundValueOps(key).get();
  }


  /**
   * 根据对应的key删除key
   *
   * @param key
   */
  public boolean delete(String key) {
	return stringRedisTemplate.delete(key);
  }

  /**
   * 开启Redis 事务
   */
  public void begin() {
	// 开启Redis 事务权限
	stringRedisTemplate.setEnableTransactionSupport(true);
	// 开启事务
	stringRedisTemplate.multi();
  }

  /**
   * 提交事务
   */
  public void exec() {
	// 成功提交事务
	stringRedisTemplate.exec();
  }

  /**
   * 回滚Redis 事务
   */
  public void discard() {
	stringRedisTemplate.discard();
  }


}
