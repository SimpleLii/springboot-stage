package com.simplelii.app.common.dao.sql;

import com.simplelii.app.common.dao.base.BaseEo;
import com.simplelii.app.common.utils.BaseEoUtil;
import com.simplelii.app.common.utils.IdUtil;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author SimpleLii
 * @description
 * @date 11:18 2020/6/24
 */
public class BaseSqlTemplate<T extends BaseEo> {

    private static final Long TENANT_CODE = 1L;

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
        return Long.valueOf(IdUtil.nextId(workerId.longValue(), TENANT_CODE));
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
//
//    public String update(T obj) {
//        return getUpdateSql(obj, false);
//    }
//
//    public String updateSelective(T obj) {
//        return getUpdateSql(obj, true);
//    }
//
//    private String getUpdateSql(T obj, boolean flag) {
//        String returnUpdateSet = null;
//        if (flag) {
//            returnUpdateSet = BaseEoUtil.returnUpdateSetNotNull(obj);
//        } else {
//            returnUpdateSet = BaseEoUtil.returnUpdateSet(obj);
//        }
//        String idname = BaseEoUtil.idName(obj.getClass());
//        SQL sql = new SQL();
//        sql.UPDATE(BaseEoUtil.tableName(obj.getClass()));
//        sql.SET(returnUpdateSet);
//        sql.WHERE(idname + "= #{" + idname + "}");
//        return sql.toString();
//    }
//
//    public String updateSelectiveSqlFilter(T obj) {
//        SQL sql = new SQL();
//        sql.UPDATE(BaseEoUtil.tableName(obj.getClass()));
//        sql.SET(BaseEoUtil.returnUpdateSetNotNull(obj));
//        String where = BaseEoUtil.returnUpdateWhereColumnNames(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(where);
//        } else {
//            return null;
//        }
//        return sql.toString();
//    }
//
//    public String delete(T obj) {
//        return deleteLogic(obj, Boolean.valueOf(false));
//    }
//
//    public String deleteLogic(T obj) {
//        return deleteLogic(obj, Boolean.valueOf(true));
//    }
//
//    private String deleteLogic(T obj, Boolean isLogicDel) {
//        String where = getWhere(obj);
//        if ("".equals(where)) {
//            return null;
//        }
//        StringBuilder sqlStr = new StringBuilder();
//        int dr = (isLogicDel == null) || (isLogicDel.booleanValue()) ? 1 : 2;
//        String tableName = BaseEoUtil.tableName(obj.getClass());
//        SQL sql = new SQL();
//        sql.UPDATE(tableName);
//        sql.SET(getUpdateCreatePersonDr(dr, obj.getUpdatePerson()));
//        sql.WHERE(where);
//        sqlStr.append(sql.toString());
//        if ((isLogicDel != null) && (!isLogicDel.booleanValue())) {
//            sql = new SQL();
//            sql.DELETE_FROM(tableName);
//            sql.WHERE(where.replace("dr=0", "dr=" + dr));
//            sqlStr.append(";").append(sql.toString()).append(";");
//        }
//        return sqlStr.toString();
//    }
//
//    public String deleteLogicById(Class<T> aClass, Long id) {
//        return deleteLogicId(aClass, id, Boolean.valueOf(true));
//    }
//
//    public String deleteById(Class<T> aClass, Long id) {
//        return deleteLogicId(aClass, id, Boolean.valueOf(false));
//    }
//
//    private String deleteLogicId(Class<T> aClass, Long id, Boolean isLogicDel) {
//        int dr = (isLogicDel == null) || (isLogicDel.booleanValue()) ? 1 : 2;
//        String tableName = BaseEoUtil.tableName(aClass);
//        StringBuilder sqlStr = new StringBuilder();
//        SQL sql = new SQL();
//        sql.UPDATE(tableName);
//        sql.SET(getUpdateCreatePersonDr(dr, null));
//        sql.WHERE("id = " + id + " and dr = 0");
//        sqlStr.append(sql.toString()).append(";");
//        if ((isLogicDel != null) && (!isLogicDel.booleanValue())) {
//            sql = new SQL();
//            sql.DELETE_FROM(tableName);
//            sql.WHERE("id = " + id + " and dr = 2");
//            sqlStr.append(sql.toString()).append(";");
//        }
//        return sqlStr.toString();
//    }
//
//    public String deleteBatch(Class<T> aClass, Long[] ids) {
//        if ((ids != null) && (ids.length == 1)) {
//            return deleteLogicId(aClass, ids[0], Boolean.valueOf(false));
//        }
//        return deleteLogicBatchIds(aClass, ids, Boolean.valueOf(false));
//    }
//
//    public String deleteLogicBatchIds(Class<T> aClass, Long[] ids) {
//        if ((ids != null) && (ids.length == 1)) {
//            return deleteLogicId(aClass, ids[0], Boolean.valueOf(true));
//        }
//        return deleteLogicBatchIds(aClass, ids, Boolean.valueOf(true));
//    }
//
//    private String deleteLogicBatchIds(Class<T> aClass, Long[] ids, Boolean isLogicDel) {
//        if ((ids == null) || (ids.length < 1)) {
//            return null;
//        }
//        String idStr = StringUtils.join(ids, ",");
//        int dr = (isLogicDel == null) || (isLogicDel.booleanValue()) ? 1 : 2;
//        String tableName = BaseEoUtil.tableName(aClass);
//        StringBuilder sqlStr = new StringBuilder();
//        SQL sql = new SQL();
//        sql.UPDATE(tableName);
//        sql.SET(getUpdateCreatePersonDr(dr, null));
//        sql.WHERE("id in (" + idStr + ") and dr = 0");
//        sqlStr.append(sql.toString()).append(";");
//        if ((isLogicDel != null) && (!isLogicDel.booleanValue())) {
//            sql = new SQL();
//            sql.DELETE_FROM(tableName);
//            sql.WHERE("id in (" + idStr + ") and dr = 2");
//            sqlStr.append(sql.toString()).append(";");
//        }
//        return sqlStr.toString();
//    }
//
//    private String getUpdateCreatePersonDr(int dr, String updatePerson) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("dr=").append(dr).append(",update_time=now()");
//        String reqUupdatePerson = ServiceContext.getContext().getRequestUserCode();
//        if ((StringUtils.isEmpty(reqUupdatePerson)) && (StringUtils.isNotEmpty(updatePerson))) {
//            reqUupdatePerson = updatePerson;
//        }
//        if (StringUtils.isNotEmpty(reqUupdatePerson)) {
//            sb.append(",update_person='").append(reqUupdatePerson).append("'");
//        }
//        return sb.toString();
//    }
//
//    public String find(T obj) {
//        SQL sql = new SQL();
//        sql.SELECT(getSelectColumnNames(obj, new String[0]));
//        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
//        String where = getWhere(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(where);
//        } else {
//            sql.WHERE("dr = 0");
//        }
//        return sql.toString();
//    }
//
//    public String findColumn(T obj, String... tableColumnName) {
//        SQL sql = new SQL();
//        sql.SELECT(getSelectColumnNames(obj, tableColumnName));
//        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
//        String where = getWhereFormArg0(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(where);
//        } else {
//            sql.WHERE("dr = 0");
//        }
//        return sql.toString();
//    }
//
//    public String findIdBySqlFilter(T obj) {
//        SQL sql = new SQL();
//        sql.SELECT("id");
//        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
//        String where = BaseEoUtil.returnUpdateWhereColumnNames(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(where);
//            return sql.toString();
//        }
//        return null;
//    }
//
//    public String findList(T obj) {
//        SQL sql = new SQL();
//        sql.SELECT(getSelectColumnNames(obj, new String[0]));
//        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
//        String where = getWhere(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(where);
//        } else {
//            sql.WHERE("dr = 0");
//        }
//        String orderBy = BaseEoUtil.resultOrderBy(obj);
//        if (StringUtils.isNotBlank(orderBy)) {
//            sql.ORDER_BY(orderBy);
//        }
//        return sql.toString();
//    }
//
//    public String findListColumn(T obj, String... tableColumnName) {
//        SQL sql = new SQL();
//        sql.SELECT(getSelectColumnNames(obj, tableColumnName));
//        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
//        String where = getWhereFormArg0(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(where);
//        } else {
//            sql.WHERE("dr = 0");
//        }
//        String orderBy = BaseEoUtil.resultOrderBy(obj);
//        if (StringUtils.isNotBlank(orderBy)) {
//            sql.ORDER_BY(orderBy);
//        }
//        return sql.toString();
//    }
//
//    public String findPageList(T obj, Integer currentPage, Integer pageSize, SqlEnum sqlEnum) {
//        currentPage = Integer.valueOf(currentPage != null ? currentPage.intValue() : currentPage.intValue() > 0 ? currentPage.intValue() - 1 : 1);
//        pageSize = Integer.valueOf(pageSize != null ? pageSize.intValue() : 10);
//        int pageNo = currentPage.intValue() * pageSize.intValue();
//        StringBuilder strBuilder = new StringBuilder();
//        boolean bool = false;
//        if ((sqlEnum != null) &&
//                (sqlEnum.equals(SqlEnum.postgreSql))) {
//            strBuilder.append(" limit ").append(pageSize).append(" offset ").append(pageNo);
//            bool = true;
//        }
//        if (!bool) {
//            strBuilder.append(" limit ").append(pageNo).append(",").append(pageSize);
//        }
//        String sql = findListColumn(obj, new String[0]);
//        return sql + strBuilder.toString();
//    }
//
//    public String findById(Class<T> aClass, Long id) {
//        return findColumnById(aClass, id, new String[0]);
//    }
//
//    public String findByIdsDr(Class<T> aClass, Long[] ids, Boolean containsDr) {
//        return findColumnByIdsDr(aClass, ids, containsDr, new String[0]);
//    }
//
//    public String findColumnByIdsDr(Class<T> aClass, Long[] ids, Boolean containsDr, String... tableColumnName) {
//        String selectColumnName = null;
//        if ((tableColumnName != null) && (tableColumnName.length > 0)) {
//            selectColumnName = StringUtils.join(tableColumnName, ",").toLowerCase();
//            if (!SqlUtil.isSpiteParams(selectColumnName)) {
//                selectColumnName = BaseEoUtil.returnSelectColumnsName(aClass);
//            }
//        } else {
//            selectColumnName = BaseEoUtil.returnSelectColumnsName(aClass);
//        }
//        SQL sql = new SQL();
//        sql.SELECT(selectColumnName);
//        sql.FROM(BaseEoUtil.tableName(aClass));
//        String idname = BaseEoUtil.idName(aClass);
//        String where = null;
//        if (ids.length == 1) {
//            where = idname + " = " + ids[0];
//        } else {
//            where = idname + " in (" + StringUtils.join(ids, ",") + ")";
//        }
//        if (!containsDr.booleanValue()) {
//            where = where + " and dr=0";
//        }
//        sql.WHERE(where);
//
//        return sql.toString();
//    }
//
//    public String findColumnById(Class<T> aClass, Long id, String... tableColumnName) {
//        String selectColumnName = null;
//        if ((tableColumnName != null) && (tableColumnName.length > 0)) {
//            selectColumnName = StringUtils.join(tableColumnName, ",").toLowerCase();
//            if (!SqlUtil.isSpiteParams(selectColumnName)) {
//                selectColumnName = BaseEoUtil.returnSelectColumnsName(aClass);
//            }
//        } else {
//            selectColumnName = BaseEoUtil.returnSelectColumnsName(aClass);
//        }
//        SQL sql = new SQL();
//        sql.SELECT(selectColumnName);
//        sql.FROM(BaseEoUtil.tableName(aClass));
//        String idname = BaseEoUtil.idName(aClass);
//        sql.WHERE(idname + " = " + id + " and dr=0");
//        return sql.toString();
//    }
//
//    public String findAll(Class<T> aClass) {
//        SQL sql = new SQL();
//        sql.SELECT(BaseEoUtil.returnSelectColumnsName(aClass));
//        sql.FROM(BaseEoUtil.tableName(aClass));
//        sql.WHERE("dr = 0");
//        return sql.toString();
//    }
//
//    public String count(Class<T> aClass) {
//        SQL sql = new SQL();
//        sql.SELECT("count(1)");
//        sql.FROM(BaseEoUtil.tableName(aClass));
//        sql.WHERE("dr = 0");
//        return sql.toString();
//    }
//
//    public String countCondition(T obj) {
//        SQL sql = new SQL();
//        sql.SELECT("count(1)");
//        sql.FROM(BaseEoUtil.tableName(obj.getClass()));
//        String where = getWhere(obj);
//        if (StringUtils.isNotBlank(where)) {
//            sql.WHERE(getWhere(obj));
//        } else {
//            sql.WHERE("dr = 0");
//        }
//        return sql.toString();
//    }
//
//    private String getSelectColumnNames(T obj, String... tableColumnName) {
//        String selectColumnName = null;
//        if ((tableColumnName != null) && (tableColumnName.length > 0)) {
//            selectColumnName = StringUtils.join(tableColumnName, ",").toLowerCase();
//            if (!SqlUtil.isSpiteParams(selectColumnName)) {
//                selectColumnName = BaseEoUtil.returnSelectColumnsName(obj.getClass());
//            }
//        } else {
//            selectColumnName = BaseEoUtil.returnSelectColumnsName(obj.getClass());
//        }
//        return selectColumnName;
//    }
//
//    private String getWhere(T obj) {
//        StringBuilder where = new StringBuilder();
//        where.append(BaseEoUtil.returnWhereColumnNames(obj, false));
//        if (obj.getId() != null) {
//            where.append(" and id = #{id}");
//        }
//        if (StringUtils.isNotEmpty(obj.getCreatePerson())) {
//            where.append(" and create_person = #{createPerson}");
//        }
//        if (StringUtils.isNotEmpty(obj.getUpdatePerson())) {
//            where.append(" and update_person = #{updatePerson}");
//        }
//        if (obj.getTenantId() != null) {
//            where.append(" and tenant_id = #{tenantId}");
//        }
//        if (obj.getInstanceId() != null) {
//            where.append(" and instance_id = #{instanceId}");
//        }
//        return where.toString();
//    }
//
//    private String getWhereFormArg0(T obj) {
//        StringBuilder where = new StringBuilder();
//        where.append(BaseEoUtil.returnWhereColumnNames(obj, true));
//        if (obj.getId() != null) {
//            where.append(" and id = #{arg0.id}");
//        }
//        if (StringUtils.isNotEmpty(obj.getCreatePerson())) {
//            where.append(" and create_person = #{arg0.createPerson}");
//        }
//        if (StringUtils.isNotEmpty(obj.getUpdatePerson())) {
//            where.append(" and update_person = #{arg0.updatePerson}");
//        }
//        if (obj.getTenantId() != null) {
//            where.append(" and tenant_id = #{arg0.tenantId}");
//        }
//        if (obj.getInstanceId() != null) {
//            where.append(" and instance_id = #{arg0.instanceId}");
//        }
//        return where.toString();
//    }
}
