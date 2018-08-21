package com.interfaceentry.interfaceentry.controller;

import com.interfaceentry.interfaceentry.entity.AmapAreaCodeEntity;
import com.interfaceentry.interfaceentry.service.AmapAreaCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 地区controller
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-21 17:20
 **/
@Controller
@RequestMapping("/area")
public class AmapAreaCodeController {
    @Autowired
    private AmapAreaCodeService amapAreaCodeService;

    /**
     * 获取所有省
     * @return
     */
    @RequestMapping("/province")
    @ResponseBody
    public List<AmapAreaCodeEntity> getAllProvince() {
        return amapAreaCodeService.findProvinceList();
    }

    /**
     * 获取所有省
     * @return
     */
    @RequestMapping("/city")
    @ResponseBody
    public List<AmapAreaCodeEntity> getCityByProvinceCode(String provinceCode) {
        return amapAreaCodeService.findCityListByProvinceCode(provinceCode);
    }
}
