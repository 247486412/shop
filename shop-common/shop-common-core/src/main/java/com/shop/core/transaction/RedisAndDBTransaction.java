package com.shop.core.transaction;

import com.shop.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;


/**
 * Redis与 DataSource 事务封装,使redis和数据库事务保持一致,同时提交或同时回滚
 */
@Component
//此类一定要多例
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
public class RedisAndDBTransaction {
  @Autowired
  private RedisUtil redisUtil;
  //数据源事务管理器
  @Autowired
  private DataSourceTransactionManager dataSourceTransactionManager;
  private TransactionStatus transactionStatus;

  /**
   * 开始事务 采用默认传播行为
   *
   * @return
   */
  public void begin() {
	// 手动begin数据库事务
	// 1.开启数据库的事务 事务传播行为
	transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
	// 2.开启redis事务
	redisUtil.begin();
  }

  /**
   * 提交事务
   */
  public void commit() throws Exception {
	// 该方法支持Redis与数据库事务同时提交,如果再提交redis事务会报错
	dataSourceTransactionManager.commit(transactionStatus);
  }

  /**
   * 回滚事务
   */
  public void rollback() {
	// 1.回滚数据库事务 redis事务和数据库的事务会同时回滚
	dataSourceTransactionManager.rollback(transactionStatus);
  }

}
