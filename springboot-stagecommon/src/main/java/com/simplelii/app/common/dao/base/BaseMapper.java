package com.simplelii.app.common.dao.base;

import com.simplelii.app.common.dao.sql.BaseSqlTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author liXin
 * @description mybatis 基础mapper，封装基本的CRUD
 * @date 2020/6/10
 */
public interface BaseMapper<T extends BaseEo> {

    /**
     * 插入单条数据
     *
     * @param paramT 对象
     * @return 受影响行数
     */
    @InsertProvider(type = BaseSqlTemplate.class, method = "insert")
    public abstract int insert(T paramT);

    /**
     * 批量插入数据
     *
     * @param paramList
     * @return
     */
    @InsertProvider(type = BaseSqlTemplate.class, method = "insertBatch")
    public abstract int insertBatch(@Param("objList") List<T> paramList);

    /**
     * 只更新传入参数的数据，需要为null的字段请使用对应的枚举，更新数据
     * 对象需要传入id
     *
     * @param paramT
     * @return
     */
    @UpdateProvider(type = BaseSqlTemplate.class, method = "updateSelect")
    public abstract int updateSelect(T paramT);

    @UpdateProvider(type = BaseSqlTemplate.class, method = "updateSelectSqlCondition")
    public abstract int updateSelectBySqlCondition(T paramT);

    /**
     * 通过id删除数据（物理删除）
     *
     * @param paramClass
     * @param id
     * @return
     */
    @DeleteProvider(type = BaseSqlTemplate.class, method = "deletePhysicsById")
    public abstract int deletePhysicsById(Class<T> paramClass, Long id);

    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteLogicById")
    public abstract int deleteLogicById(Class<T> paramClass, Long id);

    @DeleteProvider(type = BaseSqlTemplate.class, method = "deletePhysicsByBatchIds")
    public abstract int deletePhysicsByBatchIds(Class<T> paramClass, List<Long> ids);

    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteLogicByBatchIds")
    public abstract int deleteLogicByBatchIds(Class<T> paramClass, List<Long> ids);

    @DeleteProvider(type = BaseSqlTemplate.class, method = "deletePhysicsByEo")
    public abstract int deletePhysicsByEo(T paramT);

    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteLogicByEo")
    public abstract int deleteLogicByEo(T paramT);

    /**
     * 查询
     *
     * @param paramClass
     * @param paramLong
     * @return
     */
    @SelectProvider(type = BaseSqlTemplate.class, method = "queryById")
    public abstract T queryById(Class<T> paramClass, Long paramLong, String... tableColumnName);

    @SelectProvider(type = BaseSqlTemplate.class, method = "queryByIdsDr")
    public abstract List<T> queryByIdsDr(Class<T> paramClass, List<Long> ids, Boolean containsDr, String... tableColumnName);

    @SelectProvider(type = BaseSqlTemplate.class, method = "queryOneByEo")
    public abstract T queryOneByEo(T paramT, String... tableColumnName);

    @SelectProvider(type = BaseSqlTemplate.class, method = "queryAll")
    public abstract List<T> queryAll(Class<T> paramClass, String... tableColumnName);

    @SelectProvider(type = BaseSqlTemplate.class, method = "queryByEo")
    public abstract List<T> queryByEo(@Param("arg0") T paramT, String... tableColumnName);

    @SelectProvider(type = BaseSqlTemplate.class, method = "count")
    public abstract int count(Class<T> eoClass);

    @SelectProvider(type = BaseSqlTemplate.class, method = "countByCondition")
    public abstract int countByCondition(T paramT);


}
