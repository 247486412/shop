package com.shop.core.token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.shop.core.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GenerateToken {
  @Autowired
  private RedisUtil redisUtil;

  /**
   * 生成令牌
   *
   * @param redisValue redis存放的值
   * @return 返回token
   */
  public String createToken(String keyPrefix, String redisValue) {
	return createToken(keyPrefix, redisValue, null);
  }

  /**
   * 生成令牌
   *
   * @param redisValue redis存放的值
   * @param time       有效期
   * @return 返回token
   */
  public String createToken(String keyPrefix, String redisValue, Long time) {
	if (StringUtils.isEmpty(redisValue)) {
	  new Exception("redis value is null");
	}
	String token = keyPrefix + UUID.randomUUID().toString().replace("-", "");
	redisUtil.setString(token, redisValue, time);
	return token;
  }


  public void createTokenList(String seckillTokenPrefix, String seckillKey, Long tokenQuantity) {
	if (StringUtils.isBlank(seckillTokenPrefix) || StringUtils.isBlank(seckillKey) || tokenQuantity == null) {
	  new RuntimeException("redis value is null");
	}
	List<String> tokenList = makeTokenList(seckillTokenPrefix, tokenQuantity);
	//把该token集合存入redis中,key为商品id
	redisUtil.setList(seckillKey, tokenList);
  }

  //生成多个token存入集合中
  private List<String> makeTokenList(String seckillTokenPrefix, Long tokenQuantity) {
	List<String> tokenList = new ArrayList<String>();
	for (int i = 0; i < tokenQuantity; i++) {
	  String token = seckillTokenPrefix + UUID.randomUUID().toString().replace("-", "");
	  tokenList.add(token);
	}
	return tokenList;
  }

  public String getTokenFromList(String seckillKey) {
	return redisUtil.getStringFromList(seckillKey);
  }

  /**
   * 根据token获取redis中的value值
   *
   * @param token
   * @return
   */
  public String getToken(String token) {
	if (StringUtils.isEmpty(token)) {
	  return null;
	}
	String value = redisUtil.getString(token);
	return value;
  }

  /**
   * 根据key获取对象
   */
  public Object getObj(String key) {
	return redisUtil.getObj(key);
  }

  /**
   * 移除token
   *
   * @param token
   * @return
   */
  public Boolean removeToken(String token) {
	if (StringUtils.isEmpty(token)) {
	  return null;
	}
	return redisUtil.delete(token);

  }


}
