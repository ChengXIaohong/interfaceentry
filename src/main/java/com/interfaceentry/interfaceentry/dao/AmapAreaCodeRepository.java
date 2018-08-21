package com.interfaceentry.interfaceentry.dao;

import com.interfaceentry.interfaceentry.entity.AmapAreaCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/***
 *
 * @author xiongchuang
 * @date 2018-01-15
 */
@Repository
public interface AmapAreaCodeRepository extends
        JpaRepository<AmapAreaCodeEntity, Long>, JpaSpecificationExecutor<AmapAreaCodeEntity> {

    AmapAreaCodeEntity findFirstByProvinceNameAndLevel(String provinceName, String level);
    AmapAreaCodeEntity findFirstByProvinceCodeAndLevel(String provinceCode, String level);
    List<AmapAreaCodeEntity> findAllByProvinceCodeInAndLevelOrderByProvinceCodeAsc(Collection<String> provinceCodes, String level);
    List<AmapAreaCodeEntity> findAllByLevelOrderByProvinceCodeAsc(String level);


    AmapAreaCodeEntity findFirstByCityNameAndLevel(String cityName, String level);
    AmapAreaCodeEntity findFirstByCityCodeAndLevel(String cityCode, String level);
    List<AmapAreaCodeEntity> findAllByCityCodeInAndLevelOrderByProvinceCodeAscCityCodeAsc(Collection<String> cityCodes, String level);
    List<AmapAreaCodeEntity> findAllByProvinceCodeAndLevelOrderByProvinceCodeAscCityCodeAsc(String provinceCode, String level);


    AmapAreaCodeEntity findFirstByDistrictNameAndLevel(String districtName, String level);
    AmapAreaCodeEntity findFirstByDistrictCodeAndLevel(String districtCode, String level);
    List<AmapAreaCodeEntity> findAllByDistrictCodeInAndLevelOrderByProvinceCodeAscCityCodeAscDistrictCodeAsc(Collection<String> districtCodes, String level);
    List<AmapAreaCodeEntity> findAllByCityCodeAndLevelOrderByProvinceCodeAscCityCodeAscDistrictCodeAsc(String cityCode, String level);
}
