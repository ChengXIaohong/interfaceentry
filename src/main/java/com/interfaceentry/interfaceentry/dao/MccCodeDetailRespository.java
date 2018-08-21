package com.interfaceentry.interfaceentry.dao;

import com.interfaceentry.interfaceentry.entity.MccCodeDetailEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface MccCodeDetailRespository extends CrudRepository<MccCodeDetailEntity, Long> {
    List<MccCodeDetailEntity> findListByMccIdInAndStateTrue(Set<Long> mccIds);
}
