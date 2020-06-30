package com.simplelii.app.common.dao.base;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.simplelii.app.common.utils.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liXin
 * // TODO 未实现缓存，与eo mapper 继承问题未解决
 * @description dao
 * @date 2020/6/10
 */
public abstract class BaseDas<T extends BaseEo> {


    private Logger logger = LoggerFactory.getLogger(BaseDas.class);

    private static final String FIELD_VALUE_SEPARATOR = "#";

    private static final String FIELDS_SEPARATOR = ";";

    private static String NULL_EXAMPLE_STR = "{}";

    private static final Map<String, BaseMapper> proxyMapperMap = Maps.newHashMap();

    private static final Object mutex = new Object();

    private static Map<String, BaseMapper> mappers = new ConcurrentHashMap<>();


    /**
     * .获取所有的mappers
     *
     * @return
     */
    @SuppressWarnings("all")
    @Bean(name = "proxyMapperMap")
    public Map<String, BaseMapper> getMappersFromIOC() {
        if (mappers.isEmpty()) {
            mappers = (Map<String, BaseMapper>) SpringBeanUtil.getBeansOfType(BaseMapper.class);
            proxyMapperMap.putAll(mappers);
        }
        return mappers;
    }

    // 规范对外提供获取所有mappers方法
    protected abstract Map<String, BaseMapper> getMappers();

    protected abstract String getVersion();

    /**
     * 获取mapper
     *
     * @param mapperName
     * @return
     */
    public BaseMapper getMapper(String mapperName) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(mapperName)) {
            return proxyMapperMap.get(mapperName);
        }
        return null;
    }

    public BaseMapper getMapper() {
        String eoClassName = getEntityClass().getSimpleName();
        String mapperName = eoClassName.substring(0, 1).toLowerCase() + eoClassName.substring(1, eoClassName.length() - 2) + "Mapper";
        BaseMapper mapper = getMappers().get(mapperName);
        if (null == mapper) {
            mapperName = eoClassName.substring(0, eoClassName.length() - 2) + "Mapper";
            mapper = getMappers().get(mapperName);
        }
        // 解决继承问题 todo
//        String oMapperName = mapperName.replace("ExtMapper", "Mapper");
//        BaseMapper backupMapper = (BaseMapper)getMappers().get(oMapperName);
//        String key = mapperName + oMapperName;
//        if (null == proxyMap.get(key)) {
//            synchronized (mutex)
//            {
//                if (null == proxyMap.get(key))
//                {
//                    MapperInvocationHandler proxy = new MapperInvocationHandler(mapper, backupMapper);
//                    proxyMap.put(key, (BaseMapper)Proxy.newProxyInstance(backupMapper.getClass().getClassLoader(), backupMapper.getClass().getInterfaces(), proxy));
//                }
//            }
//        }
        return mapper;
    }

    /**
     * 获取到Eo-Class
     *
     * @return entityClazz
     */
    private Class<T> getEntityClass() {
        // 获取父类泛型对象，取第一个元素
        Class entityClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        if (entityClass != null) {
//            // 解决eo继承问题
//            String eoName = entityClass.getPackage().getName() + "." + StringUtils.delete(entityClass.getSimpleName(), "Eo") + "ExtEo";
//            try {
//                Class extClass = Class.forName(eoName);
//                entityClass = extClass;
//            } catch (ClassNotFoundException localClassNotFoundException) {
//
//            }
//        }
        return entityClass;
    }

    public String getCachePre() {
        return getActualArgumentClassName(0) + getVersion();
    }

    private String getActualArgumentClassName(int i) {
        Class entityClass = (Class) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[i];
        return entityClass.getSimpleName();
    }


    public List<T> queryByEo(T record, String... tableColumnName) {
        try {
            return getMapper().queryByEo(record, tableColumnName);
        } catch (Exception e) {
            this.logger.error("查询异常 method:{}, e:{}", "queryByEo", e);
        }
        return null;
    }

    /**
     * 通过条件查询列表 TODO 缓存
     *
     * @param record
     * @param cacheable
     * @return
     */
    public List<T> queryByEo(T record, boolean cacheable) {
        if (cacheable) {
        }
        return getMapper().queryByEo(record);
    }

//    private List<T> getCache(String key) {
//        if (null == key) {
//            return null;
//        }
//        List<T> result = getCacheService().getList(key, (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
//        return result;
//    }


    /**
     * 主键查询 TODO 缓存
     *
     * @param key
     * @return
     */
    public T queryByPrimaryKey(Long key) {
        T record = (T) getMapper().queryById(getEntityClass(), key);
        return record;
    }

    /**
     * @param ids             PrimaryKey
     * @param containsDr      为true时 where条件增加 dr=0
     * @param tableColumnName 需要返回的字段
     * @return
     */
    public List<T> queryByIdsDr(List<Long> ids, Boolean containsDr, String... tableColumnName) {
        return getMapper().queryByIdsDr(getEntityClass(), ids, containsDr, tableColumnName);
    }


    public T queryOneByEo(T record, String... tableColumnName) {
        return (T) getMapper().queryOneByEo(record, tableColumnName);
    }

    /**
     * 统计
     *
     * @return
     */
    public int count() {
        try {
            return getMapper().count(getEntityClass());
        } catch (Exception e) {
            this.logger.error("查询异常 method:{}, e:{}", "count", e);
        }
        return -1;
    }

    public int countByCondition(T paramT) {
        return getMapper().countByCondition(paramT);
    }

    public int insert(T record) {
        this.logger.info("insert record {}", JSON.toJSONString(record));
        return getMapper().insert(record);
    }

    public int insertBatch(List<T> records) {
        if (records.isEmpty()) {
            return 0;
        }
        return getMapper().insertBatch(records);
    }

    /**
     * 批量插入后，获取对应的ids
     *
     * @param eoList
     * @return
     */
    public List<Long> getIds(List<T> eoList) {
        List<Long> ids = new ArrayList<>();
        for (T t : eoList) {
            ids.add(t.getId());
        }
        return ids;
    }

    /**
     * 通过指定的id找到记录跟新
     * record 对象中id requiredNotNull
     *
     * @param record
     */
    public void updateSelect(T record) {
        getMapper().updateSelect(record);
    }

    public void updateSelectBySqlCondition(T record) {
        getMapper().updateSelectBySqlCondition(record);
    }


    public void deletePhysicsById(Long id) {
        getMapper().deletePhysicsById(getEntityClass(), id);
    }

    public void deleteLogicById(Long id) {
        getMapper().deleteLogicById(getEntityClass(), id);
    }

    public void deleteLogicByBatchIds(List<Long> ids) {
        getMapper().deleteLogicByBatchIds(getEntityClass(), ids);
    }

    public void deletePhysicsByBatchIds(Class<T> paramClass, List<Long> ids) {
        getMapper().deletePhysicsByBatchIds(getEntityClass(), ids);
    }

    /**
     * 通过条件 逻辑删除
     *
     * @param example
     */
    public void deleteLogicByEo(T example) {
        deleteByExample(example, Boolean.TRUE);
    }

    public void deletePhysicsByEo(T example) {
        deleteByExample(example, Boolean.FALSE);
    }

    private int deleteByExample(T example, Boolean isLogicDelete) {
        String json = JSON.toJSONString(example);
        if (NULL_EXAMPLE_STR.equals(json)) {
            return 0;
        }
        int effects = 0;
        if (isLogicDelete) {
            effects = getMapper().deleteLogicByEo(example);
        } else {
            effects = getMapper().deletePhysicsByEo(example);
        }
        return effects;
    }

    public List<T> queryAll(String... tableColumnName) {
        return getMapper().queryAll(getEntityClass(), tableColumnName);
    }


}
