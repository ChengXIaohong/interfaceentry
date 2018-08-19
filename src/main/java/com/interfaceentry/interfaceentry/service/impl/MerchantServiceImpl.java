package com.interfaceentry.interfaceentry.service.impl;

import com.interfaceentry.interfaceentry.dao.MerchantRespository;
import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import com.interfaceentry.interfaceentry.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 商户service实现
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-18 19:08
 **/
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRespository merchantRespository;

    @Override
    public MerchantEntity saveOrUpdate(MerchantEntity merchantEntity) {
        //若是修改 增加修改时间
        if (null != merchantEntity.getId()) {
            merchantEntity.setUpdateAt(System.currentTimeMillis());
        } else {
            //若是首次添加 则加入创建时间  并且把状态设置为待平台审核
            merchantEntity.setCreateAt(System.currentTimeMillis());
            merchantEntity.setSubmissionAt(System.currentTimeMillis());
            merchantEntity.setSubmissionStatus(Constants.SubmitionStatus.JBGSH_ING.name());
        }
        return merchantRespository.save(merchantEntity);
    }

    @Override
    public List<MerchantEntity> getMerchantByStatus(String status) {
        return merchantRespository.findBySubmissionStatusEquals(status);
    }

    @Override
    public String updateSubmissionStatusById(Long id, String submissionStatus) {
        Optional<MerchantEntity> optional = merchantRespository.findById(id);
        if (!optional.isPresent()) {
            return Constants.ERROR;
        }
        MerchantEntity merchantEntity = optional.get();
        merchantEntity.setSubmissionStatus(submissionStatus);
        this.saveOrUpdate(merchantEntity);
        return Constants.SUCCESS;
    }

    @Override
    public void submiToYZHSH(Long id) {
        Optional<MerchantEntity> optional = merchantRespository.findById(id);
        if (!optional.isPresent()) {
          throw  new RuntimeException("非法商户ID");
        }

        MerchantEntity merchantEntity = optional.get();

        //todo : 完善提交到翼支付审核逻辑

    }
}
