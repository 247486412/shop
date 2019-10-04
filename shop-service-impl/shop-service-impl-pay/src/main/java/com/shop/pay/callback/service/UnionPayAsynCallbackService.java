package com.shop.pay.callback.service;

import com.shop.pay.callback.template.AbstractPayCallbackTemplate;
import com.shop.pay.callback.template.factory.TemplateFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @ClassName UnionPayAsynCallbackService
 * @Author Administrator
 * @CreateTime 2019/9/19 14:13
 * Version 1.0
 **/
@RestController
public class UnionPayAsynCallbackService {
  //如果一个类或接口只有一个实现类@Resource和@AutoWired都可以使用.有多个实现类最好使用@Resource,因为默认以name注入,可以指定type和name
//  @Resource(name = "unionPayCallbackTemplate")
//  private AbstractPayCallbackTemplate abstractPayCallbackTemplate;
  private static final String UNIONPAYCALLBACKTEMPLATE_NAME = "unionPayCallbackTemplate";

  @RequestMapping("/unionPayAsynCallback")
  public String unionPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) {
    AbstractPayCallbackTemplate payCallbackTemplate = TemplateFactory.getPayCallbackTemplate(UNIONPAYCALLBACKTEMPLATE_NAME);
    return payCallbackTemplate.asyncCallback(req,resp);
  }
}
