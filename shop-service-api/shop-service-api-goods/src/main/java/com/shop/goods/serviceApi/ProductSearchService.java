package com.shop.goods.serviceApi;

import com.shop.base.BaseResponse;
import com.shop.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ProductSearchService {
@GetMapping("/search")
	public BaseResponse<List<ProductDto>> search(String name);
}
