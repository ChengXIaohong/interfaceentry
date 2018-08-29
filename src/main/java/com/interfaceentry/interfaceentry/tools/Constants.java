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
    public static final String URI_SUBMIT_MERCHANT = "/mapi/o2o/personalstore/platformMerchantService/applyMerchantEntry";

    /**
     * 步骤四：待商户资质信息和基本信息上传成功
     * 后，翼⽀付运营⼈员会针对商户进⾏审核，在
     * 此审核期间，平台商每隔5分钟调⽤《聚合⽀付
     * 签约结果查询》接⼜进⾏商户⼊驻签约结果的
     * 查询操作，查
     */
    public static final String URI_SUBMIT_RESULT = "/mapi/o2o/personalstore/platformMerchantService/querySignAggregateRusult";

    /**
     * 针对平台商接⼝口进件业务场景，当商户基本信息审核失败后，平台商需重
     * 新收集商户基本信息，并在翼⽀支付侧进⾏行行更更新申请，重新审核
     */
    public static final String URI_UPDATE_MERCHANT_BASEINFO = "/mapi/o2o/personalstore/platformMerchantService/updateMerchantBaseInfo";

    /**
     * 针对平台商接⼝口进件业务场景，当商户资质信息审核失败后，平台商需重
     * 新收集商户资质信息，并在翼⽀支付侧进⾏行行更更新申请，重新审核
     */
    public static final String URI_UPDATE_BUSI_QUALIFICATIONINFO = "/mapi/o2o/personalstore/platformMerchantService/updateBusiQualificationInfo";

    /**
     * 资质修改用
     * IDCard-身份证正⾯照
     */
    public static final String UPDATE_IDCARD = "IDCard";

    /**
     * 资质修改用
     * IDCardBack-身份证反⾯照
     */
    public static final String UPDATE_IDCARDBACK = "IDCardBack";

    /**
     * 资质修改用
     * integrateLicense：三证合一照
     */
    public static final String UPDATE_INTEGRATELICENSE = "integrateLicense";


    /**
     * 资质修改用
     * storeInterior：店铺内景
     */
    public static final String UPDATE_STOREINTERIOR = "storeInterior";

    /**
     * 资质修改用
     * signBoard：店铺招牌照
     */
    public static final String UPDATE_SIGNBOARD = "signBoard";

    /**
     * 错误码字典表 成功
     */
    public static final String REQUEST_SUCCESS = "000000";

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
