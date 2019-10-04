package com.shop.seckill.serviceApi;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @ClassName OrderSeckillService
 * @Author Administrator
 * @CreateTime 2019/9/27 22:27
 * Version 1.0
 **/
public interface OrderSeckillService {
  @RequestMapping("/getOrder")
  public BaseResponse<JSONObject> getOrder(@RequestParam("phone") String phone, @RequestParam("seckillId") Long seckillId);
}
