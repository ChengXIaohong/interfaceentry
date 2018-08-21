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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        this.initFile2Base64Set2Obj(merchantEntity, fileMap);


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
    public String updateStatus(Long id, String submissionStatus) {
        if (Constants.SubmitionStatus.YZFSH_ING.name().equals(submissionStatus)) {
            merchantService.submiToYZHSH(id);
            return merchantService.updateSubmissionStatusById(id, submissionStatus);
        } else {
            return merchantService.updateSubmissionStatusById(id, submissionStatus);
        }
    }


    /**
     * 根据银行名称查询银行信息
     */
    @RequestMapping("/getSettleBankInfos")
    @ResponseBody
    public List<SettleBankInfo> getSettleBankInfos(String bankName, String bankCode) {
        return merchantService.getSettleBankInfos(bankName, bankCode);
    }

    /**
     * 图片转换
     *
     * @param merchantEntity
     * @param fileMap
     */
    private void initFile2Base64Set2Obj(MerchantEntity merchantEntity, Map<String, MultipartFile> fileMap) throws IOException {
        //身份证正面
        MultipartFile _identityCardFrontPic = fileMap.get("_identityCardFrontPic");
        merchantEntity.setIdentityCardFrontPic(this.multipartFile2Base64(_identityCardFrontPic));

        MultipartFile _identityCardReversePic = fileMap.get("_identityCardReversePic");
        merchantEntity.setIdentityCardReversePic(this.multipartFile2Base64(_identityCardReversePic));

        MultipartFile _licensePic = fileMap.get("_licensePic");
        merchantEntity.setLicensePic(this.multipartFile2Base64(_licensePic));

        MultipartFile _storeInteriorPic = fileMap.get("_storeInteriorPic");
        merchantEntity.setStoreInteriorPic(this.multipartFile2Base64(_storeInteriorPic));

        MultipartFile _storeSignBoardPic = fileMap.get("_storeSignBoardPic");
        merchantEntity.setStoreSignBoardPic(this.multipartFile2Base64(_storeInteriorPic));


    }

    private String multipartFile2Base64(MultipartFile multipartFile) throws IOException {
        File f = null;
        if (multipartFile.equals("") || multipartFile.getSize() <= 0) {
            multipartFile = null;
        } else {
            InputStream ins = multipartFile.getInputStream();
            f = new File(multipartFile.getOriginalFilename());
            FileTools.inputStreamToFile(ins, f);
        }

        FileInputStream idReversePicInput = new FileInputStream(f);
        byte[] idReversePicBuffer = new byte[(int) f.length()];
        idReversePicInput.read(idReversePicBuffer);
        idReversePicInput.close();
        //操作完成后删除文件
        f.delete();
        return Base64.getEncoder().encodeToString(idReversePicBuffer);
    }


    /**
     * 获取Mcc
     */
    @RequestMapping("/getMccCode")
    @ResponseBody
    public List<MccCode> getMccCode() {
        return merchantService.getMccCode();
    }
}
