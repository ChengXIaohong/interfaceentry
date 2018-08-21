package com.interfaceentry.interfaceentry.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mcc_code")
@Data
public class MccCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;//分类名称
    private Long pId;
    private Boolean state;
    private Long createAt;
    private Long updateAt;

}
