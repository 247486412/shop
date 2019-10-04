package com.shop.pay.callback.template.unionpay;

import com.alibaba.fastjson.JSONObject;
import com.shop.pay.callback.template.AbstractPayCallbackTemplate;
import com.shop.pay.constant.PayConstant;
import com.shop.pay.mapper.PaymentTransactionMapper;
import com.shop.pay.mapper.entity.PaymentTransactionEntity;
import com.shop.pay.mq.producer.IntegralProducer;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.UnionPayBase;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 银联支付异步回调模板实现, 参考银联demo，BackRcvResponse.java类代码
 * @ClassName UnionPayCallbackTemplate
 * @Author Administrator
 * @CreateTime 2019/9/18 17:54
 * Version 1.0
 **/
@Component
public class UnionPayCallbackTemplate extends AbstractPayCallbackTemplate {
  @Autowired
  private PaymentTransactionMapper paymentTransactionMapper;
  @Autowired
  private IntegralProducer integralProducer;

  @Override
  //对参数进行验签,确认是否被篡改
  public Map<String, String> verifySignature(HttpServletRequest request, HttpServletResponse response) {
	LogUtil.writeLog("BackRcvResponse接收后台通知开始");

	String encoding = request.getParameter(SDKConstants.param_encoding);
	// 获取银联通知服务器发送的后台通知参数
	Map<String, String> reqParam = getAllRequestParam(request);
	LogUtil.printRequestLog(reqParam);

	//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
	if (!AcpService.validate(reqParam, encoding)) {
	  LogUtil.writeLog("验证签名结果[失败].");
	  //验签失败，需解决验签问题
	  reqParam.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_203);
	} else {
	  LogUtil.writeLog("验证签名结果[成功].");
	  //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
	  //第三方支付平台返回的orderId为自己的paymentId,自己使用雪花算法生成的这笔支付订单实际支付金额的支付id
//	  String orderId =reqParam.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
	  //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
//	  String respCode = reqParam.get("respCode");
	  //返回200表示验签成功
	  reqParam.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_200);

	}
	LogUtil.writeLog("BackRcvResponse接收后台通知结束");
	//返回回调参数集合
	return reqParam;
  }

  //回调参数验签正确后执行的业务逻辑方法
  @Override
  @Transactional
  public String asyncCallbackService(Map<String, String> verifySignature) {
	//获取后台通知的数据，其他字段也可用类似方式获取
	String paymentId = verifySignature.get("orderId");
	//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
	String respCode = verifySignature.get("respCode");
	//银联响应回来的状态码不是00或A6表示交易失败
	if (!("00".equals(respCode) || "A6".equals(respCode))) {
	  return verifyFail();
	}
	PaymentTransactionEntity paymentTransactionEntity = paymentTransactionMapper.selectByPaymentId(paymentId);
	if (paymentTransactionEntity == null) {
	  return verifyFail();
	}
	//如果该支付订单已经支付成功就直接返回ok，避免网络延迟等原因造成银联重试发送回调请求时代码重复执行
	if (paymentTransactionEntity.getPaymentStatus().equals(PayConstant.PAY_STATUS_SUCCESS)) {
	  return PayConstant.YINLIAN_RESULT_SUCCESS;
	}
	//如果第一次执行该回调请求,把该支付订单的支付记录状态改为1表示已支付 。
	// 如果此处代码出问题导致支付状态没有修改为已支付,积分没有获取到等，可以使用定时任务每天查询交易表和日志，
	// 有问题的需要使用支付id调用第三方支付接口进行查询对账,手动进行补偿
	paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS, paymentId);

	//此处可以执行订单支付成功后的一些服务,比如积分服务增加积分或一些活动等

	this.addIntegral(paymentTransactionEntity);//增加积分

	//如果此处因为异常事务回滚原因导致更新支付状态失败，积分却增加，
	// 可以使用支付补偿队列,或在增加积分消费者处作补偿,把支付状态改为已支付

	//返回ok告诉银联回调成功,不然银联会重试发送回调请求
	return PayConstant.YINLIAN_RESULT_SUCCESS;
  }

  @Override
  public String verifyFail() {
	return PayConstant.YINLIAN_RESULT_FAIL;
  }


  /**
   * 基于MQ增加积分,使用异步投递消息
   */
  @Async
  public void addIntegral(PaymentTransactionEntity paymentTransaction) {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("paymentId", paymentTransaction.getPaymentId());
	jsonObject.put("userId", paymentTransaction.getUserId());
	jsonObject.put("integral", 100);
	integralProducer.sendMsg(jsonObject);
  }


  /**
   * 获取请求参数中所有的信息 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
   * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，
   * 这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
   *
   * @param request
   * @return
   */
  public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
	Map<String, String> res = new HashMap<String, String>();
	Enumeration<?> temp = request.getParameterNames();
	if (null != temp) {
	  while (temp.hasMoreElements()) {
		String en = (String) temp.nextElement();
		String value = request.getParameter(en);
		res.put(en, value);
		// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
		if (res.get(en) == null || "".equals(res.get(en))) {
		  // System.out.println("======为空的字段名===="+en);
		  res.remove(en);
		}
	  }
	}
	return res;
  }

  /**
   * 获取请求参数中所有的信息。
   * 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
   * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。
   * 理论应该可以调整struts配置使不影响，但请自己去研究。
   * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
   *
   * @param request
   * @return
   */
  public static Map<String, String> getAllRequestParamStream(final HttpServletRequest request) {
	Map<String, String> res = new HashMap<String, String>();
	try {
	  String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()), UnionPayBase.encoding);
	  LogUtil.writeLog("收到通知报文：" + notifyStr);
	  String[] kvs = notifyStr.split("&");
	  for (String kv : kvs) {
		String[] tmp = kv.split("=");
		if (tmp.length >= 2) {
		  String key = tmp[0];
		  String value = URLDecoder.decode(tmp[1], UnionPayBase.encoding);
		  res.put(key, value);
		}
	  }
	} catch (UnsupportedEncodingException e) {
	  LogUtil.writeLog("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":"
			  + e.getMessage());
	} catch (IOException e) {
	  LogUtil.writeLog("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e.getMessage());
	}
	return res;
  }

}
