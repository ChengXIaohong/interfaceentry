package com.interfaceentry.interfaceentry.dao;

import com.interfaceentry.interfaceentry.entity.RequestParamsEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * 请求记录操作dao
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 14:43
 **/
public interface RequestParamsRespository extends CrudRepository<RequestParamsEntity, Long> {
}
