package com.interfaceentry.interfaceentry.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mcc_code_detail")
@Data
public class MccCodeDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long mccId;
    private String codeMcc;//mcc编码
    private String quickRiskLv;// 快捷风险等级
    private String description;  //描述
    private String codeStair;//一级编码
    private String codeSecond; //二级编码
    private String codeAmcc;//AMCC编码
    private String codeSmcc;//SMCC编码
    private String kind;//类型
    private String standardDefinition;//国标&银联MCC定义

    private Boolean state;
    private Long createAt;
    private Long updateAt;

}
