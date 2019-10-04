package com.shop.init;

import com.unionpay.acp.sdk.SDKConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @ClassName InitUnionPay
 * @Author Administrator
 * @CreateTime 2019/9/14 19:39
 * Version 1.0
 **/
@Component
public class InitUnionPay implements ApplicationRunner {
  // springboot 项目启动的时候 执行该方法加载所有需要的证书和配置
  @Override
  public void run(ApplicationArguments args) throws Exception {
	SDKConfig.getConfig().loadPropertiesFromSrc();
  }
}
