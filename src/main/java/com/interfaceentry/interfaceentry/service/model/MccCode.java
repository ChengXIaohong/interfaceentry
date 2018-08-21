package com.interfaceentry.interfaceentry.service.model;

import lombok.Data;

import java.util.List;

@Data
public class MccCode {

    private Long id;
    private String name;//分类名称
    private Long pId;

    private String mccCode;
    private List<MccCode> children;
}
