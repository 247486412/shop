package com.shop.seckill.mapper;

import com.shop.seckill.mapper.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {

  @Insert("INSERT INTO `shop_order` VALUES (#{seckillId},#{userPhone}, '1', now());")
  int insertOrder(OrderEntity orderEntity);

  OrderEntity findByOrder(String phone, Long seckillId);
}

