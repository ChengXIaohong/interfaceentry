package com.interfaceentry.interfaceentry.controller;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 信息修改控制器
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-22 16:29
 **/
@Controller
public class ModifyController extends FinalExceptionHandler {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/modify")
    private ModelAndView modify(Long merchantId, String modifyType) {
        ModelAndView modelAndView = new ModelAndView("modify/" + modifyType);
        MerchantEntity merchantEntity = merchantService.getById(merchantId);
        if (null == merchantEntity) {
            throw new RuntimeException("非法商户主键:" + merchantId);
        }
        modelAndView.addObject("merchantInfo",merchantEntity);
        return modelAndView;
    }
}
