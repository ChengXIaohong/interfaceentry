package com.interfaceentry.interfaceentry.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商户实体类
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-17 11:49
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "merchant")
@Entity
public class MerchantEntity extends BaseEntity {
    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 营业范围
     */
    private String businessScope;

    /**
     * 营业期限
     * 格式：yyyy-MM-dd，营业期限为⻓长期
     * 时，填：2199-12-31
     */
    private String businessTerm;

    /**
     * 省份编码 请见 .file/地区城市码.xlsx
     */
    private String provinceCode;

    /**
     * 城市编码 请见 .file/地区城市码.xlsx
     */
    private String cityCode;

    /**
     * 营业地址
     */
    private String businessAddress;

    /**
     * ⾏行行业分类编码 请见 .file/行业类型码表.xlsx
     */
    private String mccCode;

    /**
     * 联系人手机号
     */
    private String contactPhone;

    /**
     * 身份证用户姓名
     */
    private String identityCardUserName;

    /**
     * 身份证件号
     */
    private String identityCardNo;

    /**
     * 身份证正面照片
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String identityCardFrontPic;

    /**
     * 身份证反面面照片
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String identityCardReversePic;

    /**
     * 营业执照号
     */
    private String busiLicenseNo;

    /**
     * 营业执照⽤用户姓名
     */
    private String busiLicenseUserName;

    /**
     * 营业许可证类型
     * businessLicense：营业执照，
     * integrateLicense：三证合一照
     */
    private String licenseType;

    /**
     * 营业许可证图片
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String licensePic;

    /**
     * 店铺内景照片
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String storeInteriorPic;

    /**
     * 店内招牌照片
     */
    @Column(columnDefinition = "MEDIUMTEXT")
    private String storeSignBoardPic;

    /**
     * 结算银行名称
     * 通过银⾏行行卡所办的地区城市码来查询银⾏行行
     * ⽀支⾏行行名称和联⾏行行号集合，供商户选择
     */
    private String settleBankName;

    /**
     * 结算银行编码
     * 请填写空字符串串，例如：””
     */
    private String settleBankNo;

    /**
     * 结算银行卡号
     */
    private String settleBankcardNo;

    /**
     * 结算银行卡⽤用户姓名
     */
    private String settleBankcardUserName;


    /**
     * 银行卡联⾏号
     * 通过银行卡所办的地区城市码来查询银行
     * ⽀支行名称和联行号集合，供商户选择
     */
    private String settleBankcardLineNumb;

    /**
     * 银行卡结算地区码
     * 银行卡财务结算地区码，通过接口一查询获得
     */
    private String settleBankcardFinanceAreaCode;

    /**
     * 银行预留手机号
     */
    private String settlePhoneNo;

    /**
     * 商户签约交易易费率
     * 单位：%
     */
    private String merchantTxnRate;

    /**
     * 商户交易结算周期
     * 填“1”
     */
    private String merchantTxnSettlePeriod;

    /**
     * 提交审核时间
     */
    private Long submissionAt;

    /**
     * 审核通过时间
     */
    private Long submissionApplyAt;

    /**
     * 审核状态
     */
    private String submissionStatus;

    /**
     * 商户简称   接口进件上传给微信⽀支付宝通道的商户简称
     */
    private String merchantNameShort;

    /**
     * 签约状态   接口进件上传给微信⽀支付宝通道的商户简称
     */
    private Boolean signStatus;

}
