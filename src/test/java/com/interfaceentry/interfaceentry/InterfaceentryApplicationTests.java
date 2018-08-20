package com.interfaceentry.interfaceentry;

import com.interfaceentry.interfaceentry.service.model.DynamicParams;
import com.interfaceentry.interfaceentry.tools.OkHttpUtil;
import com.interfaceentry.interfaceentry.tools.OnLineExecutorService;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class InterfaceentryApplicationTests {

    @Test
    public void contextLoads() {
       String data = OkHttpUtil.get("https://www.baidu.com");
       log.info(data);
    }

    @Test
    public void test() {
        Runnable runnable = new Runnable() {
            public void run() {
                System.out.println("Hello !!");
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
    }

    @Test
    public void timerTest() {
       // OnLineExecutorService.getSubmitResult("1","2");
    }
    @Test
    public void ymlParmas(){
        log.info(d.getAgentMerchantCode());
    }
    @Autowired
    private DynamicParams d;
}
