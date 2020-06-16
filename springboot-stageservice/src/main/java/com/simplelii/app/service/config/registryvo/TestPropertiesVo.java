package com.simplelii.app.service.config.registryvo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liXin
 * @description
 * @date 2020/6/16
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class TestPropertiesVo {
    private String test;


}
