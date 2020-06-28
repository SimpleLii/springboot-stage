package com.simplelii.app.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author SimpleLii
 * @description springBean 工具类
 * @date 11:13 2020/6/24
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringBeanUtil.class);

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("注入ApplicationContext到SpringUtil:{}", applicationContext);

        if (SpringBeanUtil.applicationContext != null) {
            logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}", SpringBeanUtil.applicationContext);
        }
        SpringBeanUtil.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * </p>
     *
     * @param name springbean 的id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * </p>
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        assertContextInjected();
        Map<String, ?> beans = applicationContext.getBeansOfType(clazz);

        T bean = null;
        if (beans != null) {
            bean = (T) beans.get(beans.keySet().iterator().next());
        }
        return bean;
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(name, clazz);
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得clazz类型bean的Map集合
     * </p>
     *
     * @param clazz
     * @return
     */
    public static Map<String, ?> getBeansOfType(Class<?> clazz) {
        assertContextInjected();
        Map<String, ?> beans = applicationContext.getBeansOfType(clazz);
        return beans;
    }


    /**
     * 实现DisposableBean接口,在Context关闭时清理静态变量.
     *
     * @throws Exception
     */
    public void destroy() throws Exception {
        SpringBeanUtil.clear();
    }

    /**
     * <p>
     * 清除SpringUtil中的ApplicationContext为Null.
     * </p>
     */
    public static void clear() {
        logger.debug("清除SpringUtil中的ApplicationContext:{}", applicationContext);
        applicationContext = null;
    }

    /**
     * <p>
     * 检查ApplicationContext不为空.为空抛出异常:IllegalStateException
     * </p>
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在spring.xml或注解中定义SpringUtil");
        }
    }

}
