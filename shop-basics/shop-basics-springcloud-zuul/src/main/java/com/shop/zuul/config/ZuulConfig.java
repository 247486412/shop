package com.shop.zuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Primary
@Configuration
public class ZuulConfig implements SwaggerResourcesProvider {
  @Override
  public List<SwaggerResource> get() {
	List resources = new ArrayList<>();
	// 添加文档来源
	// 网关使用服务别名获取远程服务的SwaggerApi
	resources.add(swaggerResource("app-shop-member", "/app-shop-member/v2/api-docs", "2.0"));
	resources.add(swaggerResource("app-shop-weixin", "/app-shop-weixin/v2/api-docs", "2.0"));
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
