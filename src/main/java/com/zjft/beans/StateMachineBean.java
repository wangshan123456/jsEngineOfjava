package com.zjft.beans;

/**
 * @Descrition TODO
 * @Author shanwang
 * @Date 2019/11/21 19:29
 * @Version 1.0.0
 */

public class StateMachineBean {
    private String stateJsonContent;
    private String javascriptContent;

    public StateMachineBean(String stateJsonContent, String javascriptContent) {
        this.stateJsonContent = stateJsonContent;
        this.javascriptContent = javascriptContent;
    }

    public String getStateJsonContent() {
        return stateJsonContent;
    }

    public void setStateJsonContent(String stateJsonContent) {
        this.stateJsonContent = stateJsonContent;
    }

    public String getJavascriptContent() {
        return javascriptContent;
    }

    public void setJavascriptContent(String javascriptContent) {
        this.javascriptContent = javascriptContent;
    }
}
