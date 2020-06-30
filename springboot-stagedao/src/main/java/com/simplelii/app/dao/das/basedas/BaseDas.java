package com.simplelii.app.dao.das.basedas;

import com.simplelii.app.common.dao.base.BaseEo;
import com.simplelii.app.common.dao.base.BaseMapper;
import com.simplelii.app.common.utils.CamelToUnderlineUtil;
import com.simplelii.app.common.utils.SpringBeanUtil;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liXin
 * @description dao
 * @date 2020/6/10
 */
public abstract class BaseDas<T extends BaseEo> {

    private static Map<String, BaseMapper> mappers = new ConcurrentHashMap<>();

    public <MAPPER extends BaseMapper> BaseMapper getMapper(String dasPrefix) {
        String tempMapperName = dasPrefix + "Mapper";
        String mapperName = CamelToUnderlineUtil.camelStanderFormatFirstChar(tempMapperName);
        return SpringBeanUtil.<MAPPER>getBean(mapperName);
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


    public int count() {
        String name = getClass().getName();
        String[] split = name.split("\\.");
        String dasName = split[split.length - 1];
        String dasPrefix = dasName.substring(0, dasName.length() - 3);
        BaseMapper mapper = this.getMapper(dasPrefix);
        String clazzPath = ((Proxy) mapper).getClass().getInterfaces()[0].getName();

        mapper.getClass().getInterfaces();
        String eoName = dasPrefix + "Eo";

//        mapper.count()
        return 1;
    }


}
