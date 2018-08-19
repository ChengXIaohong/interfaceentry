package com.interfaceentry.interfaceentry.tools;

/**
 * 常量类
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-07-03 10:52
 **/
public class Constants {
    /**
     * 错误页面
     */
    public static final String ERROR_PAGE = "errorPage";

    /**
     * 成功页面
     */
    public static final String SUCCESS_PAGE = "success";

    /**
     * 页面名称
     */
    public static final String PAGE_TITLE = "title";

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
}
