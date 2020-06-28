package com.simplelii.app.common.dao.base;

import com.simplelii.app.common.utils.CamelToUnderlineUtil;
import com.simplelii.app.common.utils.SpringBeanUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liXin
 * @description dao
 * @date 2020/6/10
 */
public abstract class BaseDas<T extends BaseEo> {


    private static Map<String, BaseMapper> mappers = new ConcurrentHashMap<>();

    public <MAPPER extends BaseMapper> BaseMapper getMapper() {
        String name = getClass().getName();
        String[] split = name.split("\\.");
        String dasName = split[split.length - 1];
        String tempMapperName = dasName.substring(0, dasName.length() - 3) + "Mapper";
        String mapperName = CamelToUnderlineUtil.camelStanderFormatFirstChar(tempMapperName);
        MAPPER baseMapper = SpringBeanUtil.getBean(mapperName);
//        mappers.putIfAbsent(mapperName, baseMapper);
        return baseMapper;
    }

    /**
     * .获取所有的mappers
     *
     * @return
     */
    public Map<String, BaseMapper> getMappers() {
        if (mappers.isEmpty()) {
            mappers = (Map<String, BaseMapper>) SpringBeanUtil.getBeansOfType(BaseMapper.class);
        }
        return mappers;
    }
}
