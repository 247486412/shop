package com.shop.serviceApi.impl;

import java.util.List;

import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.dto.ProductDto;
import com.shop.goods.serviceApi.ProductSearchService;
import com.shop.serviceApi.entity.ProductEntity;
import com.shop.serviceApi.reposiory.ProductReposiory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@RestController
public class ProductSearchServiceImpl extends BaseApiService<List<ProductDto>> implements ProductSearchService {
  @Autowired
  private ProductReposiory productReposiory;

  @Override
  public BaseResponse<List<ProductDto>> search(String name) {
	// 1.拼接查询条件
	BoolQueryBuilder builder = QueryBuilders.boolQuery();
	// 2.模糊查询name字段
	builder.must(QueryBuilders.fuzzyQuery("name", name));
	Pageable pageable = new QPageRequest(0, 5);
	// 3.调用ES接口查询
	Page<ProductEntity> page = productReposiory.search(builder, pageable);
	// 4.获取集合数据
	List<ProductEntity> content = page.getContent();
	// 5.将entity类型集合转换dto类型集合
	MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	List<ProductDto> mapAsList = mapperFactory.getMapperFacade().mapAsList(content, ProductDto.class);
	return setResultSuccess(mapAsList);
  }
}
