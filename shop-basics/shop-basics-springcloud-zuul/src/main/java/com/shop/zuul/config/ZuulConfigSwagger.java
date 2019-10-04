package com.shop.zuul.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Primary
@Configuration
public class ZuulConfigSwagger implements SwaggerResourcesProvider, ConfigChangeListener {
//  @ApolloConfig
//  private Config config;
  @Value("${shop.zuul.swagger.document}")
  private String document;

  //apollo配置中心发生变化时执行此监听方法,调用get方法遍历shop.zuul.swagger.document,获取最新文档配置
  @Override
  public void onChange(ConfigChangeEvent configChangeEvent) {
	get();
  }

  @Override
  public List<SwaggerResource> get() {
//	List resources = new ArrayList<>();
	// 添加文档来源
	// 网关使用服务别名获取远程服务的SwaggerApi
//	resources.add(swaggerResource("app-shop-member", "/app-shop-member/v2/serviceApi-docs", "2.0"));
//	resources.add(swaggerResource("app-shop-weixin", "/app-shop-weixin/v2/serviceApi-docs", "2.0"));
	// 从阿波罗平台获取指定key的配置文件,没有获取到该key对应的配置时,使用空字符串
//	String swaDocJson = config.getProperty("shop.zuul.swagger.document", "");

	JSONArray docJsonArray = JSONArray.parseArray(document);
	List resources = new ArrayList<>();
	// 遍历集合数组
	for (Object object : docJsonArray) {
	  JSONObject jsonObject = (JSONObject) object;
	  String name = jsonObject.getString("name");
	  String location = jsonObject.getString("location");
	  String version = jsonObject.getString("version");
	  resources.add(swaggerResource(name, location, version));
	}
	return resources;
  }

  private SwaggerResource swaggerResource(String name, String location, String version) {
	SwaggerResource swaggerResource = new SwaggerResource();
	swaggerResource.setName(name);
	swaggerResource.setLocation(location);
	swaggerResource.setSwaggerVersion(version);
	return swaggerResource;
  }


}
