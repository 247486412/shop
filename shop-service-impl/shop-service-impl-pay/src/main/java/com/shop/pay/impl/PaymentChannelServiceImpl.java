package com.shop.pay.impl;

import com.shop.core.utils.MapperUtils;
import com.shop.dto.pay.output.PaymentChannelDTO;
import com.shop.pay.mapper.PaymentChannelMapper;
import com.shop.pay.mapper.entity.PaymentChannelEntity;
import com.shop.pay.serviceApi.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 查询所有支付渠道
 * @ClassName PaymentChannelServiceImpl
 * @Author Administrator
 * @CreateTime 2019/9/16 16:19
 * Version 1.0
 **/
@RestController
public class PaymentChannelServiceImpl implements PaymentChannelService {
  @Autowired
  private PaymentChannelMapper paymentChannelMapper;

  @Override
  public List<PaymentChannelDTO> selectAll() {
	List<PaymentChannelEntity> paymentChannelEntities = paymentChannelMapper.selectAll();
	return MapperUtils.mapAsList(paymentChannelEntities, PaymentChannelDTO.class);
  }
}

