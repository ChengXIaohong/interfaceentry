package com.interfaceentry.interfaceentry.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量类
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-07-03 10:52
 **/

public class Constants {

    /**
     * 页面注入主要错误信息key
     */
    public static final String EXCEPTION_MESSAGE_BIZ = "exceptionMessage";

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";

    public enum SubmitionStatus {
        JBGSH_ING(10, "加班狗审核中"),
        JBGSH_PASS(11, "加班狗-审核通过"),
        JBGSH_REJ(12, "加班狗-审核不通过"),
        YZFSH_ING(20, "翼支付-审核中"),
        YZFSH_PASS(21, "翼支付-审核通过"),
        YZFSH_REJ(21, "翼支付-审核不通过");
        private String statusName;
        private int statusCode;

        SubmitionStatus(int statusCode, String statusName) {
            this.statusCode = statusCode;
            this.statusName = statusName;
        }
    }

    public static void main(String[] args) {
        System.out.println("as");
    }
}
