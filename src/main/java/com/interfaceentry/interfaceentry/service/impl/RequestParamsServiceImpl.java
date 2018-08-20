package com.interfaceentry.interfaceentry.service.impl;

import com.interfaceentry.interfaceentry.dao.RequestParamsRespository;
import com.interfaceentry.interfaceentry.entity.ParamsEntity;
import com.interfaceentry.interfaceentry.service.RequestParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请求对象service实现
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-18 19:15
 **/
@Service
public class RequestParamsServiceImpl implements RequestParamsService {

    @Autowired
    private RequestParamsRespository requestParamsRespository;
    @Override
    public ParamsEntity saveOrUpDate(ParamsEntity requestParamsEntity) {
        return requestParamsRespository.save(requestParamsEntity);
    }
}