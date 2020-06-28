package com.simplelii.app.common.dao.base;

import com.simplelii.app.common.dao.sql.BaseSqlTemplate;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

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
     * @param paramT
     * @return
     */
    @UpdateProvider(type = BaseSqlTemplate.class, method = "updateSelect")
    public abstract int updateSelect(T paramT);

    @UpdateProvider(type = BaseSqlTemplate.class, method = "updateSelectSqlCondition")
    public abstract int updateSelectBySqlCondition(T paramT);

//    /**
//     *  通过id删除数据
//     * @param paramClass
//     * @param paramLong
//     * @return
//     */
//    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteById")
//    public abstract int deleteById(Class<T> paramClass, Long paramLong);
//
//    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteLogicById")
//    public abstract int deleteLogicById(Class<T> paramClass, Long paramLong);
//
//    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteByBatchIds")
//    public abstract int deleteByBatchIds(Class<T> paramClass, Long[] paramArrayOfLong);
//
//    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteLogicByBatchIds")
//    public abstract int deleteLogicByBatchIds(Class<T> paramClass, Long[] paramArrayOfLong);
//
//    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteByEo")
//    public abstract int deleteByEo(T paramT);
//
//    @DeleteProvider(type = BaseSqlTemplate.class, method = "deleteLogicByEo")
//    public abstract int deleteLogicByEo(T paramT);
//
//    /**
//     *  查询
//     * @param paramClass
//     * @param paramLong
//     * @return
//     */
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryById")
//    public abstract T queryById(Class<T> paramClass, Long paramLong);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryByIdsDr")
//    public abstract List<T> queryByIdsDr(Class<T> paramClass, Long[] paramArrayOfLong, Boolean paramBoolean);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryColumnById")
//    public abstract T queryColumnById(Class<T> paramClass, Long paramLong, String... paramVarArgs);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryByEo")
//    public abstract T queryByEo(T paramT);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryIdBySqlFilter")
//    public abstract List<Long> queryIdBySqlFilter(T paramT);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryColumn")
//    public abstract T queryColumn(T paramT, String... paramVarArgs);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryList")
//    public abstract List<T> queryListByEo(T paramT);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryListColumn")
//    public abstract List<T> queryListColumn(T paramT, String... paramVarArgs);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "queryAll")
//    public abstract List<T> queryAll(Class<T> paramClass);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "count")
//    public abstract int count(Class<T> paramClass);
//
//    @SelectProvider(type = BaseSqlTemplate.class, method = "countCondition")
//    public abstract int countCondition(T paramT);


}
