package com.interfaceentry.interfaceentry.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常Controller
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-07-03 12:07
 **/

public class FinalExceptionHandler{

    /**
     * 全局异常处理
     * @param t
     * @param request
     * @param response
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public String exceptionHandler(Throwable t, HttpServletRequest request, HttpServletResponse response) {
        Map<String ,Object> statusMap = new HashMap<>();
        statusMap.put("status", "error");
        Map<String , String> errorMessageMap = new HashMap<>();
        errorMessageMap.put("msg" , t.getMessage());
        errorMessageMap.put("date" , new Date().toString());
        errorMessageMap.put("path" , request.getRequestURI());
        statusMap.put("errorInfo",errorMessageMap);
        return JSON.toJSONString(statusMap);
    }
}
