package com.simplelii.app.common.utils;

import com.simplelii.app.common.dao.base.BaseMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author SimpleLii
 * @description
 * @date 10:24 2020/6/30
 */
public class MapperInvocationHandler implements InvocationHandler {
    private Object target;
    private Object backUpTarget;

    public MapperInvocationHandler() {
    }

    public MapperInvocationHandler(BaseMapper target, BaseMapper backUpTarget) {
        this.target = target;
        this.backUpTarget = backUpTarget;
    }

    public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
        Method[] methods = this.target.getClass().getDeclaredMethods();
        for (Method me : methods) {
            if (me.getName().equals(method.getName())) {
                return method.invoke(this.target, args);
            }
        }
        return method.invoke(this.backUpTarget, args);
    }
}