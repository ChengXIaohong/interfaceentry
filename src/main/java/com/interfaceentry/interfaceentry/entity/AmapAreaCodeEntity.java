package com.interfaceentry.interfaceentry.entity;

import lombok.Data;

import javax.persistence.*;

/***
 *
 * @author wangrx
 * @date 2018-01-15
 */
@Entity
@Table(name = "amap_area_code")
@Data
public class AmapAreaCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //级别
    private String level;
    //省编码
    private String provinceCode;
    //省
    private String provinceName;
    //市编码
    private String cityCode;
    //市
    private String cityName;
    //县/区 编码
    private String districtCode;
    //县/区
    private String districtName;
    //区号
    private String areaCode;


}
