package com.interfaceentry.interfaceentry.entity;

import com.interfaceentry.interfaceentry.service.model.DynamicParams;
import com.interfaceentry.interfaceentry.tools.Constants;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * 请求携带的数据
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-16 17:38
 **/

@Data
@Entity
@Table(name = "request_record")
@Builder
@AllArgsConstructor
public class ParamsEntity extends BaseEntity implements Serializable {
    //=============请求基础参数 参数 START
    /**
     * 请求系统:平台商在聚合平台申请的平台编码
     */
    private String requestSystem;

    /**
     * 交易key
     */
    @Column(name = "key_", nullable = false)
    private String key;

    /**
     * 请求流水号：保证每次请求唯⼀
     */
    private String requestSeqId;

    /**
     * mac检验码
     * 字符串串拼接顺序：
     * requestSystem+requestSeqId+b
     * ankName+接⼝口key
     */
    private String mac;
    //=============请求基础参数 参数 END

    //=============根据银行名称查询银行信息 参数 START
    /**
     * 银行名称
     * 例例如：“浦东”
     */
    private String bankName;

    /**
     * 银行编码
     * 例例如：中国农业银⾏行行 ABC、中国
     * 银⾏行行 BOC
     */
    private String bankCode;

    //=============根据银行名称查询银行信息 参数 END

    /**
     * 查询申请结果时用
     */
    private String merchantNo;

    //=============申请平台商商户进件 START
    /**
     * 平台商商户号
     * 平台商在商服开的商户号，作为代理理商与
     * 平台商下的商户进⾏行行绑定
     */
    private String platformMerchantNo;

    /**
     * 商户号
     * 商户在平台商侧的商户号
     */
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "merchant_id", nullable = false)
    private MerchantEntity merchantEntity;


    /**
     * 代理商户号
     * 平台商在翼支付代理商平台开通的商户号
     */
    private String agentMerchantCode;

    /**
     * 员工账号
     */
    private String recommendNo;

    /**
     *  mac;
     * 字符串拼接顺序：
     * requestSystem+requestSeqId+merchant
     * No+merchantName+merchantNameShor
     * t+businessScope+businessTerm+provinc
     * eCode+cityCode+businessAddress+mcc
     * Code+contactPhone+identityCardUserN
     * ame+identityCardNo+busiLicenseNo+bu
     * siLicenseUserName+licenseType+settle
     * BankName+settleBankNo+settleBankcar
     * dNo+settleBankcardUserName+settleBa
     * nkcardLineNumber+settleBankcardFinan
     * ceAreaCode+merchantTxnRate+mercha
     * ntTxnSettlePeriod+接口key
     */
    //=============申请平台商商户进件 END
    /**
     * 接口返回数据：应答数据
     */
    private String responseResult;


    private String requestUri;


}
