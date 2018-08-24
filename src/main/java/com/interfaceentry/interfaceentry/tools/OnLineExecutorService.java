package com.interfaceentry.interfaceentry.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import com.interfaceentry.interfaceentry.entity.ParamsEntity;
import com.interfaceentry.interfaceentry.service.RequestParamsService;
import com.interfaceentry.interfaceentry.service.impl.MerchantServiceImpl;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 在线轮询
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-19 21:37
 **/
@Log
@Component
public class OnLineExecutorService {
    private static Map<String, Timer> timerContainer = new HashMap<>();

    @Resource
    private RequestParamsService requestParamsService;

    @Resource
    private MerchantServiceImpl merchantService;

    private static OnLineExecutorService instance;

    @PostConstruct
    public void init() {
        instance = this;
        instance.requestParamsService = this.requestParamsService;
        instance.merchantService = this.merchantService;
    }

    public static OnLineExecutorService getInstance() {
        if (instance == null) {
            instance = new OnLineExecutorService();
        }
        return instance;
    }

    /**
     * 定时执行请求结果任务
     *
     * @param requestSeqId
     * @param merchantNo
     */
    public void taskForGetResult(String requestSeqId, String merchantNo) {
        Timer timer = new Timer();
        timerContainer.put(requestSeqId, timer);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String result = OnLineExecutorService.getInstance().getSubmitResultOnece(requestSeqId, merchantNo);
                log.info(result);
                Boolean cancel = false;
                //判断结果
                if (!StringUtils.isEmpty(result)) {
                    JSONObject answerModel = JSON.parseObject(result);
                    JSONObject signStatusResult = answerModel.getJSONObject("result");
                    String signStatus = signStatusResult.get("signStatus").toString();
                    if (signStatus.equals("FAILURE")) {
                        OnLineExecutorService.getInstance().signStatusCall(requestSeqId, Long.parseLong(merchantNo), signStatus, signStatusResult.get("signStatusDesc").toString(), Boolean.FALSE);
                        cancel = true;
                    } else if ("SUCCESS".equals(signStatus)) {
                        OnLineExecutorService.getInstance().signStatusCall(requestSeqId, Long.parseLong(merchantNo), signStatus, signStatusResult.get("signStatusDesc").toString(), Boolean.TRUE);
                        cancel = true;
                    } else if ("SIGNING".equals(signStatus) || "UNKONW".equals(signStatus)) {
                        cancel = false;
                    }
                    //本次结果为未知或者签约中 继续下次轮询
                    if (cancel) {
                        OnLineExecutorService.getInstance().stopTackByRequestSeqId(requestSeqId);
                    }

                } else {
                    log.info("未获取到结果，等下次轮询");
                }
            }
        };

        /**
         * 延迟5分钟启动
         */
        long delay = 0;

        /**
         *每五分钟执行一次
         */
        long intevalPeriod = 5 * 60 * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    /**
     * 请求申请结果
     *
     * @param requestSeqId
     * @param merchantNo
     * @return
     */
    private String getSubmitResultOnece(String requestSeqId, String merchantNo) {
        ParamsEntity paramsForm = requestParamsService.getParamsInstance();
        String mac = paramsForm.getRequestSystem() + requestSeqId + merchantNo + paramsForm.getKey();
        mac = AppMD5Util.MD5(mac).toUpperCase();
        ParamsEntity paramsInstance = ParamsEntity.builder().requestSeqId(requestSeqId).merchantNo(merchantNo).requestSystem(paramsForm.getRequestSystem()).mac(mac).build();
        String ret = OkHttpUtil.post(paramsForm.getRequestUri() + "/mapi/o2o/personalstore/platformMerchantService/querySignAggregateRusult", JSON.toJSONString(paramsInstance), OkHttpUtil.APPLICATION_JSON);
        return ret;
    }

    /**
     * 结果回写
     *
     * @param requestSeqId 请求唯一标识
     * @param merchanrId   商户唯一标识
     * @return
     */
    private void signStatusCall(String requestSeqId, Long merchanrId, String signStatus, String signStatusDesc, Boolean success) {
        ParamsEntity paramsEntity = requestParamsService.getParamsByRequestSeqId(requestSeqId);
        paramsEntity.setSignStatus(signStatus);
        paramsEntity.setSignStatusDesc(signStatusDesc);
        requestParamsService.saveOrUpDate(paramsEntity);

        MerchantEntity merchantEntity = merchantService.getById(merchanrId);
        merchantEntity.setSignStatus(success);
        merchantEntity.setSubmissionStatus(success ? Constants.SubmitionStatus.YZFSH_PASS.name() : Constants.SubmitionStatus.YZFSH_REJ.name());
        merchantEntity.setUpdateAt(System.currentTimeMillis());
        merchantService.saveOrUpdate(merchantEntity);
    }

    public Boolean stopTackByRequestSeqId(String requestSeqId) {
        Timer timerForReqId = timerContainer.get(requestSeqId);
        if (null != timerForReqId) {
            timerForReqId.cancel();
            timerContainer.remove(requestSeqId);
        }
        return Boolean.TRUE;
    }

    public Boolean taksIsEmpty(String requestSeqId) {
        return null != OnLineExecutorService.timerContainer.get(requestSeqId);
    }
}
