package com.simplelii.app.service.aop;


import com.simplelii.app.common.aspects.AbstractApiLogAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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
