package com.interfaceentry.interfaceentry.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 测试与正式参数注入对象
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-20 14:56
 **/
@ConfigurationProperties(prefix="kdypay.config")
@Data
@NoArgsConstructor
@Component
public class DynamicParams {
    private String requestSystem;
    private String key;
    private String agentMerchantCode;
    private String recommendNo;
    private String macc;
}
