package com.interfaceentry.interfaceentry.service.model;

import lombok.Data;

/**
 * 进件申请响应结果
 * 应答结果示例：
 * {"errorCode":"000000","errorMsg":"成功","result":true,"success":true}
 * {“errorCode":"000048","errorMsg":"平台商商户申请数据已存在，请勿重复提交","result":null,"success":false}
 *
 * @author wangrx
 * @create 2018-08-21 10:05
 **/
@Data
public class IntoResponseResult {

    private Boolean success;

    private String errorCode;
    private String errorMsg;
    private String result;

}
