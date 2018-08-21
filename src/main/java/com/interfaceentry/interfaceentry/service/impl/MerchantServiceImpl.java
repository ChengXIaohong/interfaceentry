package com.interfaceentry.interfaceentry.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.interfaceentry.interfaceentry.dao.MerchantRespository;
import com.interfaceentry.interfaceentry.dao.RequestParamsRespository;
import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.entity.ParamsEntity;
import com.interfaceentry.interfaceentry.service.MerchantService;
import com.interfaceentry.interfaceentry.service.RequestParamsService;
import com.interfaceentry.interfaceentry.service.model.IntoResponseResult;
import com.interfaceentry.interfaceentry.service.model.SettleBankInfo;
import com.interfaceentry.interfaceentry.tools.AppMD5Util;
import com.interfaceentry.interfaceentry.tools.Constants;
import com.interfaceentry.interfaceentry.tools.OkHttpUtil;
import com.interfaceentry.interfaceentry.tools.OnLineExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.security.provider.MD5;

import javax.persistence.Column;
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
    private static String INTO_URL = "/mapi/o2o/personalstore/platformMerchantService/applyMerchantEntry";
    //申请平台商商户进件
    private static String GET_BANK_INFOS = "/mapi/o2o/personalstore/platformMerchantService/getSettleBankInfos";


    @Autowired
    private MerchantRespository merchantRespository;
    @Autowired
    private RequestParamsRespository requestParamsRespository;
    @Autowired
    private RequestParamsService requestParamsService;

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

        ParamsEntity requestParamsEntity = requestParamsService.getParamsInstance();
        if (requestParamsEntity == null) {
            throw new RuntimeException("非法商户");
        }

        //商户进件申请
        try {
            IntoResponseResult intoResponseResult = this.merchantInto(requestParamsEntity, merchantEntity);
            Boolean success = intoResponseResult.getSuccess();
            if (success == null) {
                throw new RuntimeException("商户进件请求success 未成功");
            }
            if (!success) {
                String errorMsg = intoResponseResult.getErrorMsg();
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            logger.error("商户进件请求失败 merchantId:{}, errorMsg:{}", merchantEntity.getId(), e.getMessage(), e);
            throw e;
        }
        //签约
        try {
            OnLineExecutorService.taskForGetResult(requestParamsEntity.getRequestSeqId(), String.valueOf(merchantEntity.getId()));
        } catch (Exception e) {
            logger.error("商户进件签约失败 merchantId:{},requestSeqId:{}", merchantEntity.getId(), requestParamsEntity.getRequestSeqId(), e);
        }

    }

    @Override
    public List<SettleBankInfo> getSettleBankInfos(String bankName, String bankCode) {
        ParamsEntity requestParamsEntity = requestParamsService.getParamsInstance();
        String requestSystem = requestParamsEntity.getRequestSystem();//请求系统  M 平台商在聚合平台申请的平台编码
        String requestSeqId = requestParamsEntity.getRequestSeqId();//请求流⽔水号  M 保证每次请求唯⼀
        String mac = requestSystem + requestSeqId + bankName + requestParamsEntity.getKey();// mac检验码  M 字符串串拼接顺序：requestSystem + requestSeqId + bankName + 接⼝key
        mac = AppMD5Util.MD5(mac);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("requestSystem", requestSystem);
        paramsMap.put("requestSeqId", requestSeqId);
        paramsMap.put("bankName", bankName);//银行名称  M 例例如：“浦东”
        paramsMap.put("bankCode", bankCode);// 银行编码  M 例例如：中国农业银行 ABC、中国银行 BOC
        paramsMap.put("mac", mac);
        try {
            String data = JSON.toJSONString(paramsMap);
            data = OkHttpUtil.post(requestParamsEntity.getRequestUri() + MerchantServiceImpl.GET_BANK_INFOS, data, OkHttpUtil.APPLICATION_JSON);
            if (StringUtils.isEmpty(data)) {
                return Collections.EMPTY_LIST;
            }
            JSONObject obj = JSONObject.parseObject(data);
            Boolean success = (Boolean) obj.get("success");
            if (success == null || !success) {
                return Collections.EMPTY_LIST;
            }
            JSONArray result = obj.getJSONArray("result");
            List<SettleBankInfo> settleBankInfos = JSONArray.parseArray(result.toJSONString(), SettleBankInfo.class);
            return settleBankInfos;
        } catch (Exception e) {
            logger.error("银行查询错误 paramsMap:{}", paramsMap.toString(), e);
            return Collections.EMPTY_LIST;
        }

    }

    private IntoResponseResult merchantInto(ParamsEntity requestParamsEntity, MerchantEntity merchantEntity) {
        IntoResponseResult intoResponseResult = new IntoResponseResult();
        String merchantName = merchantEntity.getMerchantName();// 商户名称  M
        String businessScope = merchantEntity.getBusinessScope();// 营业范围  M
        String businessTerm = merchantEntity.getBusinessTerm();//营业期限  M 格式：yyyy - MM - dd，营业期限为⻓长期时，填：2199 - 12 - 31
        String provinceCode = merchantEntity.getProvinceCode();//省份编码  M 请⻅见《地区城市码.xlsx》
        String cityCode = merchantEntity.getCityCode();// 城市编码  M 请⻅见《地区城市码.xlsx》
        String businessAddress = merchantEntity.getBusinessAddress();// 营业地址  M
        String mccCode = merchantEntity.getMccCode();//行业分类编码  M 请⻅见《行业类型码表.xlsx》
        String contactPhone = merchantEntity.getContactPhone();// 联系⼈人⼿手机号  M
        String identityCardUserName = merchantEntity.getIdentityCardUserName();// 身份证⽤用户姓名  M
        String identityCardNo = merchantEntity.getIdentityCardNo();//身份证件号  M
        String identityCardFrontPic = merchantEntity.getIdentityCardFrontPic();// 身份证正⾯面图⽚片  M
        String identityCardReversePic = merchantEntity.getIdentityCardReversePic();//身份证反⾯面图⽚片  M
        String busiLicenseNo = merchantEntity.getBusiLicenseNo();//营业执照号  M
        String busiLicenseUserName = merchantEntity.getBusiLicenseUserName();//营业执照⽤用户姓名  M
        String licenseType = merchantEntity.getLicenseType();//营业许可证类型  M businessLicense：营业执照，
        String licensePic = merchantEntity.getLicensePic();// 营业许可证图⽚片  M
        String storeInteriorPic = merchantEntity.getStoreInteriorPic();// 店铺内景照⽚  M
        String storeSignBoardPic = merchantEntity.getStoreSignBoardPic();// 店铺招牌照片  M
        String settleBankName = merchantEntity.getSettleBankName();// 结算银行名称  M 通过银行卡所办的地区城市码来查询银⾏行行⽀支⾏行行名称和联⾏行行号集合，供商户选择
        String settleBankNo = merchantEntity.getSettleBankNo();//结算银行编码  M 请填写空字符串串，例例如：””
        String settleBankcardNo = merchantEntity.getSettleBankcardNo();//结算银⾏行行卡号  M
        String settleBankcardUserName = merchantEntity.getSettleBankcardUserName();// 结算银行卡⽤用户姓名 M
        String settleBankcardLineNumber = merchantEntity.getSettleBankcardLineNumb(); //银行卡联行号  M 通过银行卡所办的地区城市码来查询银⾏行行⽀支⾏行行名称和联⾏行行号集合，供商户选择
        String settleBankcardFinanceAreaCode = merchantEntity.getSettleBankcardFinanceAreaCode();//银行卡结算地区码  M 银⾏行行卡财务结算地区码，通过接口⼀一查询获得
        String settlePhoneNo = merchantEntity.getSettlePhoneNo();// 银行预留留⼿手机号  M
        String merchantTxnRate = merchantEntity.getMerchantTxnRate();// 商户签约交易易费率  M 单位：%
        String merchantTxnSettlePeriod = merchantEntity.getMerchantTxnSettlePeriod();// 商户交易易结算周期  M 填“1”
        String integrateLicense = merchantEntity.getLicenseType();//三证合⼀一照;

        String requestSystem = requestParamsEntity.getRequestSystem();//请求系统  M 平台商在聚合平台申请的平台编码
        String requestSeqId = requestParamsEntity.getRequestSeqId(); //请求流⽔水号  M 保证每次请求唯⼀一
        String platformMerchantNo = requestParamsEntity.getPlatformMerchantNo();// 平台商商户号  M 平台商在商服开的商户号，作为代理理商与平台商下的商户进⾏行行绑定
        String agentMerchantCode = requestParamsEntity.getAgentMerchantCode();// 代理理商商户号  M 平台商在翼⽀支付代理理商平台开通的商户号
        String recommendNo = requestParamsEntity.getRecommendNo();// 员⼯工账号  M 平台商在翼⽀支付代理理商平台为⾃自⼰己的员⼯工开通的员⼯工账号

        String merchantNameShort = merchantEntity.getMerchantNameShort(); //商户简称  M 接口进件上传给微信⽀支付宝通道的商户简称
        String merchantNo = String.valueOf(merchantEntity.getId()); //商户号  M 商户在平台商侧的商户号

        /*
        mac检验码  M  字符串串拼接顺序：
            requestSystem+requestSeqId+merchant
            No+merchantName+merchantNameShor
            t+businessScope+businessTerm+provinc
            eCode+cityCode+businessAddress+mcc
            Code+contactPhone+identityCardUserN
            ame+identityCardNo+busiLicenseNo+bu
            siLicenseUserName+licenseType+settle
            BankName+settleBankNo+settleBankcar
            dNo+settleBankcardUserName+settleBa
            nkcardLineNumber+settleBankcardFinan
            ceAreaCode+merchantTxnRate+mercha
            ntTxnSettlePeriod+接口key
         */
        String mac = requestSystem
                + requestSeqId
                + merchantNo
                + merchantName
                + merchantNameShort
                + businessScope + businessTerm
                + provinceCode
                + cityCode
                + businessAddress
                + mccCode
                + contactPhone
                + identityCardUserName
                + identityCardNo + busiLicenseNo
                + busiLicenseUserName
                + licenseType
                + settleBankName
                + settleBankNo
                + settleBankcardNo
                + settleBankcardUserName
                + settleBankcardLineNumber
                + settleBankcardFinanceAreaCode
                + merchantTxnRate
                + merchantTxnSettlePeriod
                + requestParamsEntity.getKey();
        mac = AppMD5Util.MD5(mac);

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("merchantName", merchantName);
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

        paramsMap.put("merchantNameShort", merchantNameShort);
        paramsMap.put("agentMerchantCode", agentMerchantCode);
        paramsMap.put("recommendNo", recommendNo);
        paramsMap.put("mac", mac);
        paramsMap.put("requestSystem", requestSystem);
        paramsMap.put("requestSeqId", requestSeqId);
        paramsMap.put("platformMerchantNo", platformMerchantNo);
        paramsMap.put("merchantNo", merchantNo);
        paramsMap.put("integrateLicense", integrateLicense);

        String data = JSON.toJSONString(paramsMap);
        data = OkHttpUtil.post(requestParamsEntity.getRequestUri() + MerchantServiceImpl.INTO_URL, data, OkHttpUtil.APPLICATION_JSON);
        if (StringUtils.isEmpty(data)) {
            return intoResponseResult;
        }
        intoResponseResult = JSONObject.parseObject(data, IntoResponseResult.class);
        //保存请求数据
        //补充数据
        requestParamsEntity.setCreateAt(System.currentTimeMillis());
        requestParamsEntity.setUpdateAt(System.currentTimeMillis());
//        requestParamsEntity.setCreateBy();
//        requestParamsEntity.setUpdateBy();
// requestParamsEntity.setBankCode();

        requestParamsEntity.setBankName(merchantEntity.getSettleBankName());


        requestParamsEntity.setMerchantId(merchantEntity.getId());
        requestParamsEntity.setResponseResult(data);


        requestParamsRespository.save(requestParamsEntity);


        return intoResponseResult;
    }


}
