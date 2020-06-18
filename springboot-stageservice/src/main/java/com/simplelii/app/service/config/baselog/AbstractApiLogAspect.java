package com.simplelii.app.service.config.baselog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liXin
 * @description ApiLog切面增强
 * @date 2020/6/17
 */
public abstract class AbstractApiLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AbstractApiLogAspect.class);

    /**
     *  抽象方法
     */
    public abstract void apiLogAop();

    @Around(value = "apiLogAop()")
    public void around(ProceedingJoinPoint joinPoint){
        // api调用入口统一日志打印
        logger.info("");
    }

}
