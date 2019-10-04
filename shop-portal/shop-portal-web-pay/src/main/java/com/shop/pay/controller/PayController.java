package com.shop.pay.controller;

import com.shop.base.BaseResponse;
import com.shop.dto.pay.output.PaymentChannelDTO;
import com.shop.dto.pay.output.PaymentTransactionDTO;
import com.shop.pay.feign.PaymentChannelFeign;
import com.shop.pay.feign.PaymentTransactionInfoServiceFeign;
import com.shop.web.base.BaseWebController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 * @Description
 * @ClassName PayController
 * @Author Administrator
 * @CreateTime 2019/9/16 11:16
 * Version 1.0
 **/
@Controller
public class PayController extends BaseWebController {
  @Autowired
  private PaymentTransactionInfoServiceFeign paymentTransactionInfoServiceFeign;
  @Autowired
  private PaymentChannelFeign paymentChannelFeign;

  @RequestMapping("/pay")
  public String pay(String payToken, Model model){
    if (StringUtils.isBlank(payToken)){
      setErrorMsg(model,"token不能为空！");
      return ERROR_500_FTL;
	}
	BaseResponse<PaymentTransactionDTO> paymentTransaction = paymentTransactionInfoServiceFeign.tokenByPaymentTransaction(payToken);
    if (paymentTransaction==null){
      setErrorMsg(model,paymentTransaction.getMsg());
      return ERROR_500_FTL;
	}
	PaymentTransactionDTO data = paymentTransaction.getData();
    model.addAttribute("data",data);
	List<PaymentChannelDTO> paymentChanneList = paymentChannelFeign.selectAll();
	model.addAttribute("paymentChanneList", paymentChanneList);
	model.addAttribute("payToken", payToken);
	return INDEX_FTL;
  }
}
