package com.simplelii.app.common.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.simplelii.app.common.dao.base.BaseEo;
import com.simplelii.app.common.dao.sql.SqlCondition;
import com.simplelii.app.common.dao.table.ColumnInfo;
import com.simplelii.app.common.dao.table.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.simplelii.app.common.dao.sql.SqlCondition.Operator.ge;
import static com.simplelii.app.common.dao.sql.SqlCondition.Operator.gt;


/**
 * @author SimpleLii
 * @description 表基础eo工具类
 * @date 11:51 2020/6/24
 */
public class BaseEoUtil {

    private static Logger logger = LoggerFactory.getLogger(BaseEoUtil.class);
    private static Long workerId = null;
    // 缓存表映射对象信息
    private static final Map<String, TableInfo> tables = new ConcurrentHashMap();
    private static final Random random = new Random();
    private static List<ColumnInfo> baseColumns;
    private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");

    // insert default columns
    private static final String DEF_COLUMNS = "create_time,update_time,create_person,update_person,dr";
    // updatePerson
    private static final String UPDATE_PERSON = "default";

    public static Long getWorkerId() {
        if (null != workerId) {
            return workerId;
        }
        try {
            String localHost = InetAddress.getLocalHost().getHostAddress();
            if (StringUtils.isNotBlank(localHost)) {
                String[] ipSegments = localHost.split("\\.");
                if (ipSegments.length == 4) {
                    workerId = Long.parseLong(ipSegments[0]) + Long.parseLong(ipSegments[1]) + Long.parseLong(ipSegments[2]) + Long.parseLong(ipSegments[3]);
                }
            }
        } catch (Exception e) {
            logger.error("error ip generate app.worker.id.", e);
        } finally {
            if (null == workerId) {
                logger.warn("use random to generate app.worker.id");
                workerId = Long.valueOf(random.nextInt(1023));
            }
        }
        return workerId;
    }

    public static <T extends BaseEo> String idName(Class<T> aClass) throws RuntimeException {
        String name = aClass.getName();
        String idColumn = null;
        if (tables.containsKey(name)) {
            idColumn = ((TableInfo) tables.get(name)).getIdColumn();
        } else {
            idColumn = getTableInfo(aClass).getIdColumn();
        }
        if (null == idColumn) {
            throw new RuntimeException("Undefine POJO @Id");
        }
        return idColumn;
    }

    public static <T extends BaseEo> String tableName(Class<T> aClass) throws RuntimeException {
        String name = aClass.getName();
        if (tables.containsKey(name)) {
            return ((TableInfo) tables.get(name)).getTableName();
        }
        return getTableInfo(aClass).getTableName();
    }

    /**
     * 应用程序初次调用，缓存TableInfo对象
     *
     * @param aClass
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    private static <T extends BaseEo> TableInfo getTableInfo(Class<T> aClass) throws RuntimeException {
        String name = aClass.getName();
        Table table = aClass.getAnnotation(Table.class);
        TableInfo tableInfo = new TableInfo();
        if (table != null) {
            tableInfo.setTableName(table.name());
        } else {
            throw new RuntimeException("Undefine POJO @Table, need Annotation(@Table(name))");
        }
        Field[] declaredFields = BaseEo.class.getDeclaredFields();
        Class<?> idTypeClazz = Long.TYPE;
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                tableInfo.setIdColumn(field.getName());
                idTypeClazz = field.getType();
                break;
            }
        }
        if (baseColumns == null) {
            try {
                baseColumns = new ArrayList(declaredFields.length);
                getColumnInfos(baseColumns, null, null, declaredFields);
                // 单独处理id字段
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumn("id");
                columnInfo.setProperty("id");
                columnInfo.setPropertyClass(idTypeClazz);
                baseColumns.add(columnInfo);
            } catch (Exception localException1) {
                logger.info("Eo_Class与表字段映射异常: {}", name);
            }
        }
        TableInfo oldTableInfo = tables.putIfAbsent(name, tableInfo);
        if (oldTableInfo != null) {
            oldTableInfo.setBaseColumns(baseColumns);
            return oldTableInfo;
        }
        tableInfo.setBaseColumns(baseColumns);
        return tableInfo;
    }

    /**
     * 返回eo对象对应的TableInfo对象
     *
     * @param aClass
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    public static <T extends BaseEo> TableInfo getTableInfoWithColumn(Class<T> aClass) throws RuntimeException {
        String name = aClass.getName();
        TableInfo tableInfo = null;
        if (tables.containsKey(name)) {
            tableInfo = tables.get(name);
            if (tableInfo.getColumns() != null) {
                return tableInfo;
            }
        }
        getColumnList(aClass);
        return tables.get(name);
    }

    /**
     * 获取表字段对象信息
     *
     * @param aClass
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    private static <T extends BaseEo> List<ColumnInfo> getColumnList(Class<T> aClass) throws RuntimeException {
        String name = aClass.getName();
        TableInfo tableInfo = null;
        if (tables.containsKey(name)) {
            tableInfo = tables.get(name);
            if (tableInfo.getColumns() != null) {
                return tableInfo.getColumns();
            }
        } else {
            tableInfo = getTableInfo(aClass);
        }
        // 获取表的非默认字段
        List<ColumnInfo> columnList = new ArrayList<>();
        Set<String> columnSet = new HashSet<>();
        Class<?> clazz = aClass;
        String shardingColumnFiled = null;
        for (; clazz != BaseEo.class; clazz = clazz.getSuperclass()) {
            try {
//                ShardingColumn shardingColumn = (ShardingColumn) clazz.getAnnotation(ShardingColumn.class);
//                if (shardingColumn != null) {
//                    shardingColumnFiled = shardingColumn.name();
//                }
                Field[] declaredFields = clazz.getDeclaredFields();
                getColumnInfos(columnList, columnSet, shardingColumnFiled, declaredFields);
            } catch (Exception localException) {
            }
        }
        tableInfo.setColumns(columnList);
        tables.put(name, tableInfo);
        return columnList;
    }

    private static void getColumnInfos(List<ColumnInfo> columnList, Set<String> columnSet, String shardingColumnFiled, Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = field.getName();
                if ((columnSet == null) || (!columnSet.contains(columnName))) {
                    ColumnInfo columnInfo = new ColumnInfo();
                    columnInfo.setProperty(columnName);
                    columnInfo.setPropertyClass(field.getType());
                    String tableColumn = !"".equals(column.name()) ? column.name() : CamelToUnderlineUtil.camelToUnderline(columnName);
                    columnInfo.setColumn(tableColumn);
//                    if ((null != shardingColumnFiled) && (shardingColumnFiled.equals(tableColumn))) {
//                        columnInfo.setShardColumn(true);
//                    }
                    columnList.add(columnInfo);
                    if (columnSet != null) {
                        columnSet.add(columnName);
                    }
                }
            }
        }
    }

//    private static <T extends BaseEo> boolean isWhereNull(T obj, String fieldName) {
//        Class<?> aClass = obj.getClass();
//        try {
//            return fieldValueWhere(aClass, obj, fieldName);
//        } catch (Exception e) {
//            for (Class<?> clazz = aClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
//                try {
//                    if (clazz == BaseDefEo.class) {
//                        return fieldValueWhere(clazz, obj, fieldName);
//                    }
//                } catch (Exception localException1) {
//                }
//            }
//        }
//        return true;
//    }

    private static <T extends BaseEo> boolean fieldValueWhere(Class<?> aClass, T obj, String fieldName)
            throws Exception {
        Class<?> clazz = aClass;
        while ((clazz != BaseEo.class) && (clazz != Object.class)) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                if ((field.get(obj) != null) && (!"".equals(field.get(obj)))) {
                    return false;
                }
                return true;
            } catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        return true;
    }

//    private static <T extends BaseEo> boolean isNull(T obj, String fieldName) {
//        Class<?> aClass = obj.getClass();
//        try {
//            return fieldValueBoolean(aClass, obj, fieldName);
//        } catch (Exception e) {
//            for (Class<?> clazz = aClass; (clazz != BaseEo.class) && (clazz != Object.class); clazz = clazz.getSuperclass()) {
//                try {
//                    if (clazz == BaseDefEo.class) {
//                        return fieldValueBoolean(clazz, obj, fieldName);
//                    }
//                } catch (Exception localException1) {
//                }
//            }
//        }
//        return true;
//    }

    /**
     * 判断eo对应中是否有值 isNotNull
     *
     * @param aClass
     * @param obj
     * @param fieldName
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T extends BaseEo> boolean fieldValueBoolean(Class<?> aClass, T obj, String fieldName) throws Exception {
        Class<?> clazz = aClass;
        while ((clazz != BaseEo.class) && (clazz != Object.class)) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj) == null;
            } catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        return false;
    }

    public static <T extends BaseEo> String returnSelectColumnsName(Class<T> aClass) throws Exception {
        StringBuilder sb = new StringBuilder("id,create_time as createTime,create_person as createPerson,update_time as updateTime,update_person as updatePerson,dr as dr");
        for (ColumnInfo map : getColumnList(aClass)) {
            sb.append(',');
            sb.append(map.getColumn());
            sb.append(" as ");
            sb.append(map.getProperty());
        }
        return sb.toString();
    }

    /**
     * 获取表插入的字段
     *
     * @param obj
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    public static <T extends BaseEo> String returnInsertColumnsName(T obj) throws RuntimeException {
        Class<T> aClass = (Class<T>) obj.getClass();
        StringBuilder sb = new StringBuilder("id,");
        // List<ColumnInfo>
        for (ColumnInfo map : getColumnList(aClass)) {
            if (!isNull(obj, map.getProperty())) {
                sb.append(map.getColumn()).append(',');
            }
        }
        sb.append(DEF_COLUMNS);
        return sb.toString();
    }


    private static <T extends BaseEo> boolean isNull(T obj, String fieldName) {
        Class<?> aClass = obj.getClass();
        try {
            return fieldValueBoolean(aClass, obj, fieldName);
        } catch (Exception e) {
//            for (Class<?> clazz = aClass; (clazz != BaseEo.class) && (clazz != Object.class); clazz = clazz.getSuperclass()) {
//                try {
//                    if (clazz == BaseDefEo.class) {
//                        return fieldValueBoolean(clazz, obj, fieldName);
//                    }
//                } catch (Exception localException1) {
//                }
//            }
        }
        return true;
    }

    /**
     * 获取eo对应的sql字符串  eg: #{id},#{address} ...
     *
     * @param obj
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    public static <T extends BaseEo> String returnInsertColumnsDef(T obj) throws RuntimeException {
        Class<T> aClass = (Class<T>) obj.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("#{id}").append(',');
        for (ColumnInfo map : getColumnList(aClass)) {
            if (!isNull(obj, map.getProperty())) {
                sb.append("#{").append(map.getProperty()).append("},");
            }
        }
        sb.append(insertValue(obj, Boolean.FALSE));
        return sb.toString();
    }

    public static <T extends BaseEo> String returnInsertColumnsNameBatch(Class<T> aClass) {
        StringBuilder sb = new StringBuilder("id,");
        for (ColumnInfo map : getColumnList(aClass)) {
            sb.append(map.getColumn()).append(',');
        }
        sb.append(DEF_COLUMNS);
        return sb.toString();
    }

    /**
     * 拼接sql占位符
     * objList[{0}]  中 {0} 是String格式化的站位符
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends BaseEo> String returnInsertColumnsDefBatch(T obj) {
        Class<T> aClass = (Class<T>) obj.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("#'{'objList[{0}].id'}'").append(',');
        for (ColumnInfo map : getColumnList(aClass)) {
            sb.append("#'{'objList[{0}].").append(map.getProperty()).append("'}',");
        }
        sb.append(insertValue(obj, Boolean.TRUE)).append(")");
        return sb.toString();
    }

    private static <T extends BaseEo> String insertValue(T obj, Boolean isBatch) {
        StringBuilder sb = new StringBuilder();
        obj.setCreateTime(new Date());
        obj.setUpdateTime(new Date());
        if ((null == obj.getCreatePerson()) || ("0".equals(obj.getCreatePerson()))) {
            String requestUserCode = "";
            if (StringUtils.isBlank(requestUserCode)) {
                obj.setCreatePerson("");
            } else {
                obj.setCreatePerson(requestUserCode);
            }
        }
        if ((null == obj.getUpdatePerson()) || ("0".equals(obj.getUpdatePerson()))) {
            String requestUserCode = "";
            if (StringUtils.isBlank(requestUserCode)) {
                obj.setUpdatePerson("");
            } else {
                obj.setUpdatePerson(requestUserCode);
            }
        }
        if (isBatch) {
            sb.append("#'{'objList[{0}].createTime'}'").append(',').append("#'{'objList[{0}].createTime'}'").append(',').append("#'{'objList[{0}].createPerson'}'").append(',').append("#'{'objList[{0}].createPerson'}'").append(',').append("#'{'objList[{0}].dr'}'");
        } else {
            sb.append("#{createTime}").append(',').append("#{createTime}").append(',').append("#{createPerson}").append(',').append("#{createPerson}").append(',').append("#{dr}");
        }
        return sb.toString();
    }

    /**
     * 封装eo对象所有的字段映射sql
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends BaseEo> String returnUpdateSet(T obj) {
        Class<T> aClass = (Class<T>) obj.getClass();
        StringBuilder sb = new StringBuilder(updateColumn(obj));
        List<ColumnInfo> columnList = getColumnList(aClass);
        for (ColumnInfo columnInfo : columnList) {
            if (!columnInfo.isShardColumn()) {
                sb.append(',');
                sb.append(columnInfo.getColumn()).append("=#{").append(columnInfo.getProperty()).append('}');
            }
        }
        return sb.toString();
    }

    /**
     * 拼接更新人更新时间字段
     *
     * @param obj
     * @param <T>
     * @return
     */
    private static <T extends BaseEo> String updateColumn(T obj) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("update_time").append("=now()");
        String updatePerson = null;
        if ((null != obj.getUpdatePerson()) && (!"0".equals(obj.getUpdatePerson()))) {
            updatePerson = obj.getUpdatePerson();
        } else {
            updatePerson = UPDATE_PERSON;
        }
        stringBuilder.append(",update_person").append("='").append(updatePerson).append("'");
        return stringBuilder.toString();
    }

    /**
     * 封装eo对象有值的sql拼接
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends BaseEo> String returnUpdateSetNotNull(T obj) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        List<ColumnInfo> columnList = getColumnList(obj.getClass());
        if ((columnList != null) && (columnList.size() > 0)) {
            sb.append(updateColumn(obj));
            i = 1;
        }
        for (ColumnInfo columnInfo : columnList) {
            if ((!isNull(obj, columnInfo.getProperty())) && (!columnInfo.isShardColumn())) {
                if (i++ != 0) {
                    sb.append(',');
                }
                sb.append(columnInfo.getColumn()).append("=#{").append(columnInfo.getProperty()).append('}');
            }
        }
        return sb.toString();
    }

    //    public static <T extends BaseEo> String returnWhereColumnNames(T obj, boolean appendArg0) {
//        Class<T> aClass = obj.getClass();
//        List<ColumnInfo> columnList = getColumnList(aClass);
//        Set<String> columnsList = new HashSet();
//        StringBuilder sb = new StringBuilder();
//        List<SqlCondition> sqlFilters = obj.getSqlFilters();
//        if ((sqlFilters != null) && (sqlFilters.size() > 0)) {
//            for (SqlCondition sqlCondition : sqlFilters) {
//                if ((sqlCondition != null) && (!org.apache.commons.lang3.StringUtils.isEmpty(sqlCondition.getProperty()))) {
//                    String sqlFilterWhere = getSqlFilterWhere(aClass, sqlCondition);
//                    if (org.apache.commons.lang3.StringUtils.isNotBlank(sqlFilterWhere)) {
//                        if (sb.length() > 0) {
//                            sb.append(" and ");
//                        }
//                        sb.append(sqlFilterWhere);
//                    }
//                    columnsList.add(sqlCondition.getProperty());
//                }
//            }
//        }
//        if (!columnsList.contains("dr")) {
//            if (sb.length() > 1) {
//                sb.append(" and ");
//            }
//            if (obj.getDr().intValue() == 1) {
//                sb.append("dr").append("=1");
//            } else {
//                sb.append("dr").append("=0");
//            }
//        }
//        for (ColumnInfo map : columnList) {
//            if ((!isWhereNull(obj, map.getProperty())) &&
//
//                    (!columnsList.contains(map.getProperty()))) {
//                if (sb.length() > 1) {
//                    sb.append(" and ");
//                }
//                if (appendArg0) {
//                    sb.append(map.getColumn()).append("=#{arg0.").append(map.getProperty()).append("}");
//                } else {
//                    sb.append(map.getColumn()).append("=#{").append(map.getProperty()).append("}");
//                }
//            }
//        }
//        return sb.toString();
//    }
//

    /**
     * 针对单个 column 封装where条件
     *
     * @param aClass
     * @param sqlCondition eo对象中的 sqlCondition
     * @param <T>
     * @return
     */
    private static <T extends BaseEo> String getSqlFilterWhere(Class<T> aClass, SqlCondition sqlCondition) {
        if (sqlCondition.getValue() == null) {
            switch (sqlCondition.getOperator()) {
                case isNull:
                case isNotNull:
                    break;
                default:
                    return "";
            }
        }
        TableInfo tableInfo = getTableInfoWithColumn(aClass);
        StringBuilder sqlWhere = new StringBuilder();
        ColumnInfo columnInfo = tableInfo.getColumnInfo(sqlCondition.getProperty());
        if (null == columnInfo) {
            return "";
        }
        // 字段
        sqlWhere.append(columnInfo.getColumn());
        switch (sqlCondition.getOperator()) {
            case eq:
                if ((sqlCondition.getValue() instanceof String)) {
                    String value = String.valueOf(sqlCondition.getValue()).replace("'", "\\'");
                    sqlWhere.append(" = '").append(value + "'");
                } else {
                    sqlWhere.append(" = ").append(sqlCondition.getValue());
                }
                break;
            case ne:
                if ((sqlCondition.getValue() instanceof String)) {
                    String value = String.valueOf(sqlCondition.getValue()).replace("'", "\\'");
                    sqlWhere.append(" <> '").append(value + "'");
                } else {
                    sqlWhere.append(" <> ").append(sqlCondition.getValue());
                }
                break;
            case gt:
            case lt:
                if (sqlCondition.getOperator() == gt) {
                    sqlWhere.append(" > ");
                } else {
                    sqlWhere.append(" < ");
                }
                if (((sqlCondition.getValue() instanceof Date)) || ((sqlCondition.getValue() instanceof LocalDate))) {
                    sqlWhere.append("'").append(SDF_DATE.format(sqlCondition.getValue())).append("'");
                } else if ((sqlCondition.getValue() instanceof LocalDateTime)) {
                    sqlWhere.append("'").append(SDF_DATETIME.format(sqlCondition.getValue())).append("'");
                } else if ((sqlCondition.getValue() instanceof String)) {
                    String value = String.valueOf(sqlCondition.getValue()).replace("'", "\\'");
                    sqlWhere.append("'").append(value).append("'");
                } else {
                    sqlWhere.append(sqlCondition.getValue());
                }
                break;
            case ge:
            case le:
                String time = " 00:00:01'";
                if (sqlCondition.getOperator() == ge) {
                    sqlWhere.append(" >= ");
                } else {
                    sqlWhere.append(" <= ");
                    time = " 23:59:59'";
                }
                if (((sqlCondition.getValue() instanceof Date)) || ((sqlCondition.getValue() instanceof LocalDate))) {
                    sqlWhere.append("'").append(SDF_DATE.format(sqlCondition.getValue())).append(time);
                } else if ((sqlCondition.getValue() instanceof LocalDateTime)) {
                    sqlWhere.append("'").append(SDF_DATETIME.format(sqlCondition.getValue())).append("'");
                } else if ((sqlCondition.getValue() instanceof String)) {
                    String value = String.valueOf(sqlCondition.getValue()).replace("'", "\\'");
                    sqlWhere.append("'").append(value).append("'");
                } else {
                    sqlWhere.append(sqlCondition.getValue());
                }
                break;
            case like:
                String value = likeValue(sqlCondition.getValue().toString());
                sqlWhere.append(" like '").append(value.replaceAll("'", "''")).append("'");
                break;
            case in:
                String filterValue = processOperatorInValue(columnInfo.getPropertyClass(), sqlCondition);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(filterValue)) {
                    sqlWhere.append(" in (").append(filterValue).append(")");
                } else {
                    return "";
                }
                break;
            case isNull:
                sqlWhere.append(" is null");
                break;
            case isNotNull:
                sqlWhere.append(" is not null");
        }
        return sqlWhere.toString();
    }

    protected static <T extends BaseEo> String processOperatorInValue(Class<?> fieldClass, SqlCondition sqlCondition) {
        if ((sqlCondition == null) || (sqlCondition.getValue() == null)) {
            return null;
        }
        List<String> processedValues = Lists.newArrayList();
        try {
            if (String.class.equals(fieldClass)) {
                Object conditionValue = sqlCondition.getValue();
                if ((conditionValue instanceof String)) {
                    String[] splitValues = ((String) conditionValue).split(",");
                    for (String splitValue : splitValues) {
                        if (!splitValue.startsWith("'")) {
                            splitValue = "'" + splitValue;
                        }
                        if (!splitValue.endsWith("'")) {
                            splitValue = splitValue + "'";
                        }
                        processedValues.add(splitValue);
                    }
                } else if ((conditionValue instanceof Collection)) {
                    Collection<?> listFilterValues = (Collection) conditionValue;

                    for (Iterator<?> iterator = listFilterValues.iterator(); iterator.hasNext(); ) {
                        Object listValue =iterator.next();
                        if (listValue != null) {
                            if ((listValue instanceof String)) {
                                String processValue = (String) listValue;
                                if (!processValue.startsWith("'")) {
                                    processValue = "'" + processValue;
                                }
                                if (!processValue.endsWith("'")) {
                                    processValue = processValue + "'";
                                }
                                processedValues.add(processValue);
                            } else {
                                processedValues.add("'" + listValue + "'");
                            }
                        }
                    }
                } else {
                    processedValues.add("'" + conditionValue + "'");
                }
                return Joiner.on(",").skipNulls().join(processedValues);
            }
            if ((sqlCondition.getValue() instanceof Collection)) {
                return Joiner.on(",").skipNulls().join((Collection) sqlCondition.getValue());
            }
            return sqlCondition.getValue().toString();
        } catch (Exception e) {
            logger.error("sqlCondition解析property异常", e);
        }
        return null;
    }

    private static <T extends BaseEo> String likeValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        int start = 0;
        int end = value.length();
        String startValue = "";
        String endValue = "";
        if ("%".equals(value.substring(0, 1))) {
            start = 1;
            startValue = "%";
        }
        if ("%".equals(value.substring(value.length() - 1, value.length()))) {
            end = value.length() - 1;
            endValue = "%";
        }
        String v = value.substring(start, end);
        if (v.contains("%")) {
            return startValue + v.replaceAll("%", "\\%") + endValue;
        }
        return value;
    }

    public static <T extends BaseEo> String returnUpdateWhereColumnNames(T obj) {
        List<SqlCondition> sqlConditions = obj.getSqlConditions();
        if ((sqlConditions == null) || (sqlConditions.isEmpty())) {
            return null;
        }
        Class<T> aClass = (Class<T>) obj.getClass();
        StringBuilder sb = new StringBuilder();
        boolean hasIdComlumn = false;
        SqlCondition sqlCondition;
        // 封装where条件sql
        for (Iterator localIterator = sqlConditions.iterator(); localIterator.hasNext(); ) {
            sqlCondition = (SqlCondition) localIterator.next();
            if ((sqlCondition != null) && (!StringUtils.isEmpty(sqlCondition.getProperty()))) {
                if (sqlCondition.getProperty().equals("id")) {
                    hasIdComlumn = true;
                }
                // 循环调用，封装where条件
                String sqlFilterWhere = getSqlFilterWhere(aClass, sqlCondition);
                if (StringUtils.isNotBlank(sqlFilterWhere)) {
                    if (sb.length() > 0) {
                        sb.append(" and ");
                    }
                    sb.append(sqlFilterWhere);
                }
            }
        }
        // 防止恶意sql注入 id = 1245690923534717955 and name = xxx ，错误sql打印，仍然执行sql todo
        if ((sb.length() > 0) && (!SqlUtil.isSpiteParams(sb.toString().toLowerCase()))) {
            logger.error("Malice SQL Attack : {}", sb.toString());
        }
        // 判断 id 与 dr 此特殊字段，这两个字段如果eo对象中有值，则作为updateCondition，而不是作为更新结果
        if (obj.getId() != null) {
            if (sb.length() > 0) {
                sb.append(" and ");
            }
            sb.append("id = #{id}");
        } else if ((!hasIdComlumn) && ((obj.getDr() == null) || (obj.getDr() == 0))) {
            // 如果 eo对象中的条件 SQLCondition 是有值的且dr = 0 或者null （没有指定id）
            boolean isExistDr = false;
            for (SqlCondition condition : sqlConditions) {
                if (condition.getProperty().equalsIgnoreCase("dr")) {
                    isExistDr = true;
                    break;
                }
            }
            // 如果不存在则使用默认值 0
            if (!isExistDr) {
                if (sb.length() > 0) {
                    sb.append(" and ");
                }
                sb.append("dr = 0");
            }
        }
        return sb.toString();
    }

//    public static <T extends BaseEo> String resultOrderBy(T obj) {
//        String orderByDesc = obj.getOrderByDesc();
//        String orderBy = obj.getOrderBy();
//        List<SqlOrderBy> sqlOrderBys = obj.getSqlOrderBys();
//        if ((org.apache.commons.lang3.StringUtils.isEmpty(orderByDesc)) && (org.apache.commons.lang3.StringUtils.isEmpty(orderBy)) && ((sqlOrderBys == null) || (sqlOrderBys.isEmpty()))) {
//            return "id desc";
//        }
//        Class<T> aClass = obj.getClass();
//        TableInfo table = getTableInfoWithColumn(aClass);
//        StringBuilder sb = new StringBuilder();
//        Iterator localIterator;
//        if ((sqlOrderBys != null) && (!sqlOrderBys.isEmpty())) {
//            for (localIterator = sqlOrderBys.iterator(); localIterator.hasNext(); ) {
//                sqlOrderBy = (SqlOrderBy) localIterator.next();
//                if (sb.length() > 0) {
//                    sb.append(',');
//                }
//                columnInfo = table.getColumnInfo(sqlOrderBy.getProperty());
//                if (null != columnInfo) {
//                    sb.append(columnInfo.getColumn()).append(sqlOrderBy.getOrder().getSqlSyntax());
//                } else {
//                    sb.append(sqlOrderBy.getProperty()).append(sqlOrderBy.getOrder().getSqlSyntax());
//                }
//            }
//        }
//        SqlOrderBy sqlOrderBy;
//        ColumnInfo columnInfo;
//        if (!org.apache.commons.lang3.StringUtils.isEmpty(orderByDesc)) {
//            String[] strs = orderByDesc.split(",");
//            sqlOrderBy = strs;
//            columnInfo = sqlOrderBy.length;
//            for (ColumnInfo localColumnInfo1 = 0; localColumnInfo1 < columnInfo; localColumnInfo1++) {
//                String s = sqlOrderBy[localColumnInfo1];
//                if (sb.length() > 0) {
//                    sb.append(',');
//                }
//                ColumnInfo columnInfo = table.getColumnInfo(s);
//                if (null != columnInfo) {
//                    sb.append(columnInfo.getColumn()).append(" desc");
//                } else {
//                    sb.append(s).append(" desc");
//                }
//            }
//        }
//        if (!org.apache.commons.lang3.StringUtils.isEmpty(orderBy)) {
//            String[] strs = orderBy.split(",");
//            sqlOrderBy = strs;
//            columnInfo = sqlOrderBy.length;
//            for (ColumnInfo localColumnInfo2 = 0; localColumnInfo2 < columnInfo; localColumnInfo2++) {
//                String s = sqlOrderBy[localColumnInfo2];
//                if (sb.length() > 0) {
//                    sb.append(',');
//                }
//                ColumnInfo columnInfo = table.getColumnInfo(s);
//                if (null != columnInfo) {
//                    sb.append(table.getColumnInfo(s).getColumn()).append(" asc");
//                } else {
//                    sb.append(s).append(" asc");
//                }
//            }
//        }
//        if (sb.length() == 0) {
//            sb.append("id").append(" desc");
//        }
//        return sb.toString();
//    }
//
//    public static BaseEo build(Class<? extends BaseEo> eoClass)
//            throws Exception {
//        try {
//            String extClass = org.springframework.util.StringUtils.delete(eoClass.getName(), "Eo") + "ExtEo";
//            return (BaseEo) Class.forName(extClass).newInstance();
//        } catch (ClassNotFoundException ex) {
//        }
//        return (BaseEo) eoClass.newInstance();
//    }
//
//    public static BaseEo build(Class<? extends BaseEo> eoClass, Map<String, Object> extFields)
//            throws Exception {
//        try {
//            String extClass = org.springframework.util.StringUtils.delete(eoClass.getName(), "Eo") + "ExtEo";
//            BaseEo eo = (BaseEo) Class.forName(extClass).newInstance();
//            if (null != extFields) {
//                BeanUtils.populate(eo, extFields);
//            }
//            return eo;
//        } catch (ClassNotFoundException ex) {
//        }
//        return (BaseEo) eoClass.newInstance();
//    }
//
//    public static void setWorkerId(String workerId) {
//        workerId = workerId;
//    }
//
//    public static void main(String[] args) {
//        TableInfo table = getTableInfo(BaseEo.class);
//        System.out.println(table.getBaseColumns());
//    }
}
