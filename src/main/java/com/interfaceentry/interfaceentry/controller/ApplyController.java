package com.interfaceentry.interfaceentry.controller;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import com.interfaceentry.interfaceentry.service.RequestParamsService;
import com.interfaceentry.interfaceentry.service.model.MccCode;
import com.interfaceentry.interfaceentry.service.model.SettleBankInfo;
import com.interfaceentry.interfaceentry.tools.Constants;
import com.interfaceentry.interfaceentry.tools.FileTools;
import com.interfaceentry.interfaceentry.tools.OnLineExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申请聚合支付接口
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 15:03
 **/
@Controller
public class ApplyController extends FinalExceptionHandler {

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
    public ModelAndView Apply(MerchantEntity merchantEntity,
                              MultipartFile _identityCardFrontPic,
                              MultipartFile _identityCardReversePic,
                              MultipartFile _licensePic,
                              MultipartFile _storeInteriorPic,
                              MultipartFile _storeSignBoardPic) throws IOException {

        ModelAndView modelAndView = new ModelAndView("apply-status");

        MultipartFile[] multipartFiles = {_identityCardFrontPic, _identityCardReversePic, _licensePic, _storeInteriorPic, _storeSignBoardPic};
        String[] nameStrings = {"_identityCardFrontPic", "_identityCardReversePic", "_licensePic", "_storeInteriorPic", "_storeSignBoardPic"};
        Map<String, MultipartFile> fileMap = new HashMap<>(multipartFiles.length);
        for (int i = 0; i < multipartFiles.length; i++) {
            String name = nameStrings[i];
            MultipartFile multipartFile = multipartFiles[i];
            fileMap.put(name, multipartFile);
        }

        FileTools.initFile2Base64Set2Obj(merchantEntity, fileMap);


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
    public Map<String, String> updateStatus(Long id, String submissionStatus) {
        Map ret = new HashMap<>();
        String retCode;
        if (Constants.SubmitionStatus.YZFSH_ING.name().equals(submissionStatus)) {
            merchantService.submiToYZHSH(id);
            retCode = merchantService.updateSubmissionStatusById(id, submissionStatus);
        } else {
            retCode = merchantService.updateSubmissionStatusById(id, submissionStatus);
        }
        ret.put("resultCode", retCode);
        return ret;
    }


    /**
     * 根据银行名称查询银行信息
     */
    @RequestMapping("/getSettleBankInfos")
    @ResponseBody
    public List<SettleBankInfo> getSettleBankInfos(String bankName, String bankCode) throws UnsupportedEncodingException {
        return merchantService.getSettleBankInfos(bankName, bankCode);
    }


    /**
     * 获取轮询
     */
    @RequestMapping("/lunxun")
    @ResponseBody
    public Boolean lunxun(@RequestParam(name = "requestSeqId", required = true) String requestSeqId, @RequestParam(name = "merchantNo", required = true) String merchantNo) {
        OnLineExecutorService.getInstance().taskForGetResult(requestSeqId, merchantNo);
        return Boolean.TRUE;
    }


    /**
     * 获取Mcc
     */
    @RequestMapping("/getMccCode")
    @ResponseBody
    public List<MccCode> getMccCode() {
        return merchantService.getMccCode();
    }

    @RequestMapping("/kdypay/submition/callBack")
    @ResponseBody
    public Map<String, Object> submitCallBack(HttpServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> retCode = new HashMap<>();

        String json = this.getStreamAsString(request);

        if (StringUtils.isEmpty(json)) {
            ret.put("success", Boolean.FALSE);
            ret.put("errorCode", "000001");
            ret.put("errorMsg", "请求消息无效");
            retCode.put("statuscode", "FAIL");
            ret.put("result", retCode);
            return ret;
        }

        //回调逻辑处理
        merchantService.yzfSubmitionCallBack(json);

        ret.put("success", Boolean.TRUE);
        ret.put("errorCode", "000000");
        ret.put("errorMsg", "成功接收申请回调信息");
        retCode.put("statuscode", "SUCCESS");
        ret.put("result", retCode);
        return ret;
    }

    private String getStreamAsString(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = request.getInputStream();
            InputStreamReader ins = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ins);
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
