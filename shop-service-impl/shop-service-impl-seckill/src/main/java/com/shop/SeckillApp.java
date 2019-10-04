package com.shop;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description
 * @ClassName SeckillApp
 * @Author Administrator
 * @CreateTime 2019/9/26 20:15
 * Version 1.0
 **/
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableAsync
@EnableHystrix
//@EnableApolloConfig
@MapperScan("com.shop.seckill.mapper")
public class SeckillApp {
  public static void main(String[] args) {
	SpringApplication.run(SeckillApp.class, args);
  }
}
