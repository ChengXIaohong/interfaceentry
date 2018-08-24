package com.interfaceentry.interfaceentry.controller;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;

/**
 * 修改控制器
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-24 15:54
 **/
@Controller
@Validated
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/baseInfo")
    public ModelAndView updateBaseInfo(@NotNull MerchantEntity merchantEntity) {
        ModelAndView modelAndView = new ModelAndView("modify/baseInfo");

        merchantService.reSubmitionBaseInfo(merchantEntity);

        modelAndView.addObject("merchantInfo" , merchantEntity);
        return modelAndView;
    }
}
