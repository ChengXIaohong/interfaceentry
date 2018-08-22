package com.interfaceentry.interfaceentry.dao;

import com.interfaceentry.interfaceentry.entity.ParamsEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * 请求记录操作dao
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 14:43
 **/
public interface RequestParamsRespository extends CrudRepository<ParamsEntity, Long> {
    /**
     * 以requestSeqId寻找ParamsEntity
     * @param requestSeqId
     * @return
     */
    ParamsEntity findFirstByRequestSeqIdEquals(String requestSeqId);

    /**
     * 找到最新请求的一组记录
     * @param merchantId
     * @return
     */
    ParamsEntity findFirstByMerchantIdEqualsOrderByIdDesc(Long merchantId);

}
