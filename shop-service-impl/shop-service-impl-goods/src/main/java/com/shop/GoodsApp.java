package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @Description
 * @ClassName GoodsApp
 * @Author Administrator
 * @CreateTime 2019/9/10 21:13
 * Version 1.0
 **/
@SpringBootApplication
//@EnableEurekaClient
@EnableElasticsearchRepositories(basePackages = "com.shop.serviceApi")
public class GoodsApp {
  public static void main(String[] args) {
	SpringApplication.run(GoodsApp.class, args);
  }
}
