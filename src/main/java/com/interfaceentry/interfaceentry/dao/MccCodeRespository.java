package com.interfaceentry.interfaceentry.dao;

import com.interfaceentry.interfaceentry.entity.MccCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MccCodeRespository extends CrudRepository<MccCodeEntity, Long> {
    List<MccCodeEntity> findListByStateTrue();

}
