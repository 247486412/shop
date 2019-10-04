package com.shop;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description
 * @ClassName PayApp
 * @Author Administrator
 * @CreateTime 2019/9/15 9:18
 * Version 1.0
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2Doc
@MapperScan("com.shop.pay.mapper")
public class PayApp {
  public static void main(String[] args) {
	SpringApplication.run(PayApp.class, args);
  }
}
