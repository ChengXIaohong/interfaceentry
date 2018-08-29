package com.interfaceentry.interfaceentry.controller;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import com.interfaceentry.interfaceentry.tools.FileTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 修改信息转发路由
     *
     * @param merchantId
     * @param modifyType
     * @return
     */
    @RequestMapping("")
    public ModelAndView modify(@NotNull Long merchantId, @NotNull String modifyType) {
        ModelAndView modelAndView = new ModelAndView("modify/" + modifyType);
        MerchantEntity merchantEntity = merchantService.getById(merchantId);
        if (null == merchantEntity) {
            throw new RuntimeException("非法商户主键:" + merchantId);
        }
        modelAndView.addObject("merchantInfo", merchantEntity);
        return modelAndView;
    }

    /**
     * 商户基础信息修改
     *
     * @param merchantEntity
     * @return
     */
    @RequestMapping("/baseInfo")
    public ModelAndView updateBaseInfo(@NotNull MerchantEntity merchantEntity) {
        ModelAndView modelAndView = new ModelAndView("modify/result");

        Boolean success = merchantService.reSubmitionBaseInfo(merchantEntity);

        modelAndView.addObject("success", success);
        return modelAndView;
    }

    /**
     * 商户资质信息修改
     *
     * @param merchantEntity
     * @return
     */
    @RequestMapping("/busiqualificationinfo")
    public ModelAndView updateBusiQualificationInfo(@NotNull MerchantEntity merchantEntity,
                                                    MultipartFile _identityCardFrontPic,
                                                    MultipartFile _identityCardReversePic,
                                                    MultipartFile _licensePic,
                                                    MultipartFile _storeInteriorPic,
                                                    MultipartFile _storeSignBoardPic) throws IOException {
        ModelAndView modelAndView = new ModelAndView("modify/result");

        MultipartFile[] multipartFiles = {_identityCardFrontPic, _identityCardReversePic, _licensePic, _storeInteriorPic, _storeSignBoardPic};
        String[] nameStrings = {"_identityCardFrontPic", "_identityCardReversePic", "_licensePic", "_storeInteriorPic", "_storeSignBoardPic"};
        Map<String, MultipartFile> fileMap = new HashMap<>(multipartFiles.length);
        for (int i = 0; i < multipartFiles.length; i++) {
            String name = nameStrings[i];
            MultipartFile multipartFile = multipartFiles[i];
            fileMap.put(name, multipartFile);
        }

        FileTools.initFile2Base64Set2Obj(merchantEntity, fileMap);

        Boolean success = merchantService.reSubmitionBusiqualificationinfo(merchantEntity);
        modelAndView.addObject("success", true);
        return modelAndView;
    }


}
