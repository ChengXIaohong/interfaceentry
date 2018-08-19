package com.interfaceentry.interfaceentry.service;

import com.interfaceentry.interfaceentry.entity.RequestParamsEntity;

/**
 * 请求对象service
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-18 19:13
 **/
public interface RequestParamsService {

    /**
     * 增加请求对象
     * @param requestParamsEntity
     * @return
     */
    RequestParamsEntity saveOrUpDate(RequestParamsEntity requestParamsEntity);
}
