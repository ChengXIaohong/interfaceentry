package com.interfaceentry.interfaceentry.controller;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.entity.ParamsEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import com.interfaceentry.interfaceentry.service.RequestParamsService;
import com.interfaceentry.interfaceentry.tools.Constants;
import com.interfaceentry.interfaceentry.tools.OnLineExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 申请聚合支付接口
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 15:03
 **/
@Controller
public class ApplyController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private RequestParamsService requestParamsService;

    /**
     * 到申请支付页面
     *
     * @return
     */
    @RequestMapping("/toapply")
    public ModelAndView toApply() {
        ModelAndView modelAndView = new ModelAndView("apply");
        return modelAndView;
    }

    /**
     * 处理申请数据
     *
     * @param merchantEntity
     * @return
     */
    @RequestMapping("/apply")
    public ModelAndView Apply(MerchantEntity merchantEntity) {
        ModelAndView modelAndView = new ModelAndView("apply-status");
        merchantEntity = merchantService.saveOrUpdate(merchantEntity);
        modelAndView.addObject("merchant", merchantEntity);
        return modelAndView;
    }

    /**
     * 获取某种状态的申请
     *
     * @param status
     * @return
     */
    @RequestMapping("/getApply")
    public ModelAndView getApply(String status) {
        OnLineExecutorService.getSubmitResult("111" , "222");
        ModelAndView modelAndView = new ModelAndView("getApply");
        List<MerchantEntity> merchantList = merchantService.getMerchantByStatus(status);
        modelAndView.addObject("merchantList", merchantList);
        return modelAndView;
    }

    /**
     * 审核 修改状态
     *
     * @param id
     * @param submissionStatus
     * @return
     */
    @RequestMapping("/updateStatus")
    @ResponseBody
    public String updateStatus(Long id, String submissionStatus) {
        if (Constants.SubmitionStatus.YZFSH_ING.name().equals(submissionStatus)) {
            merchantService.submiToYZHSH(id);
            return merchantService.updateSubmissionStatusById(id, submissionStatus);
        } else {
            return merchantService.updateSubmissionStatusById(id, submissionStatus);
        }
    }
}
