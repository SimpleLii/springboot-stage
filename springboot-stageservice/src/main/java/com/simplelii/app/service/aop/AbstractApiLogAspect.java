package com.simplelii.app.service.aop;


import com.alibaba.fastjson.JSON;
import com.simplelii.app.api.dto.RestResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liXin
 * @description ApiLog切面增强
 * @date 2020/6/17
 */
public abstract class AbstractApiLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AbstractApiLogAspect.class);
    private static final String LEVEL_EMP = "empty";
    private static final String LEVEL_ADJ = "adjust";
    private static final String LEVEL_FULL = "full";
    private static final int ADJ_LEN = 4096;


    protected String getLogRespLevel() {
        return LEVEL_ADJ;
    }

    protected int getAdjustLenth() {
        return ADJ_LEN;
    }

    /**
     * 抽象方法
     */
    public abstract void apiLogAop();

    @Around(value = "apiLogAop()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        DateTime startTime = new DateTime();
        DateTime endTime = null;
        Interval interval = null;
        Object response = null;
        try {
            logger.info("Call API {}.{} Begin () Request => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()));
            response = joinPoint.proceed();
        } catch (Exception e) {
            endTime = new DateTime();
            interval = new Interval(startTime, endTime);
            logger.error("Call API {}.{} End () Request => {}, RT:{} ms, Error => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()), interval.toDurationMillis(), e.getMessage());
            logger.error("", e);
            throw e;
        }
        endTime = new DateTime();
        interval = new Interval(startTime, endTime);
        if (logger.isDebugEnabled()) {
            logger.debug("Call API {}.{} End () Request => {}, RT:{} ms, Response => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()), interval.toDurationMillis(), argsToString(response));
        }
        try {
            if (LEVEL_FULL.equals(getLogRespLevel())) {
                logger.info("Call API {}.{} End () Request => {}, RT:{} ms, Response => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()), interval.toDurationMillis(), argsToString(response));
            } else if (LEVEL_ADJ.equals(getLogRespLevel())) {
                RestResponse<?> restResponse = (RestResponse<?>) response;
                logger.info("Call API {}.{} End () Request => {}, RT:{} ms, Response => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()), interval.toDurationMillis(), argsToAdjustString(restResponse));
            } else {
                RestResponse<?> restResponse = (RestResponse) response;
                logger.info("Call API {}.{} End () Request => {}, RT:{} ms, Response => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()), interval.toDurationMillis(), argsToString(restResponse));
            }
        } catch (Exception e) {
            logger.error("Call API {}.{} End () Request => {}, RT:{} ms, Response => {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), argsToString(joinPoint.getArgs()), interval.toDurationMillis(), argsToString(response));
        }
        return response;
    }

    private String argsToString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            logger.error("JSON 转换出错，响应信息不明", e);
        }
        return "JSON 转换出错，响应信息不明";
    }

    private String argsToString(RestResponse<?> restResponse) {
        return "{\"reultCode\":" + restResponse.getResultCode() + ",\"resultMsg\":\"" + restResponse.getResultMsg() + "\"}";
    }

    private String argsToAdjustString(RestResponse<?> restResponse) {
        String data = JSON.toJSONString(restResponse.getData());
        return "{\"reultCode\":" + restResponse.getResultCode() + ",\"resultMsg\":\"" + restResponse.getResultMsg() + "\", \"data\":\"" + data.substring(0, data.length() > getAdjustLenth() ? getAdjustLenth() : data.length()) + "\"}";
    }
}
