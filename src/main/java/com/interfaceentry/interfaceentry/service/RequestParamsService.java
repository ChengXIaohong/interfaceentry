package com.interfaceentry.interfaceentry.service;

import com.interfaceentry.interfaceentry.entity.ParamsEntity;

/**
 * 请求对象service
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-18 19:13
 **/
public interface RequestParamsService {

    /**
     * 增加请求对象
     *
     * @param requestParamsEntity
     * @return
     */
    ParamsEntity saveOrUpDate(ParamsEntity requestParamsEntity);

    /**
     * 得到一个代参数实例
     *
     * @return
     */
    ParamsEntity getParamsInstance();

    /**
     * 按照requestSeqId找paramsEntity
     * @param requestSeqId
     * @return
     */
    ParamsEntity getParamsByRequestSeqId(String requestSeqId);
}
