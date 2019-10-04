package com.shop.bean;

import org.springframework.beans.BeanUtils;

/**
 * @Description
 * @ClassName MyBeanUtils
 * @Author Administrator
 * @CreateTime 2019/9/2 18:46
 * Version 1.0
 **/
public class MyBeanUtils<T, H> {
  /**
   * 把两个类相同属性的属性值进行转换,把前一个类的属性值赋给后一个类
   * @param object
   * @param poClass
   * @param <H>
   * @return
   */
  public static <H> H copyProperties(Object object, Class<H> poClass) {
	if (object == null) {
	  return null;
	}
	if (poClass == null) {
	  return null;
	}
	try {
	  //给传入的class对象创建实例并转换,然后返回转换后的对象
	  H h = poClass.newInstance();
	  BeanUtils.copyProperties(object, h);
	  return h;
	} catch (Exception ignored) {
	  return null;
	}
  }

  /**
   * PO转换为DOT
   */
//  public static <DTO> DTO poToDto(Object poEntity, Class<DTO> dtoClass) {
//	if (poEntity == null) {
//	  return null;
//	}
//	if (dtoClass == null) {
//	  return null;
//	}
//	try {
//	  //给传入的class对象创建实例并转换,然后返回转换后的对象
//	  DTO dto = dtoClass.newInstance();
//	  BeanUtils.copyProperties(poEntity, dto);
//	  return dto;
//	} catch (Exception ignored) {
//	  return null;
//	}
//  }
}
