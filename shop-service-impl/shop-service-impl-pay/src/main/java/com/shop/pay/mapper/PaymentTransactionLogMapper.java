package com.shop.pay.mapper;

import com.shop.pay.mapper.entity.PaymentTransactionLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionLogMapper {

	@Insert("INSERT INTO `payment_transaction_log` VALUES (NULL, NULL, #{asyncLog},NULL, #{transactionId}, null, null, NOW(), null, NOW());")
	public int insertTransactionLog(PaymentTransactionLogEntity paymentTransactionLog);

}
