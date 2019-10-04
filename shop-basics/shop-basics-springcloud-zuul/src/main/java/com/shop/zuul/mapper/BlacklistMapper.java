package com.shop.zuul.mapper;


import com.shop.zuul.mapper.entity.MeiteBlacklist;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistMapper {

	@Select(" select ID AS ID ,ip_addres AS ipAddres,restriction_type  as restrictionType, availability  as availability from meite_blacklist where  ip_addres =#{ipAddres} and  restriction_type='1' ")
	MeiteBlacklist findBlacklist(String ipAddr);

}
