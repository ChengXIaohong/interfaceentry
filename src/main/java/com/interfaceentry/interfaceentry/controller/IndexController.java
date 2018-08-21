package com.interfaceentry.interfaceentry.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页controller
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-07-02 11:02
 **/
@RestController()
public class IndexController {

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> ret = new HashMap<>(4);
        ret.put("title", "index");
        ret.put("status", "success");
        ret.put("state", Boolean.TRUE);
        ret.put("content", "index is building");
        return ret;
    }

}
