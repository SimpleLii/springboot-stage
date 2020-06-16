package com.simplelii.app.service.aop;

import com.simplelii.app.api.IUserApi;
import com.simplelii.app.api.query.IUserQueryApi;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liXin
 * @description 日志切面类
 * @date 2020/6/15
 */
@Aspect
public class LogAspect {

    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Pointcut("execution (public * com.simplelii.app.api.*.*(..))")
    public void apiLogAop() {

    }
}
