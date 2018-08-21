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
    public void taskForGetResult(String requestSeqId, String merchantNo, Long merchantId) {
        Timer timer = new Timer();
        timerContainer.put(requestSeqId, timer);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                String result = OnLineExecutorService.getInstance().getSubmitResultOnece(requestSeqId, merchantNo);

                //判断结果
                if (!StringUtils.isEmpty(result)) {
                    JSONObject answerModel = JSON.parseObject(result);
                    JSONObject signStatusResult = answerModel.getJSONObject("result");
                    if (signStatusResult.get("signStatus").equals("FAILURE")) {
                        OnLineExecutorService.getInstance().signStatusCall(requestSeqId, merchantId, signStatusResult.get("signStatus").toString(), signStatusResult.get("signStatusDesc").toString(),Boolean.FALSE);
                    } else {
                        OnLineExecutorService.getInstance().signStatusCall(requestSeqId, merchantId, signStatusResult.get("signStatus").toString(), signStatusResult.get("signStatusDesc").toString(),Boolean.TRUE);

                    }
                    timerContainer.get(requestSeqId).cancel();
                    timerContainer.remove(requestSeqId);
                } else {
                    log.info("未获取到结果，等下次轮询");
                }
            }
        };

        long delay = 0;
        long intevalPeriod = /*5 * 60 * */1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    //fixme:参数不完整

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
    private void signStatusCall(String requestSeqId, Long merchanrId, String signStatus, String signStatusDesc,Boolean success) {
        ParamsEntity paramsEntity = requestParamsService.getParamsByRequestSeqId(requestSeqId);
        paramsEntity.setSignStatus(signStatus);
        paramsEntity.setSignStatusDesc(signStatusDesc);
        requestParamsService.saveOrUpDate(paramsEntity);

        MerchantEntity merchantEntity = merchantService.getById(merchanrId);
        merchantEntity.setSignStatus(success);
        merchantEntity.setUpdateAt(System.currentTimeMillis());
        merchantService.saveOrUpdate(merchantEntity);

    }

}
