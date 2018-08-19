package com.interfaceentry.interfaceentry.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.interfaceentry.interfaceentry.dao.MerchantRespository;
import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import com.interfaceentry.interfaceentry.tools.Constants;
import com.interfaceentry.interfaceentry.tools.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 商户service实现
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-18 19:08
 **/
@Service
public class MerchantServiceImpl implements MerchantService {
    private Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
    //申请平台商商户进件
    private static String INTO_URL = "{API_Url}/mapi/o2o/personalstore/platformMerchantService/applyMerchantEntry";


    @Autowired
    private MerchantRespository merchantRespository;

    @Override
    public MerchantEntity saveOrUpdate(MerchantEntity merchantEntity) {
        //若是修改 增加修改时间
        if (null != merchantEntity.getId()) {
            merchantEntity.setUpdateAt(System.currentTimeMillis());
        } else {
            //若是首次添加 则加入创建时间  并且把状态设置为待平台审核
            merchantEntity.setCreateAt(System.currentTimeMillis());
            merchantEntity.setSubmissionAt(System.currentTimeMillis());
            merchantEntity.setSubmissionStatus(Constants.SubmitionStatus.JBGSH_ING.name());
        }
        return merchantRespository.save(merchantEntity);
    }

    @Override
    public List<MerchantEntity> getMerchantByStatus(String status) {
        return merchantRespository.findBySubmissionStatusEquals(status);
    }

    @Override
    public String updateSubmissionStatusById(Long id, String submissionStatus) {
        Optional<MerchantEntity> optional = merchantRespository.findById(id);
        if (!optional.isPresent()) {
            return Constants.ERROR;
        }
        MerchantEntity merchantEntity = optional.get();
        merchantEntity.setSubmissionStatus(submissionStatus);
        this.saveOrUpdate(merchantEntity);
        return Constants.SUCCESS;
    }

    @Override
    public void submiToYZHSH(Long id) {
        Optional<MerchantEntity> optional = merchantRespository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("非法商户ID");
        }

        MerchantEntity merchantEntity = optional.get();

        //todo : 完善提交到翼支付审核逻辑
        try {
            Boolean success = this.merchantInto(merchantEntity);
            if (success == null || !success) {
                throw new RuntimeException("商户进件请求失败");
            }
        } catch (Exception e) {
            logger.error("商户进件请求失败 merchantId:{}", merchantEntity.getId(), e);
        }

    }

    private Boolean merchantInto(MerchantEntity merchantEntity) {

        TreeMap<String, String> params = new TreeMap<>();
        String requestSystem;//请求系统  M 平台商在聚合平台申请的平台编码
        String requestSeqId; //请求流⽔水号  M 保证每次请求唯⼀一
        String platformMerchantNo;// 平台商商户号  M 平台商在商服开的商户号，作为代理理商与平台商下的商户进⾏行行绑定
        String merchantNo; //商户号  M 商户在平台商侧的商户号
        String merchantName = merchantEntity.getMerchantName();// 商户名称  M
        String merchantNameShort; //商户简称  M 接⼝口进件上传给微信⽀支付宝通道的商户简称
        String businessScope = merchantEntity.getBusinessScope();// 营业范围  M
        String businessTerm;//营业期限  M 格式：yyyy - MM - dd，营业期限为⻓长期时，填：2199 - 12 - 31
        String provinceCode;//省份编码  M 请⻅见《地区城市码.xlsx》
        String cityCode;// 城市编码  M 请⻅见《地区城市码.xlsx》
        String businessAddress;// 营业地址  M
        String mccCode;//⾏行行业分类编码  M 请⻅见《⾏行行业类型码表.xlsx》
        String contactPhone;// 联系⼈人⼿手机号  M
        String identityCardUserName;// 身份证⽤用户姓名  M
        String identityCardNo;//身份证件号  M
        String identityCardFrontPic;// 身份证正⾯面图⽚片  M
        String identityCardReversePic;//身份证反⾯面图⽚片  M
        String busiLicenseNo;//营业执照号  M
        String busiLicenseUserName;//营业执照⽤用户姓名  M
        String licenseType;//营业许可证类型  M businessLicense：营业执照，
        String licensePic;// 营业许可证图⽚片  M
        String storeInteriorPic;// 店铺内景照⽚片  M
        String storeSignBoardPic;// 店铺招牌照⽚片  M
        String settleBankName;// 结算银⾏行行名称  M 通过银⾏行行卡所办的地区城市码来查询银⾏行行⽀支⾏行行名称和联⾏行行号集合，供商户选择
        String settleBankNo;//结算银⾏行行编码  M 请填写空字符串串，例例如：””
        String settleBankcardNo;//结算银⾏行行卡号  M
        String settleBankcardUserName;// 结算银⾏行行卡⽤用户姓名 M
        String settleBankcardLineNumber; //银⾏行行卡联⾏行行号  M 通过银⾏行行卡所办的地区城市码来查询银⾏行行⽀支⾏行行名称和联⾏行行号集合，供商户选择
        String settleBankcardFinanceAreaCode;//银⾏行行卡结算地区码  M 银⾏行行卡财务结算地区码，通过接⼝口⼀一查询获得
        String settlePhoneNo;// 银⾏行行预留留⼿手机号  M
        String merchantTxnRate;// 商户签约交易易费率  M 单位：%
        String merchantTxnSettlePeriod;// 商户交易易结算周期  M 填“1”
        String agentMerchantCode;// 代理理商商户号  M 平台商在翼⽀支付代理理商平台开通的商户号
        String recommendNo;// 员⼯工账号  M 平台商在翼⽀支付代理理商平台为⾃自⼰己的员⼯工开通的员⼯工账号
        String mac; //mac检验码  M 字符串串拼接顺序：

        String integrateLicense;//三证合⼀一照;

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("requestSystem", requestSystem);
        paramsMap.put("requestSeqId", requestSeqId);
        paramsMap.put("platformMerchantNo", platformMerchantNo);
        paramsMap.put("merchantNo", merchantNo);
        paramsMap.put("merchantName", merchantName);
        paramsMap.put("merchantNameShort", merchantNameShort);
        paramsMap.put("businessScope", businessScope);
        paramsMap.put("businessTerm", businessTerm);
        paramsMap.put("provinceCode", provinceCode);
        paramsMap.put("cityCode", cityCode);
        paramsMap.put("businessAddress", businessAddress);
        paramsMap.put("mccCode", mccCode);
        paramsMap.put("contactPhone", contactPhone);
        paramsMap.put("identityCardUserName", identityCardUserName);
        paramsMap.put("identityCardNo", identityCardNo);
        paramsMap.put("identityCardFrontPic", identityCardFrontPic);
        paramsMap.put("identityCardReversePic", identityCardReversePic);
        paramsMap.put("busiLicenseNo", busiLicenseNo);
        paramsMap.put("busiLicenseUserName", busiLicenseUserName);
        paramsMap.put("licenseType", licenseType);
        paramsMap.put("licensePic", licensePic);
        paramsMap.put("storeInteriorPic", storeInteriorPic);
        paramsMap.put("storeSignBoardPic", storeSignBoardPic);
        paramsMap.put("settleBankName", settleBankName);
        paramsMap.put("settleBankNo", settleBankNo);
        paramsMap.put("settleBankcardNo", settleBankcardNo);
        paramsMap.put("settleBankcardUserName", settleBankcardUserName);
        paramsMap.put("settleBankcardLineNumber", settleBankcardLineNumber);
        paramsMap.put("settleBankcardFinanceAreaCode", settleBankcardFinanceAreaCode);
        paramsMap.put("settlePhoneNo", settlePhoneNo);
        paramsMap.put("merchantTxnRate", merchantTxnRate);
        paramsMap.put("merchantTxnSettlePeriod", merchantTxnSettlePeriod);
        paramsMap.put("agentMerchantCode", agentMerchantCode);
        paramsMap.put("recommendNo", recommendNo);
        paramsMap.put("mac", mac);

        paramsMap.put("integrateLicense", integrateLicense);

        String data = JSON.toJSONString(paramsMap);
        data = OkHttpUtil.post(MerchantServiceImpl.INTO_URL, data, OkHttpUtil.APPLICATION_JSON);
        JSONObject obj = JSONObject.parseObject(data);
        return (Boolean) obj.get("success");
    }


}
