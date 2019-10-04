package com.shop.seckill.serviceApi;

import com.alibaba.fastjson.JSONObject;
import com.shop.base.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 商品秒杀服务
 * @ClassName SeckillCommodityService
 * @Author Administrator
 * @CreateTime 2019/9/26 15:05
 * Version 1.0
 **/
public interface SeckillCommodityService {
  @RequestMapping("/seckill")
  public BaseResponse<JSONObject> seckill(@RequestParam("phone") String phone, @RequestParam("seckillId") Long seckillId);

  @RequestMapping("/addSeckillToken")
  public BaseResponse<JSONObject> addSeckillToken(@RequestParam("seckillId") Long seckillId, @RequestParam("tokenQuantity") Long tokenQuantity);
}
