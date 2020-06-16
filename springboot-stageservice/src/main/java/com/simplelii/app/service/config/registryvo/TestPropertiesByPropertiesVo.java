package com.simplelii.app.service.config.registryvo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author liXin
 * @description
 * @date 2020/6/16
 */
@Data
@Configuration
@PropertySource(value = "classpath:test.properties")
@ConfigurationProperties(prefix = "test")
public class TestPropertiesByPropertiesVo {
    private String properties;
}
