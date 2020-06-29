package com.simplelii.app.common.dao.sql;

import com.google.common.base.Joiner;
import com.simplelii.app.common.constants.EoDefaultConstant;
import com.simplelii.app.common.dao.base.BaseEo;
import com.simplelii.app.common.utils.BaseEoUtil;
import com.simplelii.app.common.utils.IdUtil;
import com.simplelii.app.common.utils.SqlUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SimpleLii
 * @description
 * @date 11:18 2020/6/24
 */
public class BaseSqlTemplate<T extends BaseEo> {

    private static final Long TENANT_CODE = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BaseSqlTemplate.class);

    public String insert(T eoT) {
        SQL sql = new SQL();
        sql.INSERT_INTO(BaseEoUtil.tableName(eoT.getClass()));
        Long id = eoT.getId();
        if ((null == id || id.equals(0L))) {
            id = getId();
        }
        sql.VALUES(BaseEoUtil.returnInsertColumnsName(eoT), BaseEoUtil.returnInsertColumnsDef(eoT));
        eoT.setId(id);
        return sql.toString();
    }

    private Long getId() {
        Long workerId = BaseEoUtil.getWorkerId();
        return IdUtil.nextId(workerId, TENANT_CODE);
    }

    public String insertBatch(Map<String, List<T>> params) {
        List<T> objList = params.get("objList");
        if ((objList == null) || (objList.size() == 0)) {
            return "sql error";
        }
        SQL sql = new SQL();
        T obj = objList.get(0);
        sql.INSERT_INTO(BaseEoUtil.tableName(obj.getClass()));
        // sql占位符接收数组
        String[] values = new String[objList.size()];
        for (int i = 0; i < objList.size(); i++) {
            StringBuilder sb = new StringBuilder();
            MessageFormat mf = new MessageFormat(BaseEoUtil.returnInsertColumnsDefBatch(objList.get(i)));
            Long id = (objList.get(i)).getId();
            if ((null == (objList.get(i)).getId()) || ((objList.get(i)).getId().equals(0L))) {
                id = getId();
            }
            if (i > 0) {
                sb.append("(");
            }
            // 替换对应的角标
            sb.append(mf.format(new Object[]{String.valueOf(i)}));
            if ((i == objList.size() - 1) &&
                    (sb.charAt(sb.length() - 1) == ')')) {
                sb.deleteCharAt(sb.length() - 1);
            }
            values[i] = sb.toString();
            objList.get(i).setId(id);
        }
        sql.INTO_COLUMNS(BaseEoUtil.returnInsertColumnsNameBatch(obj.getClass()));
        sql.INTO_VALUES(values);
        return sql.toString();
    }

    /**
     * 更新指定字段的值,id毕传
     *
     * @param obj
     * @return
     */
    public String updateSelect(T obj) {
        return getUpdateSql(obj, true);
    }

    /**
     * 封装update_sql
     *
     * @param obj
     * @param flag true：只更新eo对象有值的字段
     * @return
     */
    private String getUpdateSql(T obj, boolean flag) {
        String returnUpdateSet = null;
        if (flag) {
            returnUpdateSet = BaseEoUtil.returnUpdateSetNotNull(obj);
        } else {
            returnUpdateSet = BaseEoUtil.returnUpdateSet(obj);
        }
        String idName = BaseEoUtil.idName(obj.getClass());
        SQL sql = new SQL();
        sql.UPDATE(BaseEoUtil.tableName(obj.getClass()));
        sql.SET(returnUpdateSet);
        sql.WHERE(idName + "= #{" + idName + "}");
        return sql.toString();
    }

    /**
     * 根据条件更新有值的字段（eo对象）
     * 该方法内部逻辑是直接拼接where sql，需要进行防注入判断（针对condition使用PreparedStatement不好处理，）
     *
     * @param obj
     * @return
     */
    public String updateSelectSqlCondition(T obj) {
        SQL sql = new SQL();
        sql.UPDATE(BaseEoUtil.tableName(obj.getClass()));
        sql.SET(BaseEoUtil.returnUpdateSetNotNull(obj));
        String where = BaseEoUtil.returnUpdateWhereColumnNames(obj);
        if (StringUtils.isNotBlank(where)) {
            sql.WHERE(where);
        } else {
            return null;
        }
        return sql.toString();
    }

    public String deletePhysicsByEo(T obj) {
        return deleteLogic(obj, Boolean.FALSE);
    }

    public String deleteLogicByEo(T obj) {
        return deleteLogic(obj, Boolean.TRUE);
    }

    private String deleteLogic(T obj, Boolean isLogicDel) {
        String where = getWhere(obj);
        if ("".equals(where)) {
            return null;
        }
        StringBuilder sqlStr = new StringBuilder();
        int dr = (isLogicDel == null || isLogicDel) ? 1 : 2;
        String tableName = BaseEoUtil.tableName(obj.getClass());
        SQL sql = new SQL();
        sql.UPDATE(tableName);
        sql.SET(getUpdateCreatePersonDr(dr, obj.getUpdatePerson()));
        sql.WHERE(where);
        sqlStr.append(sql.toString());
        if (isLogicDel != null && !isLogicDel) {
            sql = new SQL();
            sql.DELETE_FROM(tableName);
            sql.WHERE(where.replace("dr=0", "dr=" + dr));
            sqlStr.append(";").append(sql.toString()).append(";");
        }
        return sqlStr.toString();
    }

    public String deleteLogicById(Class<T> aClass, Long id) {
        return deleteLogicId(aClass, id, Boolean.TRUE);
    }

    /**
     * 通过id物理删除数据
     *
     * @param aClass
     * @param id
     * @return
     */
    public String deletePhysicsById(Class<T> aClass, Long id) {
        return deleteLogicId(aClass, id, Boolean.FALSE);
    }

    /**
     * 删除数据，如果是物理删除，先进行一次更新操作，删除语句按照更新后的数据拼接sqlwhere条件，防止误删
     *
     * @param aClass     指定eoClass
     * @param id
     * @param isLogicDel true ：逻辑删除
     * @return
     */
    private String deleteLogicId(Class<T> aClass, Long id, Boolean isLogicDel) {
        int dr = ((isLogicDel == null) || isLogicDel) ? 1 : 2;
        String tableName = BaseEoUtil.tableName(aClass);
        StringBuilder sqlStr = new StringBuilder();
        SQL sql = new SQL();
        sql.UPDATE(tableName);
        sql.SET(getUpdateCreatePersonDr(dr, null));
        sql.WHERE("id = " + id + " and dr = 0");
        sqlStr.append(sql.toString()).append(";");
        if (isLogicDel != null && !isLogicDel) {
            sql = new SQL();
            sql.DELETE_FROM(tableName);
            sql.WHERE("id = " + id + " and dr = 2");
            sqlStr.append(sql.toString()).append(";");
        }
        return sqlStr.toString();
    }

    /**
     * 批量删除 （物理）
     *
     * @param aClass
     * @param ids
     * @return
     */
    public String deletePhysicsByBatchIds(Class<T> aClass, List<Long> ids) {
        if (ids != null && ids.size() == 1) {
            return deleteLogicId(aClass, ids.get(0), Boolean.FALSE);
        }
        return deleteLogicBatchIds(aClass, ids, Boolean.FALSE);
    }

    public String deleteLogicByBatchIds(Class<T> aClass, List<Long> ids) {
        if (ids != null && ids.size() == 1) {
            return deleteLogicId(aClass, ids.get(0), Boolean.TRUE);
        }
        return deleteLogicBatchIds(aClass, ids, Boolean.TRUE);
    }

    private String deleteLogicBatchIds(Class<T> aClass, List<Long> ids, Boolean isLogicDel) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        String idStr = Joiner.on(",").join(ids);
        int dr = (isLogicDel == null || isLogicDel) ? 1 : 2;
        String tableName = BaseEoUtil.tableName(aClass);
        StringBuilder sqlStr = new StringBuilder();
        SQL sql = new SQL();
        sql.UPDATE(tableName);
        sql.SET(getUpdateCreatePersonDr(dr, null));
        sql.WHERE("id in (" + idStr + ") and dr = 0");
        sqlStr.append(sql.toString()).append(";");
        if (isLogicDel != null && !isLogicDel) {
            sql = new SQL();
            sql.DELETE_FROM(tableName);
            sql.WHERE("id in (" + idStr + ") and dr = 2");
            sqlStr.append(sql.toString()).append(";");
        }
        return sqlStr.toString();
    }

    /**
     * @param dr
     * @param updatePerson 目前是使用默认值，如果是通过登录人信息获取，请按需修改，建议使用ThreadLocal 传参
     * @return
     */
    private String getUpdateCreatePersonDr(int dr, String updatePerson) {
        StringBuilder sb = new StringBuilder();
        sb.append("dr=").append(dr).append(",update_time=now()");
        sb.append(",update_person='").append(EoDefaultConstant.UPDATE_PERSON).append("'");
        return sb.toString();
    }

    public String queryByEo(T obj, String... tableColumnName) {
        SQL sql = new SQL();
        sql.SELECT(getSelectColumnNames(obj, tableColumnName));
        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
        String where = getWhereFormArg0(obj);
        if (StringUtils.isNotBlank(where)) {
            sql.WHERE(where);
        } else {
            sql.WHERE("dr = 0");
        }
        String orderBy = BaseEoUtil.resultOrderBy(obj);
        if (StringUtils.isNotBlank(orderBy)) {
            sql.ORDER_BY(orderBy);
        }
        return sql.toString();
    }

    /**
     * 查询返回一条数据，按照id desc 排序
     *
     * @param obj
     * @param tableColumnName
     * @return
     */
    public String queryOneByEo(T obj, String... tableColumnName) {
        String tempSqlStr = this.queryByEo(obj, tableColumnName);
        return tempSqlStr + " LIMIT 1";
    }

    public String queryById(Class<T> aClass, Long id, String... tableColumnName) {
        return findColumnById(aClass, id, tableColumnName);
    }

    public String queryByIdsDr(Class<T> aClass, List<Long> ids, Boolean containsDr, String... tableColumnName) {
        return findColumnByIdsDr(aClass, ids, containsDr, tableColumnName);
    }

    /**
     * @param aClass
     * @param ids
     * @param containsDr      true: where条件增加 dr=0
     * @param tableColumnName 指定返回字段
     * @return
     */
    public String findColumnByIdsDr(Class<T> aClass, List<Long> ids, Boolean containsDr, String... tableColumnName) {
        String selectColumnName = null;
        selectColumnName = getResultColumnByAssign((Class<T>) aClass, tableColumnName);
        SQL sql = new SQL();
        sql.SELECT(selectColumnName);
        sql.FROM(BaseEoUtil.tableName(aClass));
        String idname = BaseEoUtil.idName(aClass);
        String where = null;
        if (ids.size() == 1) {
            where = idname + " = " + ids.get(0);
        } else {
            where = idname + " in (" + Joiner.on(",").join(ids) + ")";
        }
        if (containsDr) {
            where = where + " and dr=0";
        }
        sql.WHERE(where);
        return sql.toString();
    }

    private String getResultColumnByAssign(Class<T> aClass, String[] tableColumnName) {
        String selectColumnName = null;
        if ((tableColumnName != null) && (tableColumnName.length > 0)) {
            List<String> tableColumnNameList = Arrays.stream(tableColumnName).collect(Collectors.toList());
            selectColumnName = BaseEoUtil.returnSelectColumnsName(aClass, tableColumnNameList);
            if (SqlUtil.isSpiteSql(selectColumnName)) {
                logger.error("Malice SQL Columns : {}", selectColumnName);
            }
        } else {
            selectColumnName = BaseEoUtil.returnSelectColumnsName(aClass);
        }
        return selectColumnName;
    }

    private String findColumnById(Class<T> aClass, Long id, String... tableColumnName) {
        String selectColumnName = getResultColumnByAssign(aClass, tableColumnName);
        SQL sql = new SQL();
        sql.SELECT(selectColumnName);
        sql.FROM(BaseEoUtil.tableName(aClass));
        String idname = BaseEoUtil.idName(aClass);
        sql.WHERE(idname + " = " + id + " and dr=0");
        return sql.toString();
    }

    public String queryAll(Class<T> aClass, String... tableColumnName) {
        String selectColumnName = getResultColumnByAssign(aClass, tableColumnName);
        SQL sql = new SQL();
        sql.SELECT(selectColumnName);
        sql.FROM(BaseEoUtil.tableName(aClass));
        sql.WHERE("dr = 0");
        return sql.toString();
    }

    /**
     * count(*) mysql 进行了优化选择最短索引
     *
     * @param aClass
     * @return
     */
    public String count(Class<T> aClass) {
        SQL sql = new SQL();
        sql.SELECT("count(*)");
        sql.FROM(BaseEoUtil.tableName(aClass));
        sql.WHERE("dr = 0");
        return sql.toString();
    }

    public String countByCondition(T obj) {
        SQL sql = new SQL();
        sql.SELECT("count(*)");
        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
        String where = getWhere(obj);
        if (StringUtils.isNotBlank(where)) {
            sql.WHERE(getWhere(obj));
        } else {
            sql.WHERE("dr = 0");
        }
        return sql.toString();
    }

    private String getSelectColumnNames(T obj, String... tableColumnName) {
        String selectColumnName = null;
        if ((tableColumnName != null) && (tableColumnName.length > 0)) {
            List<String> tableColumnNameList = Arrays.stream(tableColumnName).collect(Collectors.toList());
            selectColumnName = BaseEoUtil.returnSelectColumnsName(obj.getClass(), tableColumnNameList);
            if (SqlUtil.isSpiteSql(selectColumnName)) {
                logger.error("Malice SQL Columns : {}", selectColumnName);
            }
        } else {
            selectColumnName = BaseEoUtil.returnSelectColumnsName(obj.getClass());
        }
        return selectColumnName;
    }

    private String getWhere(T obj) {
        StringBuilder where = new StringBuilder();
        where.append(BaseEoUtil.returnWhereColumnNames(obj, false));
        if (obj.getId() != null) {
            where.append(" and id = #{id}");
        }
        if (StringUtils.isNotEmpty(obj.getCreatePerson())) {
            where.append(" and create_person = #{createPerson}");
        }
        if (StringUtils.isNotEmpty(obj.getUpdatePerson())) {
            where.append(" and update_person = #{updatePerson}");
        }
        return where.toString();
    }

    private String getWhereFormArg0(T obj) {
        StringBuilder where = new StringBuilder();
        where.append(BaseEoUtil.returnWhereColumnNames(obj, true));
        if (obj.getId() != null) {
            where.append(" and id = #{arg0.id}");
        }
        if (StringUtils.isNotEmpty(obj.getCreatePerson())) {
            where.append(" and create_person = #{arg0.createPerson}");
        }
        if (StringUtils.isNotEmpty(obj.getUpdatePerson())) {
            where.append(" and update_person = #{arg0.updatePerson}");
        }
        return where.toString();
    }
}
