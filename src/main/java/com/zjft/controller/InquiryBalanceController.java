package com.zjft.controller;

import com.zjft.beans.StateMachineBean;
import com.zjft.service.ServiceEntryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Descrition TODO
 * @Author shanwang
 * @Date 2019/11/21 18:42
 * @Version 1.0.0
 */

@RestController
public class InquiryBalanceController {
    private static final Logger logger = LoggerFactory.getLogger(InquiryBalanceController.class);

    @Autowired
    StateMachineBean stateMachineBean;

    @PostMapping(value = "/test")
    public Map<String,Object> handler(@RequestBody String params) {
        logger.info("InquiryBalanceController::handler requestParam param=[{}]",params);
        ServiceEntryHandler serviceHandler = new ServiceEntryHandler(stateMachineBean);
        return serviceHandler.setRequestParam(params).exec();
    }
}
