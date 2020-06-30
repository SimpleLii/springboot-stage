package com.simplelii.app.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author SimpleLii
 * @description 对 org.springframework.beans.BeanUtils 进行一次封装，提供pageInfo对象，Collection 对象的复制
 * @date 14:05 2020/6/30
 */
public class ExtendBeanUtil {

    /**
     * 复制对象
     *
     * @param source           源
     * @param target           目标
     * @param ignoreProperties 忽略字段
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        if (ignoreProperties != null && ignoreProperties.length > 0) {
            BeanUtils.copyProperties(source, target, ignoreProperties);
        } else {
            BeanUtils.copyProperties(source, target);
        }
    }

    /**
     * 对集合的复制
     *
     * @param sources          源
     * @param targetClazz      目标字节码
     * @param ignoreProperties 忽略字段
     */
    @SuppressWarnings("all")
    public static void copyCollection(Object sources, Object targets, Class targetClazz, String... ignoreProperties) throws RuntimeException {
        if (sources instanceof Collection && targets instanceof Collection) {
            Collection sourcesCollection = (Collection) sources;
            Collection targetsCollection = (Collection) targets;
            Iterator iterator = sourcesCollection.iterator();
            if (ignoreProperties != null && ignoreProperties.length > 0) {
                while (iterator.hasNext()) {
                    Object source = iterator.next();
                    Object target = null;
                    try {
                        target = targetClazz.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BeanUtils.copyProperties(source, target, ignoreProperties);
                    targetsCollection.add(target);
                }
            } else {
                while (iterator.hasNext()) {
                    Object source = iterator.next();
                    Object target = null;
                    try {
                        target = targetClazz.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BeanUtils.copyProperties(source, target);
                    targetsCollection.add(target);
                }
            }
        }
    }
}
