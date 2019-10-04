package com.shop.pay.callback.template;

import com.shop.pay.constant.PayConstant;
import com.shop.pay.mapper.PaymentTransactionLogMapper;
import com.shop.pay.mapper.entity.PaymentTransactionLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description 使用模板方法模式, 定义一个模板,在抽象类定义公共的功能, 用于重构异步回调代码
 * @ClassName AbstractPayCallbackTemplate
 * @Author Administrator
 * @CreateTime 2019/9/18 16:24
 * Version 1.0
 **/
@Slf4j
@Component
public abstract class AbstractPayCallbackTemplate {
  @Autowired
  private PaymentTransactionLogMapper paymentTransactionLogMapper;
  @Resource
  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  //统一的异步回调方法
  public String asyncCallback(HttpServletRequest request, HttpServletResponse response) {
	//获取所有请求参数封装成map集合，并验证参数
	Map<String, String> verifySignature = verifySignature(request, response);
	String paymentId = verifySignature.get("orderId");
	if (StringUtils.isBlank(paymentId)) {
	  return verifyFail();
	}
	//把支付id和支付返回的结果集合存入数据库支付日志表和日志系统中,如果支付成功银联回调时由于系统、代码或硬件等文件没有把状态改为已支付,需要手动进行补偿
	threadPoolTaskExecutor.execute(new PayLogThread(paymentId, verifySignature));
	String result = verifySignature.get(PayConstant.RESULT_NAME);
	//如果验证签名返回203表示验证失败,执行验证失败的方法。
	if (PayConstant.RESULT_PAYCODE_203.equals(result)) {
	  return verifyFail();
	}

	//执行具体的异步回调业务逻辑
	return asyncCallbackService(verifySignature);
  }


  //打印日志和把支付响应结果存入数据库为公共的功能,可以直接在父类中定义好，此方法需为异步,加快执行效率,避免阻塞,并发量过高此方法可以加同步synchronized
  private void payLog(String paymentId, Map<String, String> verifySignature) {
	log.info("paymentId:" + paymentId, verifySignature);
	PaymentTransactionLogEntity paymentTransactionLog = new PaymentTransactionLogEntity();
	paymentTransactionLog.setTransactionId(paymentId);
	paymentTransactionLog.setAsyncLog(verifySignature.toString());
	paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLog);

  }

  //第三方支付平台支付成功后,异步回调执行的具体具体业务逻辑,比如会送积分,可以参加活动等,
  // 执行成功后返回状态码,告诉银联执行结果,不然第三方支付平台会再次发生回调请求
  public abstract String asyncCallbackService(Map<String, String> verifySignature);

  //参数验证失败时执行的方法
  public abstract String verifyFail();

  //验证参数是否正确,是否被篡改,由于封装和验证参数银联和支付宝不同，所有需要不同的子类来实现具体的方法
  public abstract Map<String, String> verifySignature(HttpServletRequest request, HttpServletResponse response);


  /**
   * 使用多线程写入日志目的：加快响应 提高程序效率 使用线程池维护线程
   */
  class PayLogThread implements Runnable {
	private String paymentId;
	private Map<String, String> verifySignature;

	public PayLogThread(String paymentId, Map<String, String> verifySignature) {
	  this.paymentId = paymentId;
	  this.verifySignature = verifySignature;
	}

	@Override
	public void run() {
	  payLog(paymentId, verifySignature);
	}

  }

}

