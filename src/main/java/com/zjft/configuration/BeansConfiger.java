package com.zjft.configuration;

import com.zjft.beans.StateMachineBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Descrition TODO
 * @Author shanwang
 * @Date 2019/11/21 19:22
 * @Version 1.0.0
 */
@Configuration
public class BeansConfiger {

    @Value("${zjft.ZJMS.stateXmlFile.path}")
    private String stateJsonFile;

    @Value("${zjft.ZJMS.JScriptFile.path}")
    private String javascriptFile;

    @Bean
    public StateMachineBean stateMachineBean() {
        String stateJsonContent = FileUtil.readFile(stateJsonFile);
        String javascriptContent = FileUtil.readFile(javascriptFile);
        return new StateMachineBean(stateJsonContent,javascriptContent);
    }
}
