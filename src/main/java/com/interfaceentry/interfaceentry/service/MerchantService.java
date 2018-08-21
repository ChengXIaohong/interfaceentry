package com.interfaceentry.interfaceentry.service;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.model.SettleBankInfo;

import java.util.List;

/**
 * 商户service
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-18 19:06
 **/
public interface MerchantService {

    /**
     * 添加商户
     *
     * @param merchantEntity
     * @return
     */
    MerchantEntity saveOrUpdate(MerchantEntity merchantEntity);

    /**
     * 获取某种状态列表
     *
     * @param status
     * @return
     */
    List<MerchantEntity> getMerchantByStatus(String status);

    /**
     * 状态修改
     *
     * @param id
     * @param submissionStatus
     * @return
     */
    String updateSubmissionStatusById(Long id, String submissionStatus);

    /**
     * 提交到翼支付审核
     *
     * @param id
     */
    void submiToYZHSH(Long id);

    List<SettleBankInfo> getSettleBankInfos(String bankName, String bankCode);

    /**
     * 按id寻找商户
     * @param id
     * @return
     */
    MerchantEntity getById(Long id);


}
