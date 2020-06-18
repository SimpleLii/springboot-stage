package com.simplelii.app.service.config;


import com.simplelii.app.service.aop.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liXin
 * @description
 * @date 2020/6/10
 */
@Configuration
public class LogAopConfig {

    @Bean
    public LogAspect createAspect(){
        return new LogAspect();
    }

}
