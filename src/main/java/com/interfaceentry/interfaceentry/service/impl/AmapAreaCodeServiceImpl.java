package com.interfaceentry.interfaceentry.service.impl;

import com.interfaceentry.interfaceentry.config.CommonConstants;
import com.interfaceentry.interfaceentry.dao.AmapAreaCodeRepository;
import com.interfaceentry.interfaceentry.entity.AmapAreaCodeEntity;
import com.interfaceentry.interfaceentry.service.AmapAreaCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Validated
public class AmapAreaCodeServiceImpl implements AmapAreaCodeService {
    @Autowired
    private AmapAreaCodeRepository amapAreaCodeRepository;

    @Override
    public AmapAreaCodeEntity save(AmapAreaCodeEntity amapAreaCode) {
        AmapAreaCodeEntity amapAreaCodeEntity = null;
        if (CommonConstants.AmapAreaCodeConstant.Level.PROVINCE.equals(amapAreaCode.getLevel())) {
            amapAreaCodeEntity = amapAreaCodeRepository.findFirstByProvinceCodeAndLevel(amapAreaCode.getProvinceCode(), CommonConstants.AmapAreaCodeConstant.Level.PROVINCE);
        } else if (CommonConstants.AmapAreaCodeConstant.Level.CITY.equals(amapAreaCode.getLevel())) {
            amapAreaCodeEntity = amapAreaCodeRepository.findFirstByCityCodeAndLevel(amapAreaCode.getCityCode(), CommonConstants.AmapAreaCodeConstant.Level.CITY);
        } else if (CommonConstants.AmapAreaCodeConstant.Level.DISTRICT.equals(amapAreaCode.getLevel())) {
            amapAreaCodeEntity = amapAreaCodeRepository.findFirstByDistrictCodeAndLevel(amapAreaCode.getDistrictCode(), CommonConstants.AmapAreaCodeConstant.Level.DISTRICT);
        } else {
            throw new RuntimeException("传入无效参数");
        }


        if (amapAreaCodeEntity != null) {
            amapAreaCodeEntity.setId(amapAreaCodeEntity.getId());
        }

        amapAreaCodeEntity = amapAreaCodeRepository.save(amapAreaCodeEntity);

        return amapAreaCodeEntity;
    }

    private Map<String, AmapAreaCodeEntity> getAmapAreaCodeMap(List<AmapAreaCodeEntity> provinceByCodes) {
        if (CollectionUtils.isEmpty(provinceByCodes)) {
            return Collections.EMPTY_MAP;
        }
        return provinceByCodes.stream().collect(Collectors.toMap(AmapAreaCodeEntity::getProvinceCode, Function.identity(), (o1, o2) -> o1));
    }

    //市
    private Map<String, AmapAreaCodeEntity> getAmapAreaCodeMapForCity(List<AmapAreaCodeEntity> provinceByCodes) {
        if (CollectionUtils.isEmpty(provinceByCodes)) {
            return Collections.EMPTY_MAP;
        }
        return provinceByCodes.stream().collect(Collectors.toMap(AmapAreaCodeEntity::getCityCode, Function.identity(), (o1, o2) -> o1));
    }

    //区县
    private Map<String, AmapAreaCodeEntity> getAmapAreaCodeMapForDistrict(List<AmapAreaCodeEntity> provinceByCodes) {
        if (CollectionUtils.isEmpty(provinceByCodes)) {
            return Collections.EMPTY_MAP;
        }
        return provinceByCodes.stream().collect(Collectors.toMap(AmapAreaCodeEntity::getDistrictCode, Function.identity(), (o1, o2) -> o1));
    }

    @Override
    public AmapAreaCodeEntity findProvinceByName(String provinceName) {
        AmapAreaCodeEntity areaCodeEntity = amapAreaCodeRepository.findFirstByProvinceNameAndLevel(provinceName, CommonConstants.AmapAreaCodeConstant.Level.PROVINCE);
        return areaCodeEntity == null ? new AmapAreaCodeEntity() : areaCodeEntity;
    }

    @Override
    public AmapAreaCodeEntity findProvinceByCode(String code) {
        AmapAreaCodeEntity areaCodeEntity = amapAreaCodeRepository.findFirstByProvinceCodeAndLevel(code, CommonConstants.AmapAreaCodeConstant.Level.PROVINCE);
        return areaCodeEntity == null ? new AmapAreaCodeEntity() : areaCodeEntity;
    }

    @Override
    public List<AmapAreaCodeEntity> findProvinceByCodes(Collection<String> codes) {
        List<AmapAreaCodeEntity> areaCodeEntitys = amapAreaCodeRepository.findAllByProvinceCodeInAndLevelOrderByProvinceCodeAsc(codes, CommonConstants.AmapAreaCodeConstant.Level.PROVINCE);
        return CollectionUtils.isEmpty(areaCodeEntitys) ? Collections.EMPTY_LIST : areaCodeEntitys;
    }

    @Override
    public Map<String, AmapAreaCodeEntity> findProvinceMapByCodes(Collection<String> codes) {
        List<AmapAreaCodeEntity> provinceByCodes = this.findProvinceByCodes(codes);
        return this.getAmapAreaCodeMap(provinceByCodes);
    }

    @Override
    public List<AmapAreaCodeEntity> findProvinceList() {
        List<AmapAreaCodeEntity> areaCodeEntitys = amapAreaCodeRepository.findAllByLevelOrderByProvinceCodeAsc(CommonConstants.AmapAreaCodeConstant.Level.PROVINCE);
        return CollectionUtils.isEmpty(areaCodeEntitys) ? Collections.EMPTY_LIST : areaCodeEntitys;
    }

    @Override
    public Map<String, AmapAreaCodeEntity> findProvinceMap() {
        List<AmapAreaCodeEntity> provinceByCodes = this.findProvinceList();
        return this.getAmapAreaCodeMap(provinceByCodes);
    }


    @Override
    public AmapAreaCodeEntity findCityByName(String cityName) {
        AmapAreaCodeEntity areaCodeEntity = amapAreaCodeRepository.findFirstByCityNameAndLevel(cityName, CommonConstants.AmapAreaCodeConstant.Level.CITY);
        return areaCodeEntity == null ? new AmapAreaCodeEntity() : areaCodeEntity;
    }

    @Override
    public AmapAreaCodeEntity findCityByCode(String code) {
        AmapAreaCodeEntity areaCodeEntity = amapAreaCodeRepository.findFirstByCityCodeAndLevel(code, CommonConstants.AmapAreaCodeConstant.Level.CITY);
        return areaCodeEntity == null ? new AmapAreaCodeEntity() : areaCodeEntity;
    }

    @Override
    public List<AmapAreaCodeEntity> findCityListByCodes(Collection<String> codes) {
        List<AmapAreaCodeEntity> areaCodeEntitys = amapAreaCodeRepository.findAllByCityCodeInAndLevelOrderByProvinceCodeAscCityCodeAsc(codes, CommonConstants.AmapAreaCodeConstant.Level.CITY);
        return CollectionUtils.isEmpty(areaCodeEntitys) ? Collections.EMPTY_LIST : areaCodeEntitys;
    }

    @Override
    public Map<String, AmapAreaCodeEntity> findCityMapByCodes(Collection<String> codes) {
        List<AmapAreaCodeEntity> provinceByCodes = this.findCityListByCodes(codes);
        return this.getAmapAreaCodeMapForCity(provinceByCodes);
    }


    @Override
    public List<AmapAreaCodeEntity> findCityListByProvinceCode(String code) {
        List<AmapAreaCodeEntity> areaCodeEntitys = amapAreaCodeRepository.findAllByProvinceCodeAndLevelOrderByProvinceCodeAscCityCodeAsc(code, CommonConstants.AmapAreaCodeConstant.Level.CITY);
        return CollectionUtils.isEmpty(areaCodeEntitys) ? Collections.EMPTY_LIST : areaCodeEntitys;
    }

    @Override
    public Map<String, AmapAreaCodeEntity> findCityMapByProvinceCode(String code) {
        List<AmapAreaCodeEntity> provinceByCodes = this.findCityListByProvinceCode(code);
        return this.getAmapAreaCodeMapForCity(provinceByCodes);
    }

    @Override
    public AmapAreaCodeEntity findDistrictByName(String districtName) {
        AmapAreaCodeEntity areaCodeEntity = amapAreaCodeRepository.findFirstByDistrictNameAndLevel(districtName, CommonConstants.AmapAreaCodeConstant.Level.DISTRICT);
        return areaCodeEntity == null ? new AmapAreaCodeEntity() : areaCodeEntity;
    }

    @Override
    public AmapAreaCodeEntity findDistrictByCode(String code) {
        AmapAreaCodeEntity areaCodeEntity = amapAreaCodeRepository.findFirstByDistrictCodeAndLevel(code, CommonConstants.AmapAreaCodeConstant.Level.DISTRICT);
        return areaCodeEntity == null ? new AmapAreaCodeEntity() : areaCodeEntity;
    }

    @Override
    public List<AmapAreaCodeEntity> findDistrictListByCodes(Collection<String> codes) {
        List<AmapAreaCodeEntity> areaCodeEntitys = amapAreaCodeRepository.findAllByDistrictCodeInAndLevelOrderByProvinceCodeAscCityCodeAscDistrictCodeAsc(codes, CommonConstants.AmapAreaCodeConstant.Level.DISTRICT);

        return CollectionUtils.isEmpty(areaCodeEntitys) ? Collections.EMPTY_LIST : areaCodeEntitys;
    }

    @Override
    public Map<String, AmapAreaCodeEntity> findDistrictMapByCodes(Collection<String> codes) {
        List<AmapAreaCodeEntity> provinceByCodes = this.findDistrictListByCodes(codes);
        return this.getAmapAreaCodeMapForDistrict(provinceByCodes);
    }

    @Override
    public List<AmapAreaCodeEntity> findDistrictListByCityCode(String code) {
        List<AmapAreaCodeEntity> areaCodeEntitys = amapAreaCodeRepository.findAllByCityCodeAndLevelOrderByProvinceCodeAscCityCodeAscDistrictCodeAsc(code, CommonConstants.AmapAreaCodeConstant.Level.DISTRICT);

        return CollectionUtils.isEmpty(areaCodeEntitys) ? Collections.EMPTY_LIST : areaCodeEntitys;
    }

    @Override
    public Map<String, AmapAreaCodeEntity> findDistrictMapByCityCode(String code) {
        List<AmapAreaCodeEntity> provinceByCodes = this.findDistrictListByCityCode(code);
        return this.getAmapAreaCodeMapForDistrict(provinceByCodes);
    }
}
