package com.shop.seckill.impl;

import com.shop.base.BaseApiService;
import com.shop.base.BaseResponse;
import com.shop.seckill.mapper.OrderMapper;
import com.shop.seckill.mapper.entity.OrderEntity;
import com.shop.seckill.serviceApi.OrderSeckillService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
public class OrderSeckillServiceImpl extends BaseApiService<JSONObject> implements OrderSeckillService {
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public BaseResponse<JSONObject> getOrder(String phone, Long seckillId) {
		if (StringUtils.isEmpty(phone)) {
			return setResultError("手机号码不能为空!");
		}
		if (seckillId == null) {
			return setResultError("商品库存id不能为空!");
		}
		OrderEntity orderEntity = orderMapper.findByOrder(phone, seckillId);
		if (orderEntity == null) {
			return setResultError("正在排队中.....");
		}
		return setResultSuccess("恭喜你秒杀成功!");
	}

}
