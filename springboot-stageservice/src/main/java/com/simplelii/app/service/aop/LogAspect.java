package com.simplelii.app.service.aop;


import com.simplelii.app.service.config.baselog.AbstractApiLogAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liXin
 * @description 日志切面类
 * @date 2020/6/15
 */
@Aspect
public class LogAspect extends AbstractApiLogAspect {

    @Override
    @Pointcut("execution (public * com.simplelii.app.service.rest.*.*(..))")
    public void apiLogAop() {
    }

}
