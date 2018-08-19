package com.interfaceentry.interfaceentry.dao;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 商户操作dao
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 14:41
 **/
public interface MerchantRespository extends CrudRepository<MerchantEntity , Long> {
    List<MerchantEntity> findBySubmissionStatusEquals(String status);
}
