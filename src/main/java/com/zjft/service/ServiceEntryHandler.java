package com.zjft.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjft.beans.StateMachineBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

/**
 * @Descrition TODO
 * @Author shanwang
 * @Date 2019/11/21 18:52
 * @Version 1.0.0
 */

public class ServiceEntryHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServiceEntryHandler.class);

    private ScriptEngineManager manager = new ScriptEngineManager();
    private ScriptEngine engine;

    private Map<String,Object> sharedVars = new HashMap<>();

    private StateMachineBean stateMachineBean;

    private JSONObject stateMigration;

    private String JSscriptContent;

    public ServiceEntryHandler(StateMachineBean stateMachineBean) {
        this.stateMachineBean = stateMachineBean;
    }

    /**
    *功能描述
    * @author shanwang
    * @date 2019/11/21
    * @param
     * @param requestParam
    * @return com.zjft.service.ServiceEntryHandler
    */
    public ServiceEntryHandler setRequestParam(String requestParam) {
        JSONObject jsonRequestParam = JSON.parseObject(requestParam);
        for (Map.Entry<String, Object> entry : jsonRequestParam.entrySet()) {
            sharedVars.put(entry.getKey(),entry.getValue());
        }
        logger.info("ServiceEntryHandler::setRequestParam requestParam param=[{}]",requestParam);
        return this;
    }

    /**
    *功能描述
    * @author shanwang
    * @date 2019/11/22
    * @param
     * @param
    * @return void
    */
    private void setStateAndJavascriptContent() {
        this.stateMigration = JSON.parseObject(stateMachineBean.getStateJsonContent());
        this.JSscriptContent = stateMachineBean.getJavascriptContent();
    }

    /**
    *功能描述
    * @author shanwang
    * @date 2019/11/22
    * @param
     * @param
    * @return void
    */
    private void loadScriptToEngine() {
        this.engine = this.manager.getEngineByName("nashorn");
        try {
            this.engine.eval(JSscriptContent);
        } catch (ScriptException exception) {
            logger.error("ServiceEntryHandler::loadScriptToEngine loadScriptToEngine catch exception",exception);
        }

    }

    /**
    *功能描述
    * @author shanwang
    * @date 2019/11/22
    * @param
     * @param
    * @return java.lang.String
    */
    private Map<String,Object> engineHandler() {
        String startState = stateMigration.getString("start");
        JSONObject firstState = stateMigration.getJSONObject(startState);

        String currentFunctionName = firstState.getString("function");
        String currentJsonParams = null;
        JSONArray currenttransitionJsonArr = firstState.getJSONArray("transitions");

        Map<String,Object> resqonseResult = new HashMap<>();

        while(true) {
            currentJsonParams = JSON.toJSONString(sharedVars);
            logger.info("ServiceEntryHandler::engineHandler exec currentFunctionName={},params={}",currentFunctionName,currentJsonParams);
            String result = null;
            if (engine instanceof Invocable) {
                try {
                    Invocable in = (Invocable) engine;
                    result = (String)in.invokeFunction(currentFunctionName,currentJsonParams);
                } catch (ScriptException scriptException) {
                    logger.error("ServiceEntryHandler::engineHandler exec iFunction name={},exceptionType=ScriptException err",currentFunctionName);
                    logger.error("ServiceEntryHandler::engineHandler catch exception",scriptException);
                } catch (NoSuchMethodException noMethodException) {
                    logger.error("ServiceEntryHandler::engineHandler exec iFunction name={},exceptionType=NoSuchMethodException err",currentFunctionName);
                    logger.error("ServiceEntryHandler::engineHandler  catch exception",noMethodException);
                }
            }
            logger.info("ServiceEntryHandler::engineHandler exec complete return result={}",result);
            JSONObject jsonCurrentResult = JSON.parseObject(result);
            String resultCode = jsonCurrentResult.getString("resultCode");

            // 遍历JSONArray
            String targetState = null;
            for (Iterator<Object> iterator = currenttransitionJsonArr.iterator(); iterator.hasNext(); ) {
                JSONObject next = (JSONObject) iterator.next();
                String tempNode = next.getString("result");
                if(resultCode.equals(tempNode)) {
                    targetState = next.getString("targetState");
                }
            }
            JSONObject tempShareVars = jsonCurrentResult.getJSONObject("shareParams");
            for (Map.Entry<String, Object> entry : tempShareVars.entrySet()) {
                sharedVars.put(entry.getKey(),entry.getValue());
            }

            if(!"End".equals(targetState)) {
                JSONObject nextState = stateMigration.getJSONObject(targetState);
                currentFunctionName = nextState.getString("function");
                currenttransitionJsonArr = nextState.getJSONArray("transitions");

            }else {
                JSONObject resqonseJsonResult = jsonCurrentResult.getJSONObject("resqonse");
                if(null != resqonseJsonResult) {
                    for (Map.Entry<String, Object> entry : resqonseJsonResult.entrySet()) {
                        resqonseResult.put(entry.getKey(),entry.getValue());
                    }
                }
                logger.info("ServiceEntryHandler::engineHandler return resqonse={}",resqonseResult);

                break;
            }
        }
        return resqonseResult;
    }

    /**
    *功能描述
    * @author shanwang
    * @date 2019/11/21
    * @param
     * @param
    * @return java.lang.String
    */
    public Map<String,Object> exec() {
        setStateAndJavascriptContent();
        loadScriptToEngine();
        Map<String,Object> resqonseParams = engineHandler();
        return resqonseParams;
    }


}
