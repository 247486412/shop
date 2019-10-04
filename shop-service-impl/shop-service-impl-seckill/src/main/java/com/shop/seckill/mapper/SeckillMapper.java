package com.shop.seckill.mapper;

import com.shop.seckill.mapper.entity.SeckillEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @ClassName SeckillMapper
 * @Author Administrator
 * @CreateTime 2019/9/26 20:23
 * Version 1.0
 **/
@Repository
public interface SeckillMapper {
  @Select("SELECT seckill_id AS seckillId,name as name,inventory as inventory,start_time as startTime,end_time as endTime,create_time as createTime,version as version from shop_seckill where seckill_id=#{seckillId}")
  SeckillEntity findBySeckillId(Long seckillId);

  /**
   * @param seckillId
   * @return
   */

  @Update("update shop_seckill set inventory=inventory-1, version=version+1 where  seckill_id=#{seckillId} and inventory>0  and version=#{version} ;")
  int inventoryDeduction(@Param("seckillId") Long seckillId, @Param("version") Long version);

  // update meite_seckill set inventory=inventory-1 where
  // seckill_id=#{seckillId} and 1>0;
  // update meite_seckill set inventory=inventory-1 where
  // seckill_id=#{seckillId} and 1>0;
  // 在mysql中每次在更新数据库有行锁机制

  // UPDATE meite_seckill SET inventory=inventory-1 ,version=version+1 FROM
  // meite_seckill WHERE seckill_id='10001' and version='1';
}
