package com.shop.serviceApi.reposiory;

import com.shop.serviceApi.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ProductReposiory extends ElasticsearchRepository<ProductEntity, Long> {

}
