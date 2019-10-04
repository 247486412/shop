package com.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableSwagger2Doc
@EnableEurekaClient
@EnableZuulProxy
@MapperScan("com.shop.zuul.mapper")
@EnableFeignClients
//@EnableApolloConfig
public class ZuulApp {
  public static void main(String[] args) {
	SpringApplication.run(ZuulApp.class, args);
  }
}
