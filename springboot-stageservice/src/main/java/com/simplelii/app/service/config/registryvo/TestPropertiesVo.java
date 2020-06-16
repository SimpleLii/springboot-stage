package com.simplelii.app.service.config.registryvo;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liXin
 * @description
 * @date 2020/6/16
 */
@Component
@ConfigurationProperties(prefix = "redis")
@Data
public class TestPropertiesVo implements InitializingBean {
    private String test;

    @Resource
    private TestPropertiesByPropertiesVo vo;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("2121");

    }
}
