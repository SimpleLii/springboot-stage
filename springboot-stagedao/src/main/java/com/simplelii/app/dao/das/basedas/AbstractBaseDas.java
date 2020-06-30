package com.simplelii.app.dao.das.basedas;

import com.simplelii.app.common.dao.base.BaseDas;
import com.simplelii.app.common.dao.base.BaseEo;
import com.simplelii.app.common.dao.base.BaseMapper;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author SimpleLii
 * @description 继承BaseDas, 向外统一提供获取mapper方法
 * @date 10:54 2020/6/30
 */
public abstract class AbstractBaseDas<T extends BaseEo> extends BaseDas<T> {

    private final static String __VERSION__ = "_0.0.1_";

    @Resource
    private Map<String, BaseMapper> proxyMapperMap;


    @Override
    protected Map<String, BaseMapper> getMappers() {
        return proxyMapperMap;
    }

    @Override
    protected String getVersion() {
        return __VERSION__;
    }

}
