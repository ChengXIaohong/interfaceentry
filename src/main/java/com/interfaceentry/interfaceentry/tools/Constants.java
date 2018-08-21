package com.interfaceentry.interfaceentry.tools;

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

    /**
     * 步骤⼆：平台商获取商户的结算银⾏名称，调
     * ⽤《根据银⾏名称查询银⾏信息》接⼜，商户
     * 在银⾏列表中选择结算银⾏卡对应的开户⾏信
     * 息。
     */
    public static final String URI_BANK_INFO = "mapi/o2o/personalstore/platformMerchantService/getSettleBankInfos";

    /**
     * 平台商获取商户所有信息后，调⽤
     * 《申请平台商商户进件》接⼜进⾏商户数据录
     * ⼊与初审
     */
    public static final String URI_SUBMIT_MERCHANT = "mapi/o2o/personalstore/platformMerchantService/applyMerchantEntry";

    /**
     * 步骤四：待商户资质信息和基本信息上传成功
     * 后，翼⽀付运营⼈员会针对商户进⾏审核，在
     * 此审核期间，平台商每隔5分钟调⽤《聚合⽀付
     * 签约结果查询》接⼜进⾏商户⼊驻签约结果的
     * 查询操作，查
     */
    public static final String URI_SUBMIT_RESULT = "mapi/o2o/personalstore/platformMerchantService/querySignAggregateRusult";

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
