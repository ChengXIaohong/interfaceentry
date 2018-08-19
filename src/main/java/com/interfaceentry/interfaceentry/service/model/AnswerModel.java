package com.interfaceentry.interfaceentry.service.model;

import lombok.Data;

/**
 * 应答对象
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 10:05
 **/
@Data
public class AnswerModel {

    /**
     * 接口调用结果
     */
    private Boolean success;

    /**
     * 结果码
     */
    private String errorCode;

    /**
     * 结果描述
     */
    private String errorMsg;

    /**
     * 业务执行结果
     */
    private Object result;
}
