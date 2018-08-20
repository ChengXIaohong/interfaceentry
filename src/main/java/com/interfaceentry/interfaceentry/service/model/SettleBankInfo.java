package com.interfaceentry.interfaceentry.service.model;

import lombok.Data;

/**
 * 应答对象
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 10:05
 **/
@Data
public class SettleBankInfo {
    String bankName;// 银行支行名称
    String bankLineNumber;// 银⾏联⾏号
    String financeAreaCode;// 结算地区码
}
