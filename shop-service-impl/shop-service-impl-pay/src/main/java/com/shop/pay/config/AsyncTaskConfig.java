package com.shop.pay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @Description springboot多线程异步配置类
 * @ClassName AsyncTaskConfig
 * @Author Administrator
 * @CreateTime 2019/9/19 18:59
 * Version 1.0
 **/
@EnableAsync
@Configuration
@Slf4j
public class AsyncTaskConfig implements AsyncConfigurer {

  /**
   * 最小线程数(核心线程数)
   */
  @Value("${threadPool.corePoolSize}")
  private int corePoolSize;
  /**
   * 最大线程数
   */
  @Value("${threadPool.maxPoolSize}")
  private int maxPoolSize;
  /**
   * 等待队列(队列最大长度)
   */
  @Value("${threadPool.queueCapacity}")
  private int queueCapacity;

  @Override
  @Bean(name = "threadPoolTaskExecutor")
  public Executor getAsyncExecutor() {
	ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
	// 最小线程数(核心线程数)
	taskExecutor.setCorePoolSize(corePoolSize);
	// 最大线程数
	taskExecutor.setMaxPoolSize(maxPoolSize);
	// 等待队列(队列最大长度)
	taskExecutor.setQueueCapacity(queueCapacity);
	taskExecutor.setWaitForTasksToCompleteOnShutdown(true);//等待任务在关机时完成--表明等待所有线程执行完
	taskExecutor.setAwaitTerminationSeconds(600);// 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
	taskExecutor.setThreadNamePrefix("paymentCallBackLogAsync-");//  线程名称前缀
	taskExecutor.initialize();
	return taskExecutor;
  }

  //多线程异步异常处理
  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	return new AsyncExceptionHandler();
  }

  /**
   * 定义多线程异常处理类
   */
  class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
	  System.out.println("-------------》》》捕获线程异常信息");
	  log.info("Exception message - " + throwable.getMessage());
	  log.info("Method name - " + method.getName());
	  for (Object param : objects) {
		log.info("Parameter value - " + param);
	  }
	}
  }
}

