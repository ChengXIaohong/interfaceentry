package com.interfaceentry.interfaceentry.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.interfaceentry.interfaceentry.entity.ParamsEntity;
import lombok.extern.java.Log;
import org.springframework.util.StringUtils;

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
public class OnLineExecutorService {
    private static Map<String, Timer> timerContainer = new HashMap<>();

    /**
     * 定时执行请求结果任务
     *
     * @param requestSeqId
     * @param merchantNo
     */
    public static void taskForGetResult(String requestSeqId, String merchantNo) {

        Timer timer = new Timer();
        timerContainer.put(requestSeqId, timer);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String result = OnLineExecutorService.getSubmitResultOnece(requestSeqId, merchantNo);

                //判断结果
                if (!StringUtils.isEmpty(result)) {
                    JSONObject answerModel = JSON.parseObject(result);

                    if (answerModel.get("errorMsg").equals("成功")) {
                        //todo : 此处回写成功数据
                        System.out.println(123);
                    } else {
                        System.out.println(456);
                        //todo : 此处回写失败数据
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
    private static String getSubmitResultOnece(String requestSeqId, String merchantNo) {

        ParamsEntity paramsEntity = ParamsEntity.builder().build();


        //return OkHttpUtil.post("{API_Url}/mapi/o2o/personalstore/platformMerchantService/querySignAggregateRusult", jsonStr, OkHttpUtil.APPLICATION_JSON);
        return "{\"errorCode\":\"000000\",\"errorMsg\":\"成功\",\"result\":\n{\"bestpayMctNo\":null,\"signStatus\":\"SIGNING\",\"signStatusDesc\":\"签约中\"},\"success\":true}";
    }
}
