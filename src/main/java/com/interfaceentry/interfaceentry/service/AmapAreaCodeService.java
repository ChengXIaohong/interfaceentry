package com.interfaceentry.interfaceentry.service;

import com.interfaceentry.interfaceentry.entity.AmapAreaCodeEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/***
 *高德行政区域相关API
 */
public interface AmapAreaCodeService {
    AmapAreaCodeEntity save(AmapAreaCodeEntity amapAreaCode);

    /**
     * 获取省  by name
     */
    AmapAreaCodeEntity findProvinceByName(String provinceName);


    /**
     * 获取省  by code
     */
    AmapAreaCodeEntity findProvinceByCode(String code);


    /**
     * 获取省  by codes
     */
    List<AmapAreaCodeEntity> findProvinceByCodes(Collection<String> codes);

    /**
     * 获取省  by codes
     */
    Map<String, AmapAreaCodeEntity> findProvinceMapByCodes(Collection<String> codes);

    /**
     * 获取省  所有
     */
    List<AmapAreaCodeEntity> findProvinceList();


    /**
     * 获取省  所有
     */
    Map<String, AmapAreaCodeEntity> findProvinceMap();

    /**
     * 获取市  by name
     */
    AmapAreaCodeEntity findCityByName(String cityName);

    /**
     * 获取市  by code
     */
    AmapAreaCodeEntity findCityByCode(String code);

    /**
     * 获取市  by codes
     */
    List<AmapAreaCodeEntity> findCityListByCodes(Collection<String> codes);

    /**
     * 获取市  by codes
     */
    Map<String, AmapAreaCodeEntity> findCityMapByCodes(Collection<String> codes);

    /**
     * 获取市  by province code
     */
    List<AmapAreaCodeEntity> findCityListByProvinceCode(String code);

    /**
     * 获取市  by province code
     */
    Map<String, AmapAreaCodeEntity> findCityMapByProvinceCode(String code);

    /**
     * 获取县  by name
     */
    AmapAreaCodeEntity findDistrictByName(String districtName);

    /**
     * 获取县  by code
     */
    AmapAreaCodeEntity findDistrictByCode(String code);

    /**
     * 获取县  by codes
     */
    List<AmapAreaCodeEntity> findDistrictListByCodes(Collection<String> codes);

    /**
     * 获取县  by codes
     */
    Map<String, AmapAreaCodeEntity> findDistrictMapByCodes(Collection<String> codes);

    /**
     * 获取县  by city code
     */
    List<AmapAreaCodeEntity> findDistrictListByCityCode(String code);

    /**
     * 获取县  by city code
     */
    Map<String, AmapAreaCodeEntity> findDistrictMapByCityCode(String code);


}
