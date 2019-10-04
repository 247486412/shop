package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

@Component
@JobHandler(value = "PayJobHandler")
public class PayJobHandler extends IJobHandler {
  @Override
  public ReturnT<String> execute(String param){
	System.out.println("执行支付对账功能,任务参数："+param);
	return SUCCESS;
  }
}
